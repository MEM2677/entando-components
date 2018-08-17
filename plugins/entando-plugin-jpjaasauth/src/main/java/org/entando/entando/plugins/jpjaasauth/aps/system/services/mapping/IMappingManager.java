/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpjaasauth.aps.system.services.mapping;

import java.util.List;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface IMappingManager {

	public Mapping getMapping(int id) throws ApsSystemException;

	public List<Integer> getMappings() throws ApsSystemException;

	public List<Integer> searchMappings(FieldSearchFilter filters[]) throws ApsSystemException;

	public void addMapping(Mapping mapping) throws ApsSystemException;

	public void updateMapping(Mapping mapping) throws ApsSystemException;

	public void deleteMapping(int id) throws ApsSystemException;

	public SearcherDaoPaginatedResult<Mapping> getMappings(List<FieldSearchFilter> fieldSearchFilters) throws ApsSystemException;

	public List<Mapping> getMappingsByJaasRole(String jaasRole) throws ApsSystemException;
}