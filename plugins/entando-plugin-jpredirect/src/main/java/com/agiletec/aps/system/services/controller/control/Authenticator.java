/*
* Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
*
* This library is free software; you can redistribute it and/or modify it under
* the terms of the GNU Lesser General Public License as published by the Free
* Software Foundation; either version 2.1 of the License, or (at your option)
* any later version.
*
* This library is distributed in the hope that it will be useful, but WITHOUT
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
* FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
* details.
*/
package com.agiletec.aps.system.services.controller.control;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.baseconfig.SystemParamsUtils;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.aps.system.services.user.IAuthenticationProviderManager;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpredirect.aps.system.services.redirect.IRedirectManager;
import org.entando.entando.plugins.jpredirect.aps.system.services.redirect.Redirect;

/**
 * Sottoservizio di controllo esecutore dell'autenticazione.
 * @author M.Diana - E.Santoboni
 */
public class Authenticator extends AbstractControlService {
    
    private static final Logger _logger = LoggerFactory.getLogger(Authenticator.class);
    
    
    @Override
    public void afterPropertiesSet() throws Exception {
        _logger.debug("{} ready", this.getClass().getName());
    }
    
    /**
     * Esecuzione.
     * Il metodo service esegue le seguenti operazioni (nell'ordine indicato):
     * 1) se nella richiesta sono presenti dei parametri user e password, viene
     * caricato l'utente relativo; se l'utente restituito è non nullo,
     * lo si mette in sessione; se l'utente restituito è nullo, non si fa nulla.
     * 2) si controlla l'esistenza di un utente in sessione; se non esiste, si richiede
     * un utente di default e lo si mette in sessione.
     * @param reqCtx Il contesto di richiesta
     * @param status Lo stato di uscita del servizio precedente
     * @return Lo stato di uscita
     */
    @Override
    public int service(RequestContext reqCtx, int status) {
        _logger.debug("Invoked {}", this.getClass().getName());
        int retStatus = ControllerManager.INVALID_STATUS;
        
        if (status == ControllerManager.ERROR) {
            return status;
        }
        
        try {
            HttpServletRequest req = reqCtx.getRequest();
            String username = req.getParameter(SystemConstants.LOGIN_USERNAME_PARAM_NAME);
            String password = req.getParameter(SystemConstants.LOGIN_PASSWORD_PARAM_NAME);
            HttpSession session = req.getSession();
            
            //Punto 1
            if (username != null && password != null) {
                String returnUrl = req.getParameter("returnUrl");
                returnUrl = (null != returnUrl && returnUrl.trim().length() > 0) ? returnUrl : null;
                _logger.debug("user {} - password ******** ", username );
                UserDetails user = this.getAuthenticationProvider().getUser(username, password);
             
                if (user != null) {
                    if (!user.isAccountNotExpired()) {
                        req.setAttribute("accountExpired", new Boolean(true));
                    } else {
                        session.setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, user);
                        _logger.debug("New user: {}", user.getUsername());
                        // redirect?
                        if (!username.equals(SystemConstants.ADMIN_USER_NAME)) {
                            String page = conditionalRedirect(user, reqCtx);
                            
                            if (StringUtils.isNotBlank(page)) {
                                _logger.warn("redirecting current user '{}' to page '{}' as configured",
                                        user.getUsername(), page);
                                return super.redirect(page, reqCtx);
                            } else {
                                _logger.info("no redirection active for user '{}'",
                                        user.getUsername());
                            }
                        } 
                        
                        if (null != returnUrl) {
                            return super.redirectUrl(URLDecoder.decode(returnUrl, "ISO-8859-1"), reqCtx);
                        }
                    }
                } else {
                    req.setAttribute("wrongAccountCredential", new Boolean(true));
                    if (null != returnUrl) {
                        req.setAttribute("returnUrl", returnUrl);
                    }
                }
            }
            //Punto 2
            if (session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER) == null) {
                UserDetails guestUser = this.getUserManager().getGuestUser();
                session.setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, guestUser);
            }
            
            retStatus = ControllerManager.CONTINUE;
        } catch (Throwable t) {
            _logger.error("Error, could not fulfill the request", t);
            retStatus = ControllerManager.SYS_ERROR;
            reqCtx.setHTTPError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return retStatus;
    }
    
    /**
     * Find whether a redirection is needed for the given user
     * @param user
     * @return
     */
    protected String conditionalRedirect(UserDetails user, RequestContext ctx) {
        Optional<Object> url = null;
        
        try {               
            List<Group> groups =
                    this.getAuthorizationManager().getUserGroups(user);
            List<String> groupNames =
                    groups.stream().map(g -> g.getName()).collect(Collectors.toList());
            List<Role> roles =
                    this.getAuthorizationManager().getUserRoles(user);
            List<String> roleNames =
                    roles.stream().map(r -> r.getName()).collect(Collectors.toList());
            List<Redirect> redirects =
                    getRedirectManager().getRedirectObjects();
            
            
            Optional<Redirect> result = redirects.stream()
                    .parallel()
                    .filter(r -> (StringUtils.isNotBlank(r.getGroupname())
                                && StringUtils.isNotBlank(r.getRolename())
                                && groupNames.contains(r.getGroupname())
                                && roleNames.contains(r.getRolename()))
                            ||
                                (StringUtils.isNotBlank(r.getGroupname())
                                        && StringUtils.isBlank(r.getRolename())
                                        && groupNames.contains(r.getGroupname()))
                            ||
                                (StringUtils.isBlank(r.getGroupname())
                                        && StringUtils.isNotBlank(r.getRolename())
                                        && roleNames.contains(r.getRolename())))
                    .findFirst();
            
//            url = result.map(r -> getRedirectLink(r.getPagecode(), ctx));
            url = result.map(r -> r.getPagecode());
        } catch (Throwable t) {
            _logger.error("Error on redirection management for user " + user.getUsername(), t);
        }
        return (url.isPresent() ?
                (String) url.get(): null);
    }
    
    /**
     * Create the URL for the desired portal page
     * @param pageCode
     * @param reqCtx
     * @return
     *//*
    private String getRedirectLink(String pageCode, RequestContext reqCtx) {
        String url = null;
        
        if (StringUtils.isNotBlank(pageCode)) {
            HttpServletRequest request = reqCtx.getRequest();
            IURLManager urlManager = (IURLManager) ApsWebApplicationUtils.getBean("URLManager", request);
            ILangManager langManager = (ILangManager) ApsWebApplicationUtils.getBean("LangManager", request);
            Lang currentLang = (Lang)reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
            Page destinationPage = new Page();
            
            if (null == currentLang
                    && null != langManager) {
                currentLang = langManager.getDefaultLang();
                _logger.warn("defaulting to the system language '{}'", currentLang.getCode());
            } else {
                // do nothing
            }
            destinationPage.setCode(pageCode);
            url = urlManager.createURL(destinationPage, currentLang, null);
            _logger.info(" translate page code '{}' to portal URL '{}'", pageCode, url);
        } else {
            _logger.warn("invalid destination page for the current user");
        }
        return url;
    }
    */
    
    protected IUserManager getUserManager() {
        return _userManager;
    }
    public void setUserManager(IUserManager userManager) {
        this._userManager = userManager;
    }
    
    protected IAuthenticationProviderManager getAuthenticationProvider() {
        return _authenticationProvider;
    }
    public void setAuthenticationProvider(IAuthenticationProviderManager authenticationProvider) {
        this._authenticationProvider = authenticationProvider;
    }
    
    public IRedirectManager getRedirectManager() {
        return redirectManager;
    }
    
    public void setRedirectManager(IRedirectManager redirectManager) {
        this.redirectManager = redirectManager;
    }
    
    public IAuthorizationManager getAuthorizationManager() {
        return _authorizationManager;
    }
    
    public void setAuthorizationManager(IAuthorizationManager _authorizationManager) {
        this._authorizationManager = _authorizationManager;
    }
    
    private IUserManager _userManager;
    private IAuthenticationProviderManager _authenticationProvider;
    private IRedirectManager redirectManager;
    private IAuthorizationManager _authorizationManager;
}