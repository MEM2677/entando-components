/*
*
* <Your licensing text here>
*
*/
package org.entando.entando.plugins.jpredirect.aps.system.services.redirect;

import org.entando.entando.plugins.jpredirect.aps.system.services.redirect.event.RedirectChangedEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.Response;
import org.entando.entando.plugins.jpredirect.aps.system.services.redirect.api.JAXBRedirect;
import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiException;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;

public class RedirectManager extends AbstractService implements IRedirectManager {
    
    private static final Logger logger =  LoggerFactory.getLogger(RedirectManager.class);
    
    @Override
    public void init() throws Exception {
        logger.debug("{} ready.", this.getClass().getName());
    }
    
    @Override
    public Redirect getRedirect(int id) throws ApsSystemException {
        Redirect redirect = null;
        try {
            redirect = this.getRedirectDAO().loadRedirect(id);
        } catch (Throwable t) {
            logger.error("Error loading redirect with id '{}'", id,  t);
            throw new ApsSystemException("Error loading redirect with id: " + id, t);
        }
        return redirect;
    }
    
    @Override
    public List<Integer> getRedirects() throws ApsSystemException {
        List<Integer> redirects = new ArrayList<Integer>();
        try {
            redirects = this.getRedirectDAO().loadRedirects();
        } catch (Throwable t) {
            logger.error("Error loading Redirect list",  t);
            throw new ApsSystemException("Error loading Redirect ", t);
        }
        return redirects;
    }
    
    @Override
    public List<Redirect> getRedirectObjects() throws ApsSystemException {
        List<Redirect> redirects = new ArrayList<Redirect>();
        try {
            redirects = this.getRedirectDAO().loadRedirectObjects();
        } catch (Throwable t) {
            logger.error("Error loading Redirect list of objects",  t);
            throw new ApsSystemException("Error loading Redirect of objects ", t);
        }
        return redirects;
    }
    
    @Override
    public List<Integer> searchRedirects(FieldSearchFilter filters[]) throws ApsSystemException {
        List<Integer> redirects = new ArrayList<Integer>();
        try {
            redirects = this.getRedirectDAO().searchRedirects(filters);
        } catch (Throwable t) {
            logger.error("Error searching Redirects", t);
            throw new ApsSystemException("Error searching Redirects", t);
        }
        return redirects;
    }
    
    @Override
    public void addRedirect(Redirect redirect) throws ApsSystemException {
        try {
            this.getRedirectDAO().insertRedirect(redirect);
            this.notifyRedirectChangedEvent(redirect, RedirectChangedEvent.INSERT_OPERATION_CODE);
        } catch (Throwable t) {
            logger.error("Error adding Redirect", t);
            throw new ApsSystemException("Error adding Redirect", t);
        }
    }
    
    @Override
    public void updateRedirect(Redirect redirect) throws ApsSystemException {
        try {
            this.getRedirectDAO().updateRedirect(redirect);
            this.notifyRedirectChangedEvent(redirect, RedirectChangedEvent.UPDATE_OPERATION_CODE);
        } catch (Throwable t) {
            logger.error("Error updating Redirect", t);
            throw new ApsSystemException("Error updating Redirect " + redirect, t);
        }
    }
    
    @Override
    public void deleteRedirect(int id) throws ApsSystemException {
        try {
            Redirect redirect = this.getRedirect(id);
            this.getRedirectDAO().removeRedirect(id);
            this.notifyRedirectChangedEvent(redirect, RedirectChangedEvent.REMOVE_OPERATION_CODE);
        } catch (Throwable t) {
            logger.error("Error deleting Redirect with id {}", id, t);
            throw new ApsSystemException("Error deleting Redirect with id:" + id, t);
        }
    }
    
    
    /**
     * GET http://localhost:8080/<portal>/api/rs/en/redirects?
     * @param properties
     * @return
     * @throws Throwable
     */
    public List<JAXBRedirect> getRedirectsForApi(Properties properties) throws Throwable {
        List<JAXBRedirect> list = new ArrayList<JAXBRedirect>();
        List<Integer> idList = this.getRedirects();
        if (null != idList && !idList.isEmpty()) {
            Iterator<Integer> redirectIterator = idList.iterator();
            while (redirectIterator.hasNext()) {
                int currentid = redirectIterator.next();
                Redirect redirect = this.getRedirect(currentid);
                if (null != redirect) {
                    list.add(new JAXBRedirect(redirect));
                }
            }
        }
        return list;
    }
    
