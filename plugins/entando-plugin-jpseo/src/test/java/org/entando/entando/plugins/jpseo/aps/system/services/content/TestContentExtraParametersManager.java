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
import java.util.HashMap;
import java.util.Map;
import org.entando.entando.plugins.jpseo.aps.system.JpseoSystemConstants;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.ExtraConfigDOMUtil;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.ObjectMetatag;

/**
 * @author E.Santoboni
 */
public class TestContentExtraParametersManager extends BaseTestCase {

    private IContentManager contentManager = null;
    private IContentExtraParametersManager contentExtraParametersManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
        this.contentExtraParametersManager = (IContentExtraParametersManager) this.getService(JpseoSystemConstants.CONTENT_EXTRA_PARAMS_MANAGER);
    }

    public void testLoadContent_1() throws Throwable {
        Content contentWork = this.contentManager.loadContent("ART111", false);
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

        Content contentOnLine = this.contentManager.loadContent("ART111", true);
        assertNotNull(contentOnLine);
        assertNull(contentOnLine.getExtraParams());
    }

    public void testLoadContent_2() throws Throwable {
        Content contentWork = this.contentManager.loadContent("ART90", false);
        assertNull(contentWork);
    }

    public void testLoadContent_3() throws Throwable {
        Content contentWork = this.contentManager.loadContent("ART1", false);
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

        Content contentOnLine = this.contentManager.loadContent("ART1", true);
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

    public void testAddExtraParams() throws Throwable {
        String[] masterContentIds = {"ART111", "ART179", "ALL4", "EVN23", "EVN24"};
        String[] newContentIds = null;
        try {
            newContentIds = this.addContentsForTest(masterContentIds, false);
            for (int i = 0; i < newContentIds.length; i++) {
                String newContentId = newContentIds[i];
                Content content = this.contentManager.loadContent(newContentId, false);
                assertNotNull(content.getExtraParams());
                ContentMetadata metas = (ContentMetadata) content.getExtraParams().get(JpseoSystemConstants.CONTENT_METADATA_KEY);
                assertNotNull(metas);
                String[] langs = {"it", "en"};
                for (String langCode : langs) {
                    assertEquals("Description " + langCode + " " + this.getTestMarker(i), metas.getDescription(langCode));
                    assertEquals("Keywords " + langCode + " " + this.getTestMarker(i), metas.getKeyword(langCode));
                    Map<String, ObjectMetatag> keyLang = metas.getComplexParameters().get(langCode);
                    for (int j = 0; j < 5; j++) {
                        ObjectMetatag meta = keyLang.get("key" + j);
                        assertNotNull(meta);
                        assertEquals("Meta " + langCode + " " + this.getTestMarker(i), meta.getValue());
                    }
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        } finally {
            this.deleteContents(newContentIds);
        }
    }

    protected String[] addContentsForTest(String[] masterContentIds, boolean publish) throws Throwable {
        String[] newContentIds = new String[masterContentIds.length];
        try {
            for (int i = 0; i < masterContentIds.length; i++) {
                Content content = this.contentManager.loadContent(masterContentIds[i], false);
                content.setId(null);
                ContentMetadata metas = this.createMockMetadata(this.getTestMarker(i));
                content.addExtraParam(JpseoSystemConstants.CONTENT_METADATA_KEY, metas);
                this.contentManager.saveContent(content);
                newContentIds[i] = content.getId();
                if (publish) {
                    this.contentManager.insertOnLineContent(content);
                }
                ContentExtraParametersVO contentParamsVO = this.contentExtraParametersManager.getContentExtraParameters(content.getId());
                assertNotNull(contentParamsVO);
                assertNotNull(contentParamsVO.getWorkxml());
                assertEquals(publish, null != contentParamsVO.getOnlinexml());
            }
        } catch (Exception e) {
            this.deleteContents(newContentIds);
            throw e;
        }
        return newContentIds;
    }

    private String getTestMarker(int index) {
        return "Index " + index;
    }

    private ContentMetadata createMockMetadata(String marker) {
        ContentMetadata metas = new ContentMetadata();
        ApsProperties descriptions = new ApsProperties();
        ApsProperties keywords = new ApsProperties();
        metas.setComplexParameters(new HashMap<>());
        String[] langs = {"it", "en"};
        for (String langCode : langs) {
            ObjectMetatag metatagDescr = new ObjectMetatag(langCode, ExtraConfigDOMUtil.DESCRIPTION_PROPERTY_NAME, "Description " + langCode + " " + marker);
            descriptions.put(langCode, metatagDescr);
            ObjectMetatag metatagKeywords = new ObjectMetatag(langCode, ExtraConfigDOMUtil.KEYWORDS_PROPERTY_NAME, "Keywords " + langCode + " " + marker);
            keywords.put(langCode, metatagKeywords);
            Map<String, ObjectMetatag> keyLang = new HashMap<>();
            for (int i = 0; i < 5; i++) {
                ObjectMetatag meta = new ObjectMetatag(langCode, "key" + i, "Meta " + langCode + " " + marker);
                meta.setUseDefaultLangValue(i % 2 == 1);
                keyLang.put("key" + i, meta);
            }
            metas.getComplexParameters().put(langCode, keyLang);
        }
        metas.setDescriptions(descriptions);
        metas.setKeywords(keywords);
        return metas;
    }

    private void deleteContents(String[] contentIds) throws Throwable {
        for (int i = 0; i < contentIds.length; i++) {
            if (null == contentIds[i]) {
                continue;
            }
            Content content = this.contentManager.loadContent(contentIds[i], false);
            if (null != content) {
                this.contentManager.removeOnLineContent(content);
                this.contentManager.deleteContent(content);
            }
        }
    }

}
