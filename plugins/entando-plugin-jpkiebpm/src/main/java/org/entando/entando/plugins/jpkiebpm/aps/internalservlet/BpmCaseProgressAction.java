/*
 * The MIT License
 *
 * Copyright 2018 Entando Inc..
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
package org.entando.entando.plugins.jpkiebpm.aps.internalservlet;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.BaseAction;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.CaseManager;
import static org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.CaseProgressWidgetHelpers.getContainerIDfromfrontEndMilestonesData;
import static org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.CaseProgressWidgetHelpers.getKieIDfromfrontEndMilestonesData;
import static org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.CaseProgressWidgetHelpers.updatefrontEndMilestonesDataMilestones;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author own_strong
 */
public class BpmCaseProgressAction extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(BpmCaseProgressAction.class);
    private CaseManager caseManager;
    private String channel;
    private String frontEndMilestonesData;
//    private List<String> cases;
    private String caseInstanceMilestones;

    private String configuredKnowledgeSourceId;
    private String configuredContainerid;

    private String knowledgeSourceId;
    private String casePath;
    private String containerid;
    private String channelPath;

    public String view() {
        try {

            String frontEndMilestonesDataIn = extractWidgetConfig("frontEndMilestonesData");
            this.setFrontEndMilestonesData(frontEndMilestonesDataIn);
            String channelIn = extractWidgetConfig("channel");

            this.setConfiguredKnowledgeSourceId(getKieIDfromfrontEndMilestonesData(frontEndMilestonesDataIn));
            this.setConfiguredContainerid(getContainerIDfromfrontEndMilestonesData(frontEndMilestonesDataIn));
            this.setChannel(channelIn);

            this.getCaseManager().setKieServerConfiguration(this.getConfiguredKnowledgeSourceId());

            if ((!StringUtils.isBlank(this.getKnowledgeSourceId()) || !StringUtils.isBlank(this.getContainerid()) || !StringUtils.isBlank(this.getCasePath()) || !StringUtils.isBlank(this.getChannelPath()))
                    && (this.getKnowledgeSourceId().equalsIgnoreCase(this.getConfiguredKnowledgeSourceId()))
                    && (this.getContainerid().equalsIgnoreCase(this.getConfiguredContainerid()))
                    && (this.getChannelPath().equalsIgnoreCase(this.getChannel()))) {

                String updatedMilestones = this.getCaseManager().getMilestonesList(this.getConfiguredContainerid(), this.getCasePath()).toString();
                this.setCaseInstanceMilestones(updatefrontEndMilestonesDataMilestones(this.getFrontEndMilestonesData(), updatedMilestones));

            } else {

                this.setCasePath(this.getCaseManager().getCaseInstancesList(this.getConfiguredContainerid()).get(0));
                String updatedMilestones = this.getCaseManager().getMilestonesList(this.getConfiguredContainerid(), this.getCasePath()).toString();
                this.setCaseInstanceMilestones(updatefrontEndMilestonesDataMilestones(this.getFrontEndMilestonesData(), updatedMilestones));

            }
        } catch (ApsSystemException t) {
            logger.error("Error getting the configuration parameter", t);
            return FAILURE;
        }

        return SUCCESS;
    }

    //Helper classes
    protected String extractWidgetConfig(String paramName) {
        String value = null;
        try {
            HttpServletRequest request = (null != this.getRequest()) ? this.getRequest() : ServletActionContext.getRequest();
            RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
            if (null != reqCtx) {
                Widget widget = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
                if (null != widget) {
                    ApsProperties config = widget.getConfig();
                    if (null != config) {
                        String widgetParam = config.getProperty(paramName);
                        if (widgetParam != null && widgetParam.trim().length() > 0) {
                            value = widgetParam.trim();
                        }
                    } else {
                        value = "Null widget config";
                        logger.error("Null widget config");
                    }
                } else {
                    logger.error("Null widget");
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException("Error extracting param " + paramName, t);
        }
        return value;
    }

    public CaseManager getCaseManager() {
        return caseManager;
    }

    public void setCaseManager(CaseManager caseManager) {
        this.caseManager = caseManager;
    }

    public String getFrontEndMilestonesData() {
        return frontEndMilestonesData;
    }

    public void setFrontEndMilestonesData(String frontEndMilestonesData) {
        this.frontEndMilestonesData = frontEndMilestonesData;
    }

    public String getCasePath() {
        return casePath;
    }

    public void setCasePath(String casePath) {
        this.casePath = casePath;
    }

    public String getCaseInstanceMilestones() {
        return caseInstanceMilestones;
    }

    public void setCaseInstanceMilestones(String caseInstanceMilestones) {
        this.caseInstanceMilestones = caseInstanceMilestones;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getKnowledgeSourceId() {
        return knowledgeSourceId;
    }

    public void setKnowledgeSourceId(String knowledgeSourceId) {
        this.knowledgeSourceId = knowledgeSourceId;
    }

    public String getContainerid() {
        return containerid;
    }

    public void setContainerid(String containerid) {
        this.containerid = containerid;
    }

    public String getChannelPath() {
        return channelPath;
    }

    public void setChannelPath(String channelPath) {
        this.channelPath = channelPath;
    }

    public String getConfiguredKnowledgeSourceId() {
        return configuredKnowledgeSourceId;
    }

    public void setConfiguredKnowledgeSourceId(String configuredKnowledgeSourceId) {
        this.configuredKnowledgeSourceId = configuredKnowledgeSourceId;
    }

    public String getConfiguredContainerid() {
        return configuredContainerid;
    }

    public void setConfiguredContainerid(String configuredContainerid) {
        this.configuredContainerid = configuredContainerid;
    }

}
