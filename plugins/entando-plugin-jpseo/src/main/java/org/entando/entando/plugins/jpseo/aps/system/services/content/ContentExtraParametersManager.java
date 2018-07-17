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
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class ContentExtraParametersManager extends AbstractService implements IContentExtraParametersManager {

	private static final Logger logger =  LoggerFactory.getLogger(ContentExtraParametersManager.class);
    
	private IContentExtraParametersDAO contentExtraParametersDAO;

	@Override
	public void init() throws Exception {
		logger.debug("{} ready.", this.getClass().getName());
	}
    
    @AfterReturning(pointcut = "execution(* com.agiletec.plugins.jacms.aps.system.services.content.IContentManager.loadContent(..)) && args(id,onLine,..)", returning = "content")
    public void injectParameters(String id, Object onLine, Content content) {
        
    }
    
	@Override
	public ContentExtraParameters getContentExtraParameters(String contentId) throws ApsSystemException {
		ContentExtraParameters contentExtraParameters = null;
		try {
			contentExtraParameters = this.getContentExtraParametersDAO().loadContentExtraParameters(contentId);
		} catch (Throwable t) {
			logger.error("Error loading contentExtraParameters with id '{}'", contentId,  t);
			throw new ApsSystemException("Error loading contentExtraParameters with id: " + contentId, t);
		}
		return contentExtraParameters;
	}
    
	@Override
	public void addContentExtraParameters(ContentExtraParameters contentExtraParameters) throws ApsSystemException {
		try {
			this.getContentExtraParametersDAO().insertContentExtraParameters(contentExtraParameters);
		} catch (Throwable t) {
			logger.error("Error adding ContentExtraParameters", t);
			throw new ApsSystemException("Error adding ContentExtraParameters", t);
		}
	}
 
	@Override
	public void updateContentExtraParameters(ContentExtraParameters contentExtraParameters) throws ApsSystemException {
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
