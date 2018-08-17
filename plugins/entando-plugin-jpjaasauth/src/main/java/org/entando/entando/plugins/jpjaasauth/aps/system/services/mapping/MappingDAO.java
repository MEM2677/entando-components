/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpjaasauth.aps.system.services.mapping;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MappingDAO extends AbstractSearcherDAO implements IMappingDAO {

	private static final Logger logger =  LoggerFactory.getLogger(MappingDAO.class);

    @Override
    public int countMappings(FieldSearchFilter[] filters) {
        Integer mappings = null;
        try {
            mappings = super.countId(filters);
        } catch (Throwable t) {
            logger.error("error in count mappings", t);
            throw new RuntimeException("error in count mappings", t);
        }
        return mappings;
    }

	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		return metadataFieldKey;
	}
	
	@Override
	protected String getMasterTableName() {
		return "jpjaasauth_mapping";
	}
	
	@Override
	protected String getMasterTableIdFieldName() {
		return "id";
	}

	@Override
	public List<Integer> searchMappings(FieldSearchFilter[] filters) {
		List mappingsId = null;
		try {
			mappingsId  = super.searchId(filters);
		} catch (Throwable t) {
			logger.error("error in searchMappings",  t);
			throw new RuntimeException("error in searchMappings", t);
		}
		return mappingsId;
	}

	@Override
	public List<Integer> loadMappings() {
		List<Integer> mappingsId = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_MAPPINGS_ID);
			res = stat.executeQuery();
			while (res.next()) {
				int id = res.getInt("id");
				mappingsId.add(id);
			}
		} catch (Throwable t) {
			logger.error("Error loading Mapping list",  t);
			throw new RuntimeException("Error loading Mapping list", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return mappingsId;
	}
	
	@Override
	public void insertMapping(Mapping mapping) {
		PreparedStatement stat = null;
		Connection conn  = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			int id = getUniqueId(conn);
			mapping.setId(id);
			this.insertMapping(mapping, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error on insert mapping",  t);
			throw new RuntimeException("Error on insert mapping", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void insertMapping(Mapping mapping, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_MAPPING);
			int index = 1;
			stat.setInt(index++, mapping.getId());
 			stat.setString(index++, mapping.getJrole());
 			if(StringUtils.isNotBlank(mapping.getEgroup())) {
				stat.setString(index++, mapping.getEgroup());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
 			if(StringUtils.isNotBlank(mapping.getErole())) {
				stat.setString(index++, mapping.getErole());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error on insert mapping",  t);
			throw new RuntimeException("Error on insert mapping", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}
	
	/**
	 * This inspect the given table and return the id to be used as primary key
	 * for further operations
	 *
	 * @param query
	 * the query used to inspect the datasource
	 * @param conn
	 * the connection to the datasource
	 * @return The first free id to use as primary key
	 */
	protected int getUniqueId(Connection conn) {
		int id = 0;
		Statement stat = null;
		ResultSet res = null;
		try {
			stat = conn.createStatement();
			res = stat.executeQuery(MAX_ID);
			res.next();
			id = res.getInt(1) + 1;
		} catch (Throwable t) {
			logger.error("Error while getting last used ID", t);
			throw new RuntimeException("Error while getting last used ID", t);
		} finally {
			closeDaoResources(res, stat);
		}
		return id;
	}

	@Override
	public void updateMapping(Mapping mapping) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.updateMapping(mapping, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error updating mapping {}", mapping.getId(),  t);
			throw new RuntimeException("Error updating mapping", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void updateMapping(Mapping mapping, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(UPDATE_MAPPING);
			int index = 1;

 			stat.setString(index++, mapping.getJrole());
 			if(StringUtils.isNotBlank(mapping.getEgroup())) {
				stat.setString(index++, mapping.getEgroup());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
 			if(StringUtils.isNotBlank(mapping.getErole())) {
				stat.setString(index++, mapping.getErole());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
			stat.setInt(index++, mapping.getId());
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error updating mapping {}", mapping.getId(),  t);
			throw new RuntimeException("Error updating mapping", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void removeMapping(int id) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.removeMapping(id, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			logger.error("Error deleting mapping {}", id, t);
			throw new RuntimeException("Error deleting mapping", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	public void removeMapping(int id, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_MAPPING);
			int index = 1;
			stat.setInt(index++, id);
			stat.executeUpdate();
		} catch (Throwable t) {
			logger.error("Error deleting mapping {}", id, t);
			throw new RuntimeException("Error deleting mapping", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	public Mapping loadMapping(int id) {
		Mapping mapping = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			mapping = this.loadMapping(id, conn);
		} catch (Throwable t) {
			logger.error("Error loading mapping with id {}", id, t);
			throw new RuntimeException("Error loading mapping with id " + id, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return mapping;
	}

	public Mapping loadMapping(int id, Connection conn) {
		Mapping mapping = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_MAPPING_BY_ID);
			int index = 1;
			stat.setInt(index++, id);
			res = stat.executeQuery();
			if (res.next()) {
				mapping = this.buildMappingFromRes(res);
			}
		} catch (Throwable t) {
			logger.error("Error loading mapping with id {}", id, t);
			throw new RuntimeException("Error loading mapping with id " + id, t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return mapping;
	}
	
	public List<Mapping> loadMappingByJAASRole(String jaasRole) {
		List<Mapping> mappings = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			mappings = this.loadMappingByJAASRole(jaasRole, conn);
		} catch (Throwable t) {
			logger.error("Error loading mapping with role {}", jaasRole, t);
			throw new RuntimeException("Error loading mapping with role " + jaasRole, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return mappings;
	}

	public List<Mapping> loadMappingByJAASRole(String jaasRole, Connection conn) {
		List<Mapping> mappings = new ArrayList<>();
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_MAPPING_BY_ROLE);
			int index = 1;
			stat.setString(index++, jaasRole);
			res = stat.executeQuery();
			while (res.next()) {
				Mapping mapping = this.buildMappingFromRes(res);
				
				mappings.add(mapping);
			}
		} catch (Throwable t) {
			logger.error("Error loading mapping with role {}", jaasRole, t);
			throw new RuntimeException("Error loading mapping with role " + jaasRole, t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return mappings;
	}

	protected Mapping buildMappingFromRes(ResultSet res) {
		Mapping mapping = null;
		try {
			mapping = new Mapping();				
			mapping.setId(res.getInt("id"));
			mapping.setJrole(res.getString("jrole"));
			mapping.setEgroup(res.getString("egroup"));
			mapping.setErole(res.getString("erole"));
		} catch (Throwable t) {
			logger.error("Error in buildMappingFromRes", t);
		}
		return mapping;
	}

	private static final String ADD_MAPPING = "INSERT INTO jpjaasauth_mapping (id, jrole, egroup, erole ) VALUES (?, ?, ?, ? )";

	private static final String UPDATE_MAPPING = "UPDATE jpjaasauth_mapping SET  jrole=?,  egroup=?, erole=? WHERE id = ?";

	private static final String DELETE_MAPPING = "DELETE FROM jpjaasauth_mapping WHERE id = ?";
	
	private static final String LOAD_MAPPING_BY_ID = "SELECT id, jrole, egroup, erole  FROM jpjaasauth_mapping WHERE id = ?";
	
	private static final String LOAD_MAPPING_BY_ROLE = "SELECT id, jrole, egroup, erole FROM jpjaasauth_mapping WHERE jrole = ? ORDER BY id";
	
	private static final String LOAD_MAPPINGS_ID  = "SELECT id FROM jpjaasauth_mapping";
	
	private static final String MAX_ID = "SELECT MAX(id) FROM jpjaasauth_mapping";
	
}