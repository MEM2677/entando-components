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
package org.entando.entando.plugins.jpseo.apsadmin.content;

import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.plugins.jacms.apsadmin.content.helper.IContentActionHelper;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;

@Aspect
public class ContentActionAspect {

    private static final Logger logger = LoggerFactory.getLogger(ContentActionAspect.class);

    private IContentActionHelper contentActionHelper;

    protected Content updateContentOnSession(boolean updateMainGroup) {
        HttpServletRequest request = ServletActionContext.getRequest();
        Content content = this.getContent(request);
        this.getContentActionHelper().updateContent(content, updateMainGroup, request);
        return content;
    }

    public Content getContent(HttpServletRequest request) {
        String contentOnSessionMarker = (String) request.getAttribute("contentOnSessionMarker");
        return (Content) request.getSession().getAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + contentOnSessionMarker);
    }

    @Before("execution(* com.agiletec.plugins.jacms.apsadmin.content.ContentAction.validate())")
    public void validate(JoinPoint joinPoint) {
        System.out.println("validate");
    }

    @Before("execution(* com.agiletec.plugins.jacms.apsadmin.content.ContentAction.joinGroup())")
    public void joinGroup(JoinPoint joinPoint) {
        System.out.println("joinGroup");
    }

    @Before("execution(* com.agiletec.plugins.jacms.apsadmin.content.ContentAction.removeGroup())")
    public void removeGroup(JoinPoint joinPoint) {
        System.out.println("removeGroup");
    }

    public IContentActionHelper getContentActionHelper() {
        return contentActionHelper;
    }

    public void setContentActionHelper(IContentActionHelper contentActionHelper) {
        this.contentActionHelper = contentActionHelper;
    }

}
