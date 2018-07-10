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
package org.entando.entando.plugins.jpseo.aps.system.services.page;

import org.entando.entando.plugins.jpseo.aps.system.services.mapping.ObjectMetatag;
import com.agiletec.aps.system.services.page.PageExtraConfigDOM;
import com.agiletec.aps.system.services.page.PageMetadata;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.ExtraConfigDOMUtil;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dom class for parse the xml of extra page config
 *
 * @author E.Santoboni
 */
public class SeoPageExtraConfigDOM extends PageExtraConfigDOM {

    private static final Logger _logger = LoggerFactory.getLogger(SeoPageExtraConfigDOM.class);

    private static final String USE_EXTRA_DESCRIPTIONS_ELEMENT_NAME = "useextradescriptions";
    
    private static final String FRIENDLY_CODE_ELEMENT_NAME = "friendlycode";
    
    @Deprecated
    private static final String XML_CONFIG_ELEMENT_NAME = "xmlConfig";

    @Override
    protected void addExtraConfig(PageMetadata pageMetadata, Document doc) {
        super.addExtraConfig(pageMetadata, doc);
        if (!(pageMetadata instanceof SeoPageMetadata)) {
            return;
        }
        Element root = doc.getRootElement();
        SeoPageMetadata seoPage = (SeoPageMetadata) pageMetadata;
        Element useExtraDescriptionsElement = root.getChild(USE_EXTRA_DESCRIPTIONS_ELEMENT_NAME);
        if (null != useExtraDescriptionsElement) {
            Boolean value = Boolean.valueOf(useExtraDescriptionsElement.getText());
            seoPage.setUseExtraDescriptions(value.booleanValue());
        }
        Element friendlyCodeElement = root.getChild(FRIENDLY_CODE_ELEMENT_NAME);
        if (null != friendlyCodeElement) {
            seoPage.setFriendlyCode(friendlyCodeElement.getText());
        }
        ExtraConfigDOMUtil.extractConfig(seoPage, root);
        Element xmlConfigElement = root.getChild(XML_CONFIG_ELEMENT_NAME);
        if (null != xmlConfigElement) {
            //Used to guarantee porting with previous versions of the plugin
            String xml = xmlConfigElement.getText();
            seoPage.setComplexParameters(this.extractComplexParameters(xml));
        }
    }
    
    /**
     * Extract the complex parameters from string
     *
     * @param xmlConfig the config
     * @return the map of complex parameters
     * @deprecated Used to guarantee porting with previous versions of the
     * plugin
     */
    private Map<String, Map<String, ObjectMetatag>> extractComplexParameters(String xmlConfig) {
        Document doc = this.decodeComplexParameterDOM(xmlConfig);
        List<Element> elements = doc.getRootElement().getChildren();
        return ExtraConfigDOMUtil.extractComplexParameters(elements);
    }
    
    /*
.....
    OLD STRUCTURE
  <complexParameters>
    <parameter key="key1">VALUE_1</parameter>
    <parameter key="key2">VALUE_2</parameter>
    <parameter key="key5">
      <property key="fr">VALUE_5 FR</property>
      <property key="en">VALUE_5 EN</property>
      <property key="it">VALUE_5 IT</property>
    </parameter>
    <parameter key="key6">VALUE_6</parameter>
    <parameter key="key3">
      <property key="en">VALUE_3 EN</property>
      <property key="it">VALUE_3 IT</property>
    </parameter>
    <parameter key="key4">VALUE_4</parameter>
  </complexParameters>
.....
    
    
    New Structure
.....
  <complexParameters>
    <lang code="it">
      <meta key="key5">VALUE_5_IT</meta>
      <meta key="key3" attributeName="name" useDefaultLang="false" >VALUE_3_IT</meta>
      <meta key="key2" attributeName="property" useDefaultLang="true" />
    </lang>
    <lang code="en">
      <meta key="key5">VALUE_5_IT</meta>
      <meta key="key3" attributeName="name" useDefaultLang="false" >VALUE_3_EN</meta>
      <meta key="key2" attributeName="property" useDefaultLang="true" />
    </lang>
    ...
    ...
  </complexParameters>
.....
    */
    
    @Deprecated
    private Document decodeComplexParameterDOM(String xml) {
        Document doc = null;
        SAXBuilder builder = new SAXBuilder();
        builder.setValidation(false);
        StringReader reader = new StringReader(xml);
        try {
            doc = builder.build(reader);
        } catch (Throwable t) {
            _logger.error("Error while parsing xml: {}", xml, t);
        }
        return doc;
    }
     
    @Override
    protected void fillDocument(Document doc, PageMetadata pageMetadata) {
        super.fillDocument(doc, pageMetadata);
        if (!(pageMetadata instanceof SeoPageMetadata)) {
            return;
        }
        SeoPageMetadata seoPageMetadata = (SeoPageMetadata) pageMetadata;
        Element useExtraDescriptionsElement = new Element(USE_EXTRA_DESCRIPTIONS_ELEMENT_NAME);
        useExtraDescriptionsElement.setText(String.valueOf(seoPageMetadata.isUseExtraDescriptions()));
        doc.getRootElement().addContent(useExtraDescriptionsElement);
        if (null != seoPageMetadata.getFriendlyCode() && seoPageMetadata.getFriendlyCode().trim().length() > 0) {
            Element friendlyCodeElement = new Element(FRIENDLY_CODE_ELEMENT_NAME);
            friendlyCodeElement.setText(seoPageMetadata.getFriendlyCode().trim());
            doc.getRootElement().addContent(friendlyCodeElement);
        }
        ExtraConfigDOMUtil.fillDocument(doc.getRootElement(), seoPageMetadata);
    }
    
}
