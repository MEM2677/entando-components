/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpjaasauth.apsadmin.mapping;

import org.entando.entando.plugins.jpjaasauth.aps.system.services.mapping.Mapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpjaasauth.aps.system.services.mapping.IMappingManager;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MappingAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(MappingAction.class);

	public String newMapping() {
		try {
			this.setStrutsAction(ApsAdminSystemConstants.ADD);
		} catch (Throwable t) {
			_logger.error("error in newMapping", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String edit() {
		try {
			Mapping mapping = this.getMappingManager().getMapping(this.getId());
			if (null == mapping) {
				this.addActionError(this.getText("error.mapping.null"));
				return INPUT;
			}
			this.populateForm(mapping);
			this.setStrutsAction(ApsAdminSystemConstants.EDIT);
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String save() {
		try {
			Mapping mapping = this.createMapping();
			int strutsAction = this.getStrutsAction();
			if (StringUtils.isBlank(_egroup)
					&& StringUtils.isBlank(_erole)) {
				this.addActionError(this.getText("jaasauth.message.noauth"));
				return INPUT;
			}
			if (ApsAdminSystemConstants.ADD == strutsAction) {
				this.getMappingManager().addMapping(mapping);
			} else if (ApsAdminSystemConstants.EDIT == strutsAction) {
				this.getMappingManager().updateMapping(mapping);
			}
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String trash() {
		try {
			Mapping mapping = this.getMappingManager().getMapping(this.getId());
			if (null == mapping) {
				this.addActionError(this.getText("error.mapping.null"));
				return INPUT;
			}
			this.populateForm(mapping);
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
				this.getMappingManager().deleteMapping(this.getId());
			}
		} catch (Throwable t) {
			_logger.error("error in delete", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String view() {
		try {
			Mapping mapping = this.getMappingManager().getMapping(this.getId());
			if (null == mapping) {
				this.addActionError(this.getText("error.mapping.null"));
				return INPUT;
			}
			this.populateForm(mapping);
		} catch (Throwable t) {
			_logger.error("error in view", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	private void populateForm(Mapping mapping) throws Throwable {
		this.setId(mapping.getId());
		this.setJrole(mapping.getJrole());
		this.setEgroup(mapping.getEgroup());
		this.setErole(mapping.getErole());
	}

	private Mapping createMapping() {
		Mapping mapping = new Mapping();
		mapping.setId(this.getId());
		mapping.setJrole(this.getJrole());
		mapping.setEgroup(this.getEgroup());
		mapping.setErole(this.getErole());
		return mapping;
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

	public String getJrole() {
		return _jrole;
	}
	public void setJrole(String jrole) {
		this._jrole = jrole;
	}

	public String getEgroup() {
		return _egroup;
	}
	public void setEgroup(String egroup) {
		this._egroup = egroup;
	}

	public String getErole() {
		return _erole;
	}
	public void setErole(String erole) {
		this._erole = erole;
	}

	protected IMappingManager getMappingManager() {
		return _mappingManager;
	}
	public void setMappingManager(IMappingManager mappingManager) {
		this._mappingManager = mappingManager;
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


	private int _strutsAction;
	private int _id;
	private String _jrole;
	private String _egroup;
	private String _erole;

	private IMappingManager _mappingManager;
	private IGroupManager groupManager;
	private IRoleManager roleManager;

}