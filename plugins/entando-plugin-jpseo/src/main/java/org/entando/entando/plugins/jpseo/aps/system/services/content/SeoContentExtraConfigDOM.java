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

import com.agiletec.aps.system.exception.ApsSystemException;
import java.io.StringReader;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.ExtraConfigDOMUtil;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class SeoContentExtraConfigDOM {

    private static final Logger logger = LoggerFactory.getLogger(SeoContentExtraConfigDOM.class);

    public ContentMetadata addExtraConfig(String xml) throws ApsSystemException {
        if (StringUtils.isBlank(xml)) {
            return null;
        }
        Document doc = this.decodeDOM(xml);
        Element root = doc.getRootElement();
        ContentMetadata contentMetadata = new ContentMetadata();
        ExtraConfigDOMUtil.extractConfig(contentMetadata, root);
        return contentMetadata;
    }

    public String extractXml(ContentMetadata contentMetadata) {
        Document doc = new Document();
        Element elementRoot = new Element("config");
        doc.setRootElement(elementRoot);
        ExtraConfigDOMUtil.fillDocument(doc.getRootElement(), contentMetadata);
        return this.getXMLDocument(doc);
    }

    private Document decodeDOM(String xml) throws ApsSystemException {
        Document doc = null;
        SAXBuilder builder = new SAXBuilder();
        builder.setValidation(false);
        StringReader reader = new StringReader(xml);
        try {
            doc = builder.build(reader);
        } catch (Throwable t) {
            logger.error("Error while parsing xml: {} ", xml, t);
            throw new ApsSystemException("Error detected while parsing the XML", t);
        }
        return doc;
    }

    protected String getXMLDocument(Document doc) {
        XMLOutputter out = new XMLOutputter();
        Format format = Format.getPrettyFormat();
        out.setFormat(format);
        return out.outputString(doc);
    }

}
