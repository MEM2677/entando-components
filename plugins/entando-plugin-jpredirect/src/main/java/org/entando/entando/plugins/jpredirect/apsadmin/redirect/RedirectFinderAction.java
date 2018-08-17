/*
*
* <Your licensing text here>
*
*/
package org.entando.entando.plugins.jpredirect.apsadmin.redirect;

import java.util.List;
import org.apache.commons.lang.StringUtils;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import org.entando.entando.plugins.jpredirect.aps.system.services.redirect.Redirect;
import org.entando.entando.plugins.jpredirect.aps.system.services.redirect.IRedirectManager;
import com.agiletec.apsadmin.system.BaseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedirectFinderAction extends BaseAction {
    
    private static final Logger _logger =  LoggerFactory.getLogger(RedirectFinderAction.class);
    
    public List<Integer> getRedirectsId() {
        try {
            FieldSearchFilter[] filters = new FieldSearchFilter[0];
            if (null != this.getId()) {
                //TODO add a constant into your IRedirectManager class
                FieldSearchFilter filterToAdd = new FieldSearchFilter(("id"), this.getId(), false);
                filters = this.addFilter(filters, filterToAdd);
            }
            if (StringUtils.isNotBlank(this.getPagecode())) {
                //TODO add a constant into your IRedirectManager class
                FieldSearchFilter filterToAdd = new FieldSearchFilter(("pagecode"), this.getPagecode(), true);
                filters = this.addFilter(filters, filterToAdd);
            }
            if (StringUtils.isNotBlank(this.getGroupname())) {
                //TODO add a constant into your IRedirectManager class
                FieldSearchFilter filterToAdd = new FieldSearchFilter(("groupname"), this.getGroupname(), true);
                filters = this.addFilter(filters, filterToAdd);
            }
            if (StringUtils.isNotBlank(this.getRolename())) {
                //TODO add a constant into your IRedirectManager class
                FieldSearchFilter filterToAdd = new FieldSearchFilter(("rolename"), this.getRolename(), true);
                filters = this.addFilter(filters, filterToAdd);
            }
            List<Integer> redirects = this.getRedirectManager().searchRedirects(filters);
            return redirects;
        } catch (Throwable t) {
            _logger.error("Error getting redirects list", t);
            throw new RuntimeException("Error getting redirects list", t);
        }
    }
    
    protected FieldSearchFilter[] addFilter(FieldSearchFilter[] filters, FieldSearchFilter filterToAdd) {
        int len = filters.length;
        FieldSearchFilter[] newFilters = new FieldSearchFilter[len + 1];
        for(int i=0; i < len; i++){
            newFilters[i] = filters[i];
        }
        newFilters[len] = filterToAdd;
        return newFilters;
    }
    
    public Redirect getRedirect(int id) {
        Redirect redirect = null;
        try {
            redirect = this.getRedirectManager().getRedirect(id);
        } catch (Throwable t) {
            _logger.error("Error getting redirect with id {}", id, t);
            throw new RuntimeException("Error getting redirect with id " + id, t);
        }
        return redirect;
    }
    
     public String getPageTitle(String code) {
        try {
            Lang lang = this.getCurrentLang();
            
            if (org.apache.commons.lang3.StringUtils.isNotBlank(code)) {
                IPage page = this.getPageManager().getOnlinePage(code);
            
                return page.getTitle(lang.getCode());
            }
        } catch (Throwable t) {
            _logger.info("error getting page title in the current language");
        }
        return null;
    }
    
    public Integer getId() {
        return _id;
    }
    public void setId(Integer id) {
        this._id = id;
    }
    
    
    public String getPagecode() {
        return _pagecode;
    }
    public void setPagecode(String pagecode) {
        this._pagecode = pagecode;
    }
    
    
    public String getGroupname() {
        return _groupname;
    }
    public void setGroupname(String groupname) {
        this._groupname = groupname;
    }
    
    
    public String getRolename() {
        return _rolename;
    }
    public void setRolename(String rolename) {
        this._rolename = rolename;
    }
    
    
    protected IRedirectManager getRedirectManager() {
        return _redirectManager;
    }
    public void setRedirectManager(IRedirectManager redirectManager) {
        this._redirectManager = redirectManager;
    }

    public IPageManager getPageManager() {
        return pageManager;
    }

    public void setPageManager(IPageManager pageManager) {
        this.pageManager = pageManager;
    }
    
    private Integer _id;
    private String _pagecode;
    private String _groupname;
    private String _rolename;
    private IRedirectManager _redirectManager;
    private IPageManager pageManager;
}