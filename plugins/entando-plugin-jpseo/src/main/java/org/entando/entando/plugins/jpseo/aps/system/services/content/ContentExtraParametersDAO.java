/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.entando.entando.plugins.jpseo.aps.system.services.content;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import org.apache.commons.lang.StringUtils;
import com.agiletec.aps.system.common.AbstractSearcherDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentExtraParametersDAO extends AbstractSearcherDAO implements IContentExtraParametersDAO {

    private static final Logger logger = LoggerFactory.getLogger(ContentExtraParametersDAO.class);

    @Override
    protected String getTableFieldName(String metadataFieldKey) {
        return metadataFieldKey;
    }

    @Override
    protected String getMasterTableName() {
        return "jpseo_contentextraparams";
    }

    @Override
    protected String getMasterTableIdFieldName() {
        return "contentid";
    }

    @Override
    public void insertContentExtraParameters(ContentExtraParametersVO contentExtraParameters) {
        PreparedStatement stat = null;
        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            this.insertContentExtraParameters(contentExtraParameters, conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            logger.error("Error on insert contentExtraParameters", t);
            throw new RuntimeException("Error on insert contentExtraParameters", t);
        } finally {
            this.closeDaoResources(null, stat, conn);
        }
    }

    public void insertContentExtraParameters(ContentExtraParametersVO contentExtraParameters, Connection conn) {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(ADD_CONTENTEXTRAPARAMETERS);
            int index = 1;
            stat.setString(index++, contentExtraParameters.getContentid());
            if (StringUtils.isNotBlank(contentExtraParameters.getWorkxml())) {
                stat.setString(index++, contentExtraParameters.getWorkxml());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }
            if (StringUtils.isNotBlank(contentExtraParameters.getOnlinexml())) {
                stat.setString(index++, contentExtraParameters.getOnlinexml());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }
            stat.executeUpdate();
        } catch (Throwable t) {
            logger.error("Error on insert contentExtraParameters", t);
            throw new RuntimeException("Error on insert contentExtraParameters", t);
        } finally {
            this.closeDaoResources(null, stat, null);
        }
    }

    @Override
    public void updateContentExtraParameters(ContentExtraParametersVO contentExtraParameters) {
        PreparedStatement stat = null;
        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            this.updateContentExtraParameters(contentExtraParameters, conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            logger.error("Error updating contentExtraParameters {}", contentExtraParameters.getContentid(), t);
            throw new RuntimeException("Error updating contentExtraParameters", t);
        } finally {
            this.closeDaoResources(null, stat, conn);
        }
    }

    public void updateContentExtraParameters(ContentExtraParametersVO contentExtraParameters, Connection conn) {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(UPDATE_CONTENTEXTRAPARAMETERS);
            int index = 1;
            if (StringUtils.isNotBlank(contentExtraParameters.getWorkxml())) {
                stat.setString(index++, contentExtraParameters.getWorkxml());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }
            if (StringUtils.isNotBlank(contentExtraParameters.getOnlinexml())) {
                stat.setString(index++, contentExtraParameters.getOnlinexml());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }
            stat.setString(index++, contentExtraParameters.getContentid());
            stat.executeUpdate();
        } catch (Throwable t) {
            logger.error("Error updating contentExtraParameters {}", contentExtraParameters.getContentid(), t);
            throw new RuntimeException("Error updating contentExtraParameters", t);
        } finally {
            this.closeDaoResources(null, stat, null);
        }
    }

    @Override
    public void removeContentExtraParameters(String contentId) {
        PreparedStatement stat = null;
        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            this.removeContentExtraParameters(contentId, conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            logger.error("Error deleting contentExtraParameters {}", contentId, t);
            throw new RuntimeException("Error deleting contentExtraParameters", t);
        } finally {
            this.closeDaoResources(null, stat, conn);
        }
    }

    public void removeContentExtraParameters(String contentId, Connection conn) {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(DELETE_CONTENTEXTRAPARAMETERS);
            stat.setString(1, contentId);
            stat.executeUpdate();
        } catch (Throwable t) {
            logger.error("Error deleting contentExtraParameters {}", contentId, t);
            throw new RuntimeException("Error deleting contentExtraParameters", t);
        } finally {
            this.closeDaoResources(null, stat, null);
        }
    }

    @Override
    public ContentExtraParametersVO loadContentExtraParameters(String contentId) {
        ContentExtraParametersVO contentExtraParameters = null;
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        try {
            conn = this.getConnection();
            contentExtraParameters = this.loadContentExtraParameters(contentId, conn);
        } catch (Throwable t) {
            logger.error("Error loading contentExtraParameters with id {}", contentId, t);
            throw new RuntimeException("Error loading contentExtraParameters with id " + contentId, t);
        } finally {
            closeDaoResources(res, stat, conn);
        }
        return contentExtraParameters;
    }

    public ContentExtraParametersVO loadContentExtraParameters(String contentId, Connection conn) {
        ContentExtraParametersVO contentExtraParameters = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        try {
            stat = conn.prepareStatement(LOAD_CONTENTEXTRAPARAMETERS);
            stat.setString(1, contentId);
            res = stat.executeQuery();
            if (res.next()) {
                contentExtraParameters = this.buildContentExtraParametersFromRes(res);
            }
        } catch (Throwable t) {
            logger.error("Error loading contentExtraParameters with id {}", contentId, t);
            throw new RuntimeException("Error loading contentExtraParameters with id " + contentId, t);
        } finally {
            closeDaoResources(res, stat, null);
        }
        return contentExtraParameters;
    }

    protected ContentExtraParametersVO buildContentExtraParametersFromRes(ResultSet res) {
        ContentExtraParametersVO contentExtraParameters = null;
        try {
            contentExtraParameters = new ContentExtraParametersVO();
            contentExtraParameters.setContentid(res.getString("contentid"));
            contentExtraParameters.setWorkxml(res.getString("workxml"));
            contentExtraParameters.setOnlinexml(res.getString("onlinexml"));
        } catch (Throwable t) {
            logger.error("Error in buildContentExtraParametersFromRes", t);
        }
        return contentExtraParameters;
    }

    private static final String ADD_CONTENTEXTRAPARAMETERS = "INSERT INTO jpseo_contentextraparams (contentid, workxml, onlinexml) VALUES ( ? , ? , ? )";

    private static final String UPDATE_CONTENTEXTRAPARAMETERS = "UPDATE jpseo_contentextraparams SET workxml = ? , onlinexml = ? WHERE contentid = ?";

    private static final String DELETE_CONTENTEXTRAPARAMETERS = "DELETE FROM jpseo_contentextraparams WHERE contentid = ?";

    private static final String LOAD_CONTENTEXTRAPARAMETERS = "SELECT contentid, workxml, onlinexml FROM jpseo_contentextraparams WHERE contentid = ?";

}