    /**
     * GET http://localhost:8080/<portal>/api/rs/en/redirect?id=1
     * @param properties
     * @return
     * @throws Throwable
     */
    public JAXBRedirect getRedirectForApi(Properties properties) throws Throwable {
        String idString = properties.getProperty("id");
        int id = 0;
        JAXBRedirect jaxbRedirect = null;
        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Invalid Integer format for 'id' parameter - '" + idString + "'", Response.Status.CONFLICT);
        }
        Redirect redirect = this.getRedirect(id);
        if (null == redirect) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Redirect with id '" + idString + "' does not exist", Response.Status.CONFLICT);
        }
        jaxbRedirect = new JAXBRedirect(redirect);
        return jaxbRedirect;
    }
    
    /**
     * POST Content-Type: application/xml http://localhost:8080/<portal>/api/rs/en/redirect
     * @param jaxbRedirect
     * @throws ApiException
     * @throws ApsSystemException
     */
    public void addRedirectForApi(JAXBRedirect jaxbRedirect) throws ApiException, ApsSystemException {
        if (null != this.getRedirect(jaxbRedirect.getId())) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Redirect with id " + jaxbRedirect.getId() + " already exists", Response.Status.CONFLICT);
        }
        Redirect redirect = jaxbRedirect.getRedirect();
        this.addRedirect(redirect);
    }
    
    /**
     * PUT Content-Type: application/xml http://localhost:8080/<portal>/api/rs/en/redirect
     * @param jaxbRedirect
     * @throws ApiException
     * @throws ApsSystemException
     */
    public void updateRedirectForApi(JAXBRedirect jaxbRedirect) throws ApiException, ApsSystemException {
        if (null == this.getRedirect(jaxbRedirect.getId())) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Redirect with id " + jaxbRedirect.getId() + " does not exist", Response.Status.CONFLICT);
        }
        Redirect redirect = jaxbRedirect.getRedirect();
        this.updateRedirect(redirect);
    }
    
    /**
     * DELETE http://localhost:8080/<portal>/api/rs/en/redirect?id=1
     * @param properties
     * @throws ApiException
     * @throws ApsSystemException
     */
    public void deleteRedirectForApi(Properties properties) throws Throwable {
        String idString = properties.getProperty("id");
        int id = 0;
        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Invalid Integer format for 'id' parameter - '" + idString + "'", Response.Status.CONFLICT);
        }
        this.deleteRedirect(id);
    }
    
    private void notifyRedirectChangedEvent(Redirect redirect, int operationCode) {
        RedirectChangedEvent event = new RedirectChangedEvent();
        event.setRedirect(redirect);
        event.setOperationCode(operationCode);
        this.notifyEvent(event);
    }
    
    @SuppressWarnings("rawtypes")
    public SearcherDaoPaginatedResult<Redirect> getRedirects(FieldSearchFilter[] filters) throws ApsSystemException {
        SearcherDaoPaginatedResult<Redirect> pagedResult = null;
        try {
            List<Redirect> redirects = new ArrayList<>();
            int count = this.getRedirectDAO().countRedirects(filters);
            
            List<Integer> redirectNames = this.getRedirectDAO().searchRedirects(filters);
            for (Integer redirectName : redirectNames) {
                redirects.add(this.getRedirect(redirectName));
            }
            pagedResult = new SearcherDaoPaginatedResult<Redirect>(count, redirects);
        } catch (Throwable t) {
            logger.error("Error searching redirects", t);
            throw new ApsSystemException("Error searching redirects", t);
        }
        return pagedResult;
    }
    
    @Override
    public SearcherDaoPaginatedResult<Redirect> getRedirects(List<FieldSearchFilter> filters) throws ApsSystemException {
        FieldSearchFilter[] array = null;
        if (null != filters) {
            array = filters.toArray(new FieldSearchFilter[filters.size()]);
        }
        return this.getRedirects(array);
    }
    
    public void setRedirectDAO(IRedirectDAO redirectDAO) {
        this._redirectDAO = redirectDAO;
    }
    protected IRedirectDAO getRedirectDAO() {
        return _redirectDAO;
    }
    
    private IRedirectDAO _redirectDAO;
}
