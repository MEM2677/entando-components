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

import com.agiletec.aps.util.ApsProperties;
import java.io.Serializable;
import java.util.Map;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.IObjectMetadata;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.ObjectMetatag;

/**
 * @author E.Santoboni
 */
public class ContentMetadata implements IObjectMetadata, Serializable {

    private ApsProperties descriptions = new ApsProperties();
    private ApsProperties keywords = new ApsProperties();

    private Map<String, Map<String, ObjectMetatag>> complexParameters;

    @Override
    public String getDescription(String langCode) {
        ObjectMetatag meta = (null != this.getDescriptions()) ? (ObjectMetatag) this.getDescriptions().get(langCode) : null;
        return (null != meta) ? meta.getValue() : null;
    }

    @Override
    public ApsProperties getDescriptions() {
        return descriptions;
    }

    @Override
    public void setDescriptions(ApsProperties descriptions) {
        this.descriptions = descriptions;
    }

    public String getKeyword(String langCode) {
        ObjectMetatag meta = (null != this.getKeywords()) ? (ObjectMetatag) this.getKeywords().get(langCode) : null;
        return (null != meta) ? meta.getValue() : null;
    }

    @Override
    public ApsProperties getKeywords() {
        return keywords;
    }

    @Override
    public void setKeywords(ApsProperties keywords) {
        this.keywords = keywords;
    }

    @Override
    public Map<String, Map<String, ObjectMetatag>> getComplexParameters() {
        return complexParameters;
    }

    @Override
    public void setComplexParameters(Map<String, Map<String, ObjectMetatag>> complexParameters) {
        this.complexParameters = complexParameters;
    }

}
