/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpjaasauth.aps.system.services.mapping;

import java.util.ArrayList;
import java.util.List;

import org.entando.entando.plugins.jpjaasauth.aps.system.services.mapping.event.MappingChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;

public class MappingManager extends AbstractService implements IMappingManager {

	private static final Logger logger =  LoggerFactory.getLogger(MappingManager.class);

	@Override
	public void init() throws Exception {
		logger.debug("{} ready.", this.getClass().getName());
	}
 
	@Override
	public Mapping getMapping(int id) throws ApsSystemException {
		Mapping mapping = null;
		try {
			mapping = this.getMappingDAO().loadMapping(id);
		} catch (Throwable t) {
			logger.error("Error loading mapping with id '{}'", id,  t);
			throw new ApsSystemException("Error loading mapping with id: " + id, t);
		}
		return mapping;
	}

	@Override
	public List<Integer> getMappings() throws ApsSystemException {
		List<Integer> mappings = new ArrayList<Integer>();
		try {
			mappings = this.getMappingDAO().loadMappings();
		} catch (Throwable t) {
			logger.error("Error loading Mapping list",  t);
			throw new ApsSystemException("Error loading Mapping ", t);
		}
		return mappings;
	}

	@Override
	public List<Integer> searchMappings(FieldSearchFilter filters[]) throws ApsSystemException {
		List<Integer> mappings = new ArrayList<Integer>();
		try {
			mappings = this.getMappingDAO().searchMappings(filters);
		} catch (Throwable t) {
			logger.error("Error searching Mappings", t);
			throw new ApsSystemException("Error searching Mappings", t);
		}
		return mappings;
	}

	@Override
	public void addMapping(Mapping mapping) throws ApsSystemException {
		try {
			this.getMappingDAO().insertMapping(mapping);
			this.notifyMappingChangedEvent(mapping, MappingChangedEvent.INSERT_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error adding Mapping", t);
			throw new ApsSystemException("Error adding Mapping", t);
		}
	}
 
	@Override
	public void updateMapping(Mapping mapping) throws ApsSystemException {
		try {
			this.getMappingDAO().updateMapping(mapping);
			this.notifyMappingChangedEvent(mapping, MappingChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error updating Mapping", t);
			throw new ApsSystemException("Error updating Mapping " + mapping, t);
		}
	}

	@Override
	public void deleteMapping(int id) throws ApsSystemException {
		try {
			Mapping mapping = this.getMapping(id);
			this.getMappingDAO().removeMapping(id);
			this.notifyMappingChangedEvent(mapping, MappingChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error deleting Mapping with id {}", id, t);
			throw new ApsSystemException("Error deleting Mapping with id:" + id, t);
		}
	}

	@Override
	public List<Mapping> getMappingsByJaasRole(String jaasRole) throws ApsSystemException {
		try {
			return this.getMappingDAO().loadMappingByJAASRole(jaasRole);
		} catch (Throwable t) {
			logger.error("Error deleting Mapping with JAAS role {}", jaasRole, t);
			throw new ApsSystemException("Error deleting Mapping with JAAS role:" + jaasRole, t);
		}
	}

	private void notifyMappingChangedEvent(Mapping mapping, int operationCode) {
		MappingChangedEvent event = new MappingChangedEvent();
		event.setMapping(mapping);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}

    @SuppressWarnings("rawtypes")
    public SearcherDaoPaginatedResult<Mapping> getMappings(FieldSearchFilter[] filters) throws ApsSystemException {
        SearcherDaoPaginatedResult<Mapping> pagedResult = null;
        try {
            List<Mapping> mappings = new ArrayList<>();
            int count = this.getMappingDAO().countMappings(filters);

            List<Integer> mappingNames = this.getMappingDAO().searchMappings(filters);
            for (Integer mappingName : mappingNames) {
                mappings.add(this.getMapping(mappingName));
            }
            pagedResult = new SearcherDaoPaginatedResult<Mapping>(count, mappings);
        } catch (Throwable t) {
            logger.error("Error searching mappings", t);
            throw new ApsSystemException("Error searching mappings", t);
        }
        return pagedResult;
    }

    @Override
    public SearcherDaoPaginatedResult<Mapping> getMappings(List<FieldSearchFilter> filters) throws ApsSystemException {
        FieldSearchFilter[] array = null;
        if (null != filters) {
            array = filters.toArray(new FieldSearchFilter[filters.size()]);
        }
        return this.getMappings(array);
    }

	public void setMappingDAO(IMappingDAO mappingDAO) {
		 this._mappingDAO = mappingDAO;
	}
	protected IMappingDAO getMappingDAO() {
		return _mappingDAO;
	}

	private IMappingDAO _mappingDAO;
}
