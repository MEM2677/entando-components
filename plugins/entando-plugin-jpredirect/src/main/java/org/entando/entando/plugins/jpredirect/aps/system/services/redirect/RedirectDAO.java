/*
*
* <Your licensing text here>
*
*/
package org.entando.entando.plugins.jpredirect.aps.system.services.redirect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedirectDAO extends AbstractSearcherDAO implements IRedirectDAO {
    
    private static final Logger logger =  LoggerFactory.getLogger(RedirectDAO.class);
    
    @Override
    public int countRedirects(FieldSearchFilter[] filters) {
        Integer redirects = null;
        try {
            redirects = super.countId(filters);
        } catch (Throwable t) {
            logger.error("error in count redirects", t);
            throw new RuntimeException("error in count redirects", t);
        }
        return redirects;
    }
    
    @Override
    protected String getTableFieldName(String metadataFieldKey) {
        return metadataFieldKey;
    }
    
    @Override
    protected String getMasterTableName() {
        return "jpredirect_redirect";
    }
    
    @Override
    protected String getMasterTableIdFieldName() {
        return "id";
    }
    
    @Override
    public List<Integer> searchRedirects(FieldSearchFilter[] filters) {
        List<Integer> ids = new ArrayList<Integer>();
        List redirectsId = null;
        try {
            redirectsId  = super.searchId(filters);
            redirectsId.forEach(i -> ids.add(Integer.valueOf((String)i)));
        } catch (Throwable t) {
            logger.error("error in searchRedirects",  t);
            throw new RuntimeException("error in searchRedirects", t);
        }
        return ids;
    }
    
    @Override
    public List<Integer> loadRedirects() {
        List<Integer> redirectsId = new ArrayList<Integer>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        try {
            conn = this.getConnection();
            stat = conn.prepareStatement(LOAD_REDIRECTS_ID);
            res = stat.executeQuery();
            while (res.next()) {
                int id = res.getInt("id");
                redirectsId.add(id);
            }
        } catch (Throwable t) {
            logger.error("Error loading Redirect list",  t);
            throw new RuntimeException("Error loading Redirect list", t);
        } finally {
            closeDaoResources(res, stat, conn);
        }
        return redirectsId;
    }
    
    @Override
    public void insertRedirect(Redirect redirect) {
        PreparedStatement stat = null;
        Connection conn  = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            int id = getUniqueId(conn);
            redirect.setId(id);
            this.insertRedirect(redirect, conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            logger.error("Error on insert redirect",  t);
            throw new RuntimeException("Error on insert redirect", t);
        } finally {
            this.closeDaoResources(null, stat, conn);
        }
    }
    
    public void insertRedirect(Redirect redirect, Connection conn) {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(ADD_REDIRECT);
            int index = 1;
            stat.setInt(index++, redirect.getId());
            stat.setString(index++, redirect.getPagecode());
            if (StringUtils.isNotBlank(redirect.getGroupname())) {
                stat.setString(index++, redirect.getGroupname());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }
            if(StringUtils.isNotBlank(redirect.getRolename())) {
                stat.setString(index++, redirect.getRolename());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }
            stat.executeUpdate();
        } catch (Throwable t) {
            logger.error("Error on insert redirect",  t);
            throw new RuntimeException("Error on insert redirect", t);
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
    public void updateRedirect(Redirect redirect) {
        PreparedStatement stat = null;
        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            this.updateRedirect(redirect, conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            logger.error("Error updating redirect {}", redirect.getId(),  t);
            throw new RuntimeException("Error updating redirect", t);
        } finally {
            this.closeDaoResources(null, stat, conn);
        }
    }
    
    public void updateRedirect(Redirect redirect, Connection conn) {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(UPDATE_REDIRECT);
            int index = 1;
            
            stat.setString(index++, redirect.getPagecode());
            if(StringUtils.isNotBlank(redirect.getGroupname())) {
                stat.setString(index++, redirect.getGroupname());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }
            if(StringUtils.isNotBlank(redirect.getRolename())) {
                stat.setString(index++, redirect.getRolename());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }
            stat.setInt(index++, redirect.getId());
            stat.executeUpdate();
        } catch (Throwable t) {
            logger.error("Error updating redirect {}", redirect.getId(),  t);
            throw new RuntimeException("Error updating redirect", t);
        } finally {
            this.closeDaoResources(null, stat, null);
        }
    }
    
    @Override
    public void removeRedirect(int id) {
        PreparedStatement stat = null;
        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            this.removeRedirect(id, conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            logger.error("Error deleting redirect {}", id, t);
            throw new RuntimeException("Error deleting redirect", t);
        } finally {
            this.closeDaoResources(null, stat, conn);
        }
    }
    
    public void removeRedirect(int id, Connection conn) {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(DELETE_REDIRECT);
            int index = 1;
            stat.setInt(index++, id);
            stat.executeUpdate();
        } catch (Throwable t) {
            logger.error("Error deleting redirect {}", id, t);
            throw new RuntimeException("Error deleting redirect", t);
        } finally {
            this.closeDaoResources(null, stat, null);
        }
    }
    
    public Redirect loadRedirect(int id) {
        Redirect redirect = null;
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        try {
            conn = this.getConnection();
            redirect = this.loadRedirect(id, conn);
        } catch (Throwable t) {
            logger.error("Error loading redirect with id {}", id, t);
            throw new RuntimeException("Error loading redirect with id " + id, t);
        } finally {
            closeDaoResources(res, stat, conn);
        }
        return redirect;
    }
    
    public Redirect loadRedirect(int id, Connection conn) {
        Redirect redirect = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        try {
            stat = conn.prepareStatement(LOAD_REDIRECT);
            int index = 1;
            stat.setInt(index++, id);
            res = stat.executeQuery();
            if (res.next()) {
                redirect = this.buildRedirectFromRes(res);
            }
        } catch (Throwable t) {
            logger.error("Error loading redirect with id {}", id, t);
            throw new RuntimeException("Error loading redirect with id " + id, t);
        } finally {
            closeDaoResources(res, stat, null);
        }
        return redirect;
    }
    
    @Override
    public List<Redirect> loadRedirectObjects() {
        List<Redirect> redirects = new ArrayList<>();
        PreparedStatement stat = null;
        ResultSet res = null;
        Connection conn = null;
        
        try {
            conn = this.getConnection();
            stat = conn.prepareStatement(LOAD_REDIRECTS);
            res = stat.executeQuery();
            while (res.next()) {
                Redirect redirect = this.buildRedirectFromRes(res);
                redirects.add(redirect);
            }
        } catch (Throwable t) {
            logger.error("Error loading list of redirects", t);
            throw new RuntimeException("Error loading the list of redirects", t);
        } finally {
            closeDaoResources(res, stat, null);
        }
        return redirects;
    }
    
    protected Redirect buildRedirectFromRes(ResultSet res) {
        Redirect redirect = null;
        try {
            redirect = new Redirect();
            redirect.setId(res.getInt("id"));
            redirect.setPagecode(res.getString("pagecode"));
            redirect.setGroupname(res.getString("groupname"));
            redirect.setRolename(res.getString("rolename"));
        } catch (Throwable t) {
            logger.error("Error in buildRedirectFromRes", t);
        }
        return redirect;
    }
    
    private static final String ADD_REDIRECT = "INSERT INTO jpredirect_redirect (id, pagecode, groupname, rolename ) VALUES (?, ?, ?, ? )";
    
    private static final String UPDATE_REDIRECT = "UPDATE jpredirect_redirect SET  pagecode=?,  groupname=?, rolename=? WHERE id = ?";
    
    private static final String DELETE_REDIRECT = "DELETE FROM jpredirect_redirect WHERE id = ?";
    
    private static final String LOAD_REDIRECT = "SELECT id, pagecode, groupname, rolename  FROM jpredirect_redirect WHERE id = ?";
    
    private static final String LOAD_REDIRECTS_ID  = "SELECT id FROM jpredirect_redirect";
    
    private static final String MAX_ID = "SELECT MAX(id) FROM jpredirect_redirect";
    
    private static final String LOAD_REDIRECTS = "SELECT id, pagecode, groupname, rolename  FROM jpredirect_redirect ORDER BY id";
}