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
package com.agiletec.aps.system.services.user;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import javax.security.auth.Subject;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.entando.entando.aps.system.services.oauth2.IApiOAuth2TokenManager;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2Token;
import org.entando.entando.plugins.jpjaasauth.aps.system.services.login.model.JAASUser;
import org.entando.entando.plugins.jpjaasauth.aps.system.services.mapping.IMappingManager;
import org.entando.entando.plugins.jpjaasauth.aps.system.services.mapping.Mapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.Authorization;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.role.Role;

/**
 * Implementazione concreta dell'oggetto Authentication Provider di default del sistema.
 * L'Authentication Provider è l'oggetto delegato alla restituzione di un'utenza 
 * (comprensiva delle sue autorizzazioni) in occasione di una richiesta di autenticazione utente; 
 * questo oggetto non ha visibilità ai singoli sistemi (concreti) delegati alla gestione 
 * delle autorizzazioni.
 * @author E.Santoboni
 */
public class JAASAuthenticationProviderManager extends AbstractService 
		implements IAuthenticationProviderManager {

	private static final Logger _logger = LoggerFactory.getLogger(JAASAuthenticationProviderManager.class);
	
	@Override
    public void init() throws Exception {
        _logger.debug("{} ready", this.getClass().getName() );
    }
    
	@Override
    public UserDetails getUser(String username) throws ApsSystemException {
        return this.extractUser(username, null);
    }
    
	@Override
    public UserDetails getUser(String username, String password) throws ApsSystemException {
        return this.extractUser(username, password);
    }
    
    protected UserDetails extractUser(String username, String password) throws ApsSystemException {
        UserDetails user = null;
        
        try {
            if (null == password) {
                user = this.getUserManager().getUser(username);
            } else {
                user = this.getUserManager().getUser(username, password);
            }
            if (null == user || user.isDisabled()) {
                return null;
            }
            if (!user.getUsername().equals(SystemConstants.ADMIN_USER_NAME)) {
                if (!user.isAccountNotExpired()) {
                    _logger.info("USER ACCOUNT '{}' EXPIRED", user.getUsername());
                    return user;
                }
            }
            this.getUserManager().updateLastAccess(user);
            if (!user.isCredentialsNotExpired()) {
                _logger.info("USER '{}' credentials EXPIRED", user.getUsername());
                return user;
            }
            this.addUserAuthorizations(user);

            if (this.getAuthorizationManager().isAuthOnPermission(user, Permission.SUPERUSER)){
                this.registerToken(user);
            }


        } catch (Throwable t) {
            throw new ApsSystemException("Error detected during the authentication of the user " + username, t);
        }
        return user;
    }

    private void registerToken(final UserDetails user) {

        OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
        try {
            final String accessToken = oauthIssuerImpl.accessToken();
            final String refreshToken = oauthIssuerImpl.refreshToken();
            user.setAccessToken(accessToken);
            user.setRefreshToken(refreshToken);

            final OAuth2Token oAuth2Token = new OAuth2Token();
            oAuth2Token.setAccessToken(accessToken);
            oAuth2Token.setRefreshToken(refreshToken);
            oAuth2Token.setClientId("LOCAL_USER");
            oAuth2Token.setLocalUser(user.getUsername());
            Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
            calendar.add(Calendar.SECOND, 3600);
            oAuth2Token.setExpiresIn(calendar.getTime());
            oAuth2Token.setGrantType(GrantType.IMPLICIT.toString());
            tokenManager.addApiOAuth2Token(oAuth2Token,true);
        } catch (OAuthSystemException e) {
            _logger.error("OAuthSystemException {} ", e.getMessage());
            _logger.debug("OAuthSystemException {} ", e);

        } catch (ApsSystemException e) {
            _logger.error("ApsSystemException {} ", e.getMessage());
            _logger.debug("ApsSystemException {} ", e);
        }


    }

    protected void addUserAuthorizations(UserDetails user) throws ApsSystemException {
        if (null == user) {
            return;
        }
		List<Authorization> auths = this.getAuthorizationManager().getUserAuthorizations(user.getUsername());
		if (null == auths) {
			return;
		}
		for (int i = 0; i < auths.size(); i++) {
			Authorization authorization = auths.get(i);
			user.addAuthorization(authorization);
		}
		if (!user.isEntandoUser() 
				&& user instanceof JAASUser) {
			// map container roles to Entando roles and groups
			
			Subject subject = ((JAASUser)user).getSubject();
			Set<Principal> set = subject.getPrincipals();
			Object[] principals = (Object[]) set.toArray();
			
			// the first element is always the principal and not a role, last is principal that represents the caller principal associated with the invocation being processed by the container 
			if (principals.length < 3) {
				return;
			}
			IntStream.range(1, principals.length - 1)
				.forEach(i -> {
					Object cur = principals[i];
					
					// iterate over the principals of this group
					if (cur instanceof java.security.acl.Group) {
						Enumeration<? extends Principal> en = ((Group)cur).members();
						
						Collections.list(en).forEach(g -> {
							String jaasRole = g.getName();
							
//							System.out.println("!group! " + g.getName() + " ("+ g.getClass().getCanonicalName() +")");
							try {
								associateEntandoAuths((JAASUser)user, jaasRole);
							} catch (Throwable t) {
								_logger.error("error associating JAAS role '{}' with entando authroizations",
										g.getName());
							}
						});
					}
				});
		}
    }

    protected void associateEntandoAuths(JAASUser user, String jaasRole) throws Throwable {
    	
    	List<Mapping> mappings = this.getMappingManager().getMappingsByJaasRole(jaasRole);
    	
		mappings.forEach(m -> {
			com.agiletec.aps.system.services.group.Group group =
					this.getGroupManager().getGroup(m.getEgroup());
    		Role role =
    				this.getRoleManager().getRole(m.getErole());
    		
    		Authorization auth = new Authorization(group, role);
    		user.addAuthorization(auth);
    		if (null != role 
    				&& null != group) {
    			_logger.info("associating group '{}', role '{}' to user '{}'",
    					group.getName(), role.getName(), user.getUsername());
    		} else if (null == role) {
    			_logger.info("associating group '{}' to user '{}'",
    					group.getName(), user.getUsername());
    		} else {
    			_logger.info("associating role '{}' to user '{}'",
    					role.getName(), user.getUsername());
    		}
    	});
    }
    
    protected IUserManager getUserManager() {
        return _userManager;
    }
    public void setUserManager(IUserManager userManager) {
        this._userManager = userManager;
    }
	
	protected IAuthorizationManager getAuthorizationManager() {
		return _authorizationManager;
	}
	public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
		this._authorizationManager = authorizationManager;
	}

    public IApiOAuth2TokenManager getTokenManager() {
        return tokenManager;
    }

    public void setTokenManager(IApiOAuth2TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    public IMappingManager getMappingManager() {
		return mappingManager;
	}

	public void setMappingManager(IMappingManager mappingManager) {
		this.mappingManager = mappingManager;
	}

	public IGroupManager getGroupManager() {
		return groupManager;
	}

	public void setGroupManager(IGroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public IRoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(IRoleManager roleManager) {
		this.roleManager = roleManager;
	}

	private IUserManager _userManager;
	private IAuthorizationManager _authorizationManager;
    private IApiOAuth2TokenManager tokenManager;
    private IMappingManager mappingManager;
    private IGroupManager groupManager;
    private IRoleManager roleManager;
    
}
