/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpjaasauth.apsadmin.mapping;

import java.util.List;
import org.apache.commons.lang.StringUtils;

import com.agiletec.aps.system.common.FieldSearchFilter;
import org.entando.entando.plugins.jpjaasauth.aps.system.services.mapping.Mapping;
import org.entando.entando.plugins.jpjaasauth.aps.system.services.mapping.IMappingManager;
import com.agiletec.apsadmin.system.BaseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MappingFinderAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(MappingFinderAction.class);

	public List<Integer> getMappingsId() {
		try {
			FieldSearchFilter[] filters = new FieldSearchFilter[0];
			if (null != this.getId()) {
				//TODO add a constant into your IMappingManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("id"), this.getId(), false);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getJrole())) {
				//TODO add a constant into your IMappingManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("jrole"), this.getJrole(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getEgroup())) {
				//TODO add a constant into your IMappingManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("egroup"), this.getEgroup(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getErole())) {
				//TODO add a constant into your IMappingManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("erole"), this.getErole(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			List<Integer> mappings = this.getMappingManager().searchMappings(filters);
			return mappings;
		} catch (Throwable t) {
			_logger.error("Error getting mappings list", t);
			throw new RuntimeException("Error getting mappings list", t);
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

	public Mapping getMapping(int id) {
		Mapping mapping = null;
		try {
			mapping = this.getMappingManager().getMapping(id);
		} catch (Throwable t) {
			_logger.error("Error getting mapping with id {}", id, t);
			throw new RuntimeException("Error getting mapping with id " + id, t);
		}
		return mapping;
	}


	public Integer getId() {
		return _id;
	}
	public void setId(Integer id) {
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
	
	private Integer _id;
	private String _jrole;
	private String _egroup;
	private String _erole;
	private IMappingManager _mappingManager;
}