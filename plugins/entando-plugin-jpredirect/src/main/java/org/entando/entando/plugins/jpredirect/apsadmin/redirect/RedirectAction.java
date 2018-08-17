/*
*
* <Your licensing text here>
*
*/
package org.entando.entando.plugins.jpredirect.apsadmin.redirect;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.role.Role;
import org.entando.entando.plugins.jpredirect.aps.system.services.redirect.Redirect;
import org.entando.entando.plugins.jpredirect.aps.system.services.redirect.IRedirectManager;



import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedirectAction extends BaseAction {
    
    private static final Logger _logger =  LoggerFactory.getLogger(RedirectAction.class);
    
    public String newRedirect() {
        try {
            this.setStrutsAction(ApsAdminSystemConstants.ADD);
        } catch (Throwable t) {
            _logger.error("error in newRedirect", t);
            return FAILURE;
        }
        return SUCCESS;
    }
    
    public String edit() {
        try {
            Redirect redirect = this.getRedirectManager().getRedirect(this.getId());
            if (null == redirect) {
                this.addActionError(this.getText("error.redirect.null"));
                return INPUT;
            }
            this.populateForm(redirect);
            this.setStrutsAction(ApsAdminSystemConstants.EDIT);
        } catch (Throwable t) {
            _logger.error("error in edit", t);
            return FAILURE;
        }
        return SUCCESS;
    }
    
    public String save() {
        try {
            Redirect redirect = this.createRedirect();
            int strutsAction = this.getStrutsAction();
            if (ApsAdminSystemConstants.ADD == strutsAction) {
                this.getRedirectManager().addRedirect(redirect);
            } else if (ApsAdminSystemConstants.EDIT == strutsAction) {
                this.getRedirectManager().updateRedirect(redirect);
            }
        } catch (Throwable t) {
            _logger.error("error in save", t);
            return FAILURE;
        }
        return SUCCESS;
    }
    
    public String trash() {
        try {
            Redirect redirect = this.getRedirectManager().getRedirect(this.getId());
            if (null == redirect) {
                this.addActionError(this.getText("error.redirect.null"));
                return INPUT;
            }
            this.populateForm(redirect);
            this.setStrutsAction(ApsAdminSystemConstants.DELETE);
        } catch (Throwable t) {
            _logger.error("error in trash", t);
            return FAILURE;
        }
        return SUCCESS;
    }
    
    public String delete() {
        try {
            if (this.getStrutsAction() == ApsAdminSystemConstants.DELETE) {
                this.getRedirectManager().deleteRedirect(this.getId());
            }
        } catch (Throwable t) {
            _logger.error("error in delete", t);
            return FAILURE;
        }
        return SUCCESS;
    }
    
    public String view() {
        try {
            Redirect redirect = this.getRedirectManager().getRedirect(this.getId());
            if (null == redirect) {
                this.addActionError(this.getText("error.redirect.null"));
                return INPUT;
            }
            this.populateForm(redirect);
        } catch (Throwable t) {
            _logger.error("error in view", t);
            return FAILURE;
        }
        return SUCCESS;
    }
    
    private void populateForm(Redirect redirect) throws Throwable {
        this.setId(redirect.getId());
        this.setPagecode(redirect.getPagecode());
        this.setGroupname(redirect.getGroupname());
        this.setRolename(redirect.getRolename());
    }
    
    private Redirect createRedirect() {
        Redirect redirect = new Redirect();
        redirect.setId(this.getId());
        redirect.setPagecode(this.getPagecode());
        redirect.setGroupname(this.getGroupname());
        redirect.setRolename(this.getRolename());
        return redirect;
    }
    
    public String getRedirectInfo(Integer id) {
        StringBuilder text = null;
        
        try {
            if (null != id) {
                Redirect redirect = this.getRedirectManager().getRedirect(id);
                
                text = new StringBuilder(this.getText("label.pagecode"));
                text.append(":");
                text.append(redirect.getPagecode());
                if (StringUtils.isNotBlank(redirect.getGroupname())) {
                    text.append(" ");
                    text.append(this.getText("label.groupname"));
                    text.append(":");
                    text.append(redirect.getGroupname());
                }
                if (StringUtils.isNotBlank(redirect.getRolename())) {
                    text.append(" ");
                    text.append(this.getText("label.rolename"));
                    text.append(":");
                    text.append(redirect.getRolename());
                }
            }
        } catch (Throwable t) {
            _logger.error("error creating redirect text");
        }
        return text.toString();
    }
    
    public Map<String, String> getGroups() {
        Map<String, String> groups = new HashMap<String, String>();
        
        try {
            List<Group> groupsList = getGroupManager().getGroups();
            groupsList.forEach(g -> groups.put(g.getName(), 
                    g.getDescription()));
        } catch (Throwable t) {
            _logger.error("error getting group names for the form", t);
        }
        return groups;
    }
    
    public Map<String, String> getRoles() {
        Map<String, String> roles = new HashMap<String, String>();
        
        try {
            List<Role> rolesList = getRoleManager().getRoles();
            rolesList.forEach(r -> roles.put(r.getName(), 
                    r.getDescription()));
        } catch (Throwable t) {
            _logger.error("error getting role names for the form", t);
        }
        return roles;
    }
    
    /**
     * get the pages list
     */
    public Map<String, String> getPages() {
        IPage root = this.getPageManager().getOnlineRoot();
        List<IPage> pages = new ArrayList<IPage>();
        this.addPublicPages(root, pages);
        Map<String, String> result = new LinkedHashMap<>();
        Lang lang = this.getCurrentLang();
        
        pages.forEach(
                p -> result.put(p.getCode(),
                        p.getShortFullTitle(lang.getCode())));
        return result;
    }
    
    protected void addPublicPages(IPage page, List<IPage> pages) {
        if (null == page) {
            return;
        }
        pages.add(page);
        String[] children = page.getChildrenCodes();
        for (int i = 0; i < children.length; i++) {
            IPage child = this.getPageManager().getOnlinePage(children[i]);
            this.addPublicPages(child, pages);
        }
    }
    
    public int getStrutsAction() {
        return _strutsAction;
    }
    public void setStrutsAction(int strutsAction) {
        this._strutsAction = strutsAction;
    }
    
    public int getId() {
        return _id;
    }
    public void setId(int id) {
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

    public IPageManager getPageManager() {
        return pageManager;
    }

    public void setPageManager(IPageManager pageManager) {
        this.pageManager = pageManager;
    }
    
    private int _strutsAction;
    private int _id;
    private String _pagecode;
    private String _groupname;
    private String _rolename;
    
    private IRedirectManager _redirectManager;
    private IGroupManager groupManager;
    private IRoleManager roleManager;
    private IPageManager pageManager;
}