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

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import java.util.Map;
import org.entando.entando.plugins.jpseo.aps.system.JpseoSystemConstants;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.ObjectMetatag;

/**
 * @author M. Morini - E.Santoboni
 */
public class TestContentExtraParametersManager extends BaseTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    /*
<config>
  <descriptions>
    <property key="en">EN Content Description 1</property>
    <property key="it">Descrizione IT Content 1</property>
  </descriptions>
  <keywords>
    <property key="en" useDefaultLang="true" >keyEN1.1,keyEN1.2</property>
    <property key="it">keyIT1.1,keyIT1.2,keyIT1.3,keyIT1.4</property>
  </keywords>
  <complexParameters>
    <parameter key="key1">
        <property key="it">VALUE_1 IT ART111</property>
    </parameter>
    <parameter key="key2">
      <property key="fr">VALUE_2 FR ART111</property>
      <property key="en">VALUE_2 EN ART111</property>
      <property key="it">VALUE_2 IT ART111</property>
    </parameter>
    <parameter key="key3">
      <property key="en">VALUE_3 EN ART111</property>
      <property key="it">VALUE_3 IT ART111</property>
    </parameter>
    <parameter key="key4">
        <property key="it">VALUE_4 IT ART111</property>
    </parameter>
  </complexParameters>
</config>

     */
    public void testLoadContent_1() throws Throwable {
        Content contentWork = this._contentManager.loadContent("ART111", false);
        assertNotNull(contentWork);
        ContentMetadata contentMetadata = (ContentMetadata) contentWork.getExtraParams().get(JpseoSystemConstants.CONTENT_METADATA_KEY);
        assertNotNull(contentMetadata);
        ApsProperties descriptions = contentMetadata.getDescriptions();
        assertEquals(2, descriptions.size());
        assertTrue(descriptions.get("en") instanceof ObjectMetatag);
        ObjectMetatag metaDescrEn = (ObjectMetatag) descriptions.get("en");
        assertEquals("EN Content Description 1", metaDescrEn.getValue());
        assertEquals("name", metaDescrEn.getKeyAttribute());
        assertFalse(metaDescrEn.isUseDefaultLangValue());
        ApsProperties keywords = contentMetadata.getKeywords();
        assertEquals(2, keywords.size());
        ObjectMetatag metaKeywordsEn = (ObjectMetatag) keywords.get("en");
        assertEquals("keyEN1.1,keyEN1.2", metaKeywordsEn.getValue());
        assertTrue(metaKeywordsEn.isUseDefaultLangValue());

        Content contentOnLine = this._contentManager.loadContent("ART111", true);
        assertNotNull(contentOnLine);
        assertNull(contentOnLine.getExtraParams());
    }

    public void testLoadContent_2() throws Throwable {
        Content contentWork = this._contentManager.loadContent("ART90", false);
        assertNull(contentWork);
    }

    public void testLoadContent_3() throws Throwable {
        Content contentWork = this._contentManager.loadContent("ART1", false);
        assertNotNull(contentWork);
        ContentMetadata contentMetadata = (ContentMetadata) contentWork.getExtraParams().get(JpseoSystemConstants.CONTENT_METADATA_KEY);
        assertNotNull(contentMetadata);
        Map<String, Map<String, ObjectMetatag>> complexParams = contentMetadata.getComplexParameters();
        assertEquals(2, complexParams.size());
        Map<String, ObjectMetatag> keyLangEn = complexParams.get("en");
        assertNotNull(keyLangEn);
        assertEquals(2, keyLangEn.size());
        assertEquals("VALUE_2 EN ART1 work", keyLangEn.get("key2").getValue());

        Map<String, ObjectMetatag> keyLangIt = complexParams.get("it");
        assertNotNull(keyLangIt);
        assertEquals(3, keyLangIt.size());
        assertEquals("VALUE_4 IT ART1 work", keyLangIt.get("key4").getValue());

        Content contentOnLine = this._contentManager.loadContent("ART1", true);
        contentMetadata = (ContentMetadata) contentOnLine.getExtraParams().get(JpseoSystemConstants.CONTENT_METADATA_KEY);
        assertNotNull(contentMetadata);
        complexParams = contentMetadata.getComplexParameters();
        assertEquals(2, complexParams.size());
        keyLangEn = complexParams.get("en");
        assertNotNull(keyLangEn);
        assertEquals(1, keyLangEn.size());
        assertNull(keyLangEn.get("key2"));
        assertEquals("VALUE_3 EN ART1 online", keyLangEn.get("key3").getValue());

        keyLangIt = complexParams.get("it");
        assertNotNull(keyLangIt);
        assertEquals(2, keyLangIt.size());
        assertEquals("VALUE_4 IT ART1 online", keyLangIt.get("key4").getValue());
    }

    private void init() throws Exception {
        try {
            this._contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
        } catch (Throwable t) {
            throw new Exception(t);
        }
    }

    private IContentManager _contentManager = null;

}
