/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpjaasauth.aps.system.services.mapping;

import java.util.List;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface IMappingDAO {

	public List<Integer> searchMappings(FieldSearchFilter[] filters);
	
	public Mapping loadMapping(int id);

	public List<Integer> loadMappings();

	public void removeMapping(int id);
	
	public void updateMapping(Mapping mapping);

	public void insertMapping(Mapping mapping);

    public int countMappings(FieldSearchFilter[] filters);
    
    public List<Mapping> loadMappingByJAASRole(String jaasRole);
}