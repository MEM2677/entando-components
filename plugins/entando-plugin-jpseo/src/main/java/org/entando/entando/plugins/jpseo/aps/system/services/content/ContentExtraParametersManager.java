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

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.entando.entando.plugins.jpseo.aps.system.JpseoSystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class ContentExtraParametersManager extends AbstractService implements IContentExtraParametersManager {

    private static final Logger logger = LoggerFactory.getLogger(ContentExtraParametersManager.class);

    private IContentExtraParametersDAO contentExtraParametersDAO;

    @Override
    public void init() throws Exception {
        logger.debug("{} ready.", this.getClass().getName());
    }

    @AfterReturning(pointcut = "execution(* com.agiletec.plugins.jacms.aps.system.services.content.IContentManager.loadContent(..)) && args(id,onLine,..)", returning = "content")
    public void injectParameters(String id, boolean onLine, Content content) {
        try {
            if (null == content) {
                return;
            }
            ContentExtraParametersVO extraParameters = this.getContentExtraParameters(id);
            if (null == extraParameters) {
                return;
            }
            String xmlConfig = (onLine) ? extraParameters.getOnlinexml() : extraParameters.getWorkxml();
            if (StringUtils.isBlank(xmlConfig)) {
                return;
            }
            SeoContentExtraConfigDOM dom = new SeoContentExtraConfigDOM();
            ContentMetadata contentMetadata = dom.addExtraConfig(xmlConfig);
            if (null == content.getExtraParams()) {
                content.setExtraParams(new HashMap<>());
            }
            content.getExtraParams().put(JpseoSystemConstants.CONTENT_METADATA_KEY, contentMetadata);
        } catch (Exception e) {
            logger.error("Error extracting seo parameters for content {}", id, e);
        }
    }

    @After("execution(* com.agiletec.plugins.jacms.aps.system.services.content.IContentManager.saveContent(..)) && args(content)")
    public void saveContent(Content content) {
        if (null == content || null == content.getExtraParams() || null == content.getExtraParams().get(JpseoSystemConstants.CONTENT_METADATA_KEY)) {
            return;
        }
        String xml = this.extractXmlConfiguration(content);
        ContentExtraParametersVO extraParameters = this.getContentExtraParametersDAO().loadContentExtraParameters(content.getId());
        if (null == extraParameters) {
            extraParameters = new ContentExtraParametersVO();
            extraParameters.setContentid(content.getId());
            extraParameters.setWorkxml(xml);
            this.getContentExtraParametersDAO().insertContentExtraParameters(extraParameters);
        } else {
            extraParameters.setWorkxml(xml);
            this.getContentExtraParametersDAO().updateContentExtraParameters(extraParameters);
        }
    }

    @After("execution(* com.agiletec.plugins.jacms.aps.system.services.content.IContentManager.addContent(..)) && args(content)")
    public void addContent(Content content) {
        this.saveContent(content);
    }

    @After("execution(* com.agiletec.plugins.jacms.aps.system.services.content.IContentManager.insertOnLineContent(..)) && args(content)")
    public void insertOnLineContent(Content content) {
        if (null == content) {
            return;
        }
        String xml = this.extractXmlConfiguration(content);
        if (null == xml) {
            this.getContentExtraParametersDAO().removeContentExtraParameters(content.getId());
            return;
        }
        ContentExtraParametersVO extraParameters = this.getContentExtraParametersDAO().loadContentExtraParameters(content.getId());
        boolean hasToBeInsert = false;
        if (null == extraParameters) {
            extraParameters = new ContentExtraParametersVO();
            extraParameters.setContentid(content.getId());
            hasToBeInsert = true;
        }
        extraParameters.setWorkxml(xml);
        extraParameters.setOnlinexml(xml);
        if (hasToBeInsert) {
            this.getContentExtraParametersDAO().insertContentExtraParameters(extraParameters);
        } else {
            this.getContentExtraParametersDAO().updateContentExtraParameters(extraParameters);
        }
    }

    private String extractXmlConfiguration(Content content) {
        if (null == content || null == content.getExtraParams() || null == content.getExtraParams().get(JpseoSystemConstants.CONTENT_METADATA_KEY)) {
            return null;
        }
        ContentMetadata contentMetadata = (ContentMetadata) content.getExtraParams().get(JpseoSystemConstants.CONTENT_METADATA_KEY);
        SeoContentExtraConfigDOM dom = new SeoContentExtraConfigDOM();
        return dom.extractXml(contentMetadata);
    }

    @After("execution(* com.agiletec.plugins.jacms.aps.system.services.content.IContentManager.removeOnLineContent(..)) && args(content)")
    public void removeOnLineContent(Content content) {
        if (null == content) {
            return;
        }
        ContentExtraParametersVO extraParameters = this.getContentExtraParametersDAO().loadContentExtraParameters(content.getId());
        if (null == extraParameters) {
            return;
        }
        extraParameters.setOnlinexml(null);
        this.getContentExtraParametersDAO().updateContentExtraParameters(extraParameters);
    }

    @Before("execution(* com.agiletec.plugins.jacms.aps.system.services.content.IContentManager.deleteContent(..)) && args(content)")
    public void deleteContent(Content content) {
        if (null == content) {
            return;
        }
        this.getContentExtraParametersDAO().removeContentExtraParameters(content.getId());
    }

    @Override
    public ContentExtraParametersVO getContentExtraParameters(String contentId) throws ApsSystemException {
        ContentExtraParametersVO contentExtraParameters = null;
        try {
            contentExtraParameters = this.getContentExtraParametersDAO().loadContentExtraParameters(contentId);
        } catch (Throwable t) {
            logger.error("Error loading contentExtraParameters with id '{}'", contentId, t);
            throw new ApsSystemException("Error loading contentExtraParameters with id: " + contentId, t);
        }
        return contentExtraParameters;
    }

    @Override
    public void addContentExtraParameters(ContentExtraParametersVO contentExtraParameters) throws ApsSystemException {
        try {
            this.getContentExtraParametersDAO().insertContentExtraParameters(contentExtraParameters);
        } catch (Throwable t) {
            logger.error("Error adding ContentExtraParameters", t);
            throw new ApsSystemException("Error adding ContentExtraParameters", t);
        }
    }

    @Override
    public void updateContentExtraParameters(ContentExtraParametersVO contentExtraParameters) throws ApsSystemException {
        try {
            this.getContentExtraParametersDAO().updateContentExtraParameters(contentExtraParameters);
        } catch (Throwable t) {
            logger.error("Error updating ContentExtraParameters", t);
            throw new ApsSystemException("Error updating ContentExtraParameters " + contentExtraParameters, t);
        }
    }

    @Override
    public void deleteContentExtraParameters(String contentId) throws ApsSystemException {
        try {
            this.getContentExtraParametersDAO().removeContentExtraParameters(contentId);
        } catch (Throwable t) {
            logger.error("Error deleting ContentExtraParameters with id {}", contentId, t);
            throw new ApsSystemException("Error deleting ContentExtraParameters with id:" + contentId, t);
        }
    }

    public void setContentExtraParametersDAO(IContentExtraParametersDAO contentExtraParametersDAO) {
        this.contentExtraParametersDAO = contentExtraParametersDAO;
    }

    protected IContentExtraParametersDAO getContentExtraParametersDAO() {
        return contentExtraParametersDAO;
    }

}
