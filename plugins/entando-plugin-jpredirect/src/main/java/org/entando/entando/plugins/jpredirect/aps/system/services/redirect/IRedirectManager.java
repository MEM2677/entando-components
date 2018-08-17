/*
*
* <Your licensing text here>
*
*/
package org.entando.entando.plugins.jpredirect.aps.system.services.redirect;

import java.util.List;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface IRedirectManager {

    public static String BEAN_ID = "jpredirectRedirectManager";
    
    public Redirect getRedirect(int id) throws ApsSystemException;
    
    public List<Integer> getRedirects() throws ApsSystemException;
    
    public List<Integer> searchRedirects(FieldSearchFilter filters[]) throws ApsSystemException;
    
    public void addRedirect(Redirect redirect) throws ApsSystemException;
    
    public void updateRedirect(Redirect redirect) throws ApsSystemException;
    
    public void deleteRedirect(int id) throws ApsSystemException;
    
    public SearcherDaoPaginatedResult<Redirect> getRedirects(List<FieldSearchFilter> fieldSearchFilters) throws ApsSystemException;
    
    public List<Redirect> getRedirectObjects() throws ApsSystemException;
}