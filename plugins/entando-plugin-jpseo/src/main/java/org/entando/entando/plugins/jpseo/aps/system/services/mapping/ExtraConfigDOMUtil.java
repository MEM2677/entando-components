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
package org.entando.entando.plugins.jpseo.aps.system.services.mapping;

import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsProperties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jdom.Element;

/**
 * @author E.Santoboni
 */
public class ExtraConfigDOMUtil {
    
    private static final String DESCRIPTIONS_ELEMENT_NAME = "descriptions";
    private static final String KEYWORDS_ELEMENT_NAME = "keywords";
    private static final String USE_DEFAULT_LANG_ELEMENT_NAME = "useDefaultLang";
    private static final String PROPERTY_ELEMENT_NAME = "property";
    private static final String KEY_ATTRIBUTE_NAME = "key";
    private static final String COMPLEX_PARAMS_ELEMENT_NAME = "complexParameters";
    
    public static Map<String, Map<String, ObjectMetatag>> extractComplexParameters(List<Element> elements) {
        Map<String, Map<String, ObjectMetatag>> complexParameters = new HashMap<>();
        if (null == elements) {
            return complexParameters;
        }
        for (Element paramElement : elements) {
            String elementName = paramElement.getName();
            if (elementName.equals("parameter")) {
                //Used to guarantee porting with previous versions of the plugin
                String key = paramElement.getAttributeValue("key");
                List<Element> langElements = paramElement.getChildren("property");
                if (null != langElements && langElements.size() > 0) {
                    for (Element langElement : langElements) {
                        String langCode = langElement.getAttributeValue("key");
                        Map<String, ObjectMetatag> langMap = extractLangMap(langCode, complexParameters);
                        ObjectMetatag metatag = new ObjectMetatag(langCode, key, langElement.getText());
                        langMap.put(key, metatag);
                    }
                } else {
                    Map<String, ObjectMetatag> defaultLang = extractLangMap("default", complexParameters);
                    ObjectMetatag metatag = new ObjectMetatag("default", key, paramElement.getText());
                    defaultLang.put(key, metatag);
                }
            } else if (elementName.equals("lang")) {
                String langCode = paramElement.getAttributeValue("code");
                Map<String, ObjectMetatag> langMap = extractLangMap(langCode, complexParameters);
                List<Element> langElements = paramElement.getChildren("meta");
                for (Element langElement : langElements) {
                    String key = langElement.getAttributeValue("key");
                    ObjectMetatag metatag = new ObjectMetatag(langCode, key, langElement.getText());
                    metatag.setKeyAttribute(langElement.getAttributeValue("attributeName"));
                    String useDefaultLang = langElement.getAttributeValue(USE_DEFAULT_LANG_ELEMENT_NAME);
                    metatag.setUseDefaultLangValue(Boolean.parseBoolean(useDefaultLang));
                    langMap.put(key, metatag);
                }
            }
        }
        return complexParameters;
    }
    
    private static Map<String, ObjectMetatag> extractLangMap(String code, Map<String, Map<String, ObjectMetatag>> complexParameters) {
        Map<String, ObjectMetatag> langMap = complexParameters.get(code);
        if (null == langMap) {
            langMap = new HashMap<>();
            complexParameters.put(code, langMap);
        }
        return langMap;
    }
    
    public static void extractConfig(IObjectMetadata seoPage, Element root) {
        Element descriptionsElement = root.getChild(DESCRIPTIONS_ELEMENT_NAME);
        extractMultilangProperty(descriptionsElement, seoPage.getDescriptions(), "description");
        Element keywordsElement = root.getChild(KEYWORDS_ELEMENT_NAME);
        extractMultilangProperty(keywordsElement, seoPage.getKeywords(), "keywords");
        Element complexParamElement = root.getChild(COMPLEX_PARAMS_ELEMENT_NAME);
        if (null != complexParamElement) {
            List<Element> elements = complexParamElement.getChildren();
            seoPage.setComplexParameters(ExtraConfigDOMUtil.extractComplexParameters(elements));
        }
    }
    
    private static void extractMultilangProperty(Element mainElement, ApsProperties propertyToFill, String propertyName) {
        if (null != mainElement) {
            List<Element> mainElements = mainElement.getChildren(PROPERTY_ELEMENT_NAME);
            for (int i = 0; i < mainElements.size(); i++) {
                Element singleElement = mainElements.get(i);
                String langCode = singleElement.getAttributeValue(KEY_ATTRIBUTE_NAME);
                String useDefaultLang = singleElement.getAttributeValue(USE_DEFAULT_LANG_ELEMENT_NAME);
                ObjectMetatag metatag = new ObjectMetatag(langCode, propertyName, singleElement.getText());
                metatag.setUseDefaultLangValue(Boolean.parseBoolean(useDefaultLang));
                propertyToFill.put(langCode, metatag);
            }
        }
    }
    
    public static void fillDocument(Element elementRoot, IObjectMetadata seoPageMetadata) {
        ApsProperties descriptions = seoPageMetadata.getDescriptions();
        fillMultilangProperty(descriptions, elementRoot, DESCRIPTIONS_ELEMENT_NAME);
        ApsProperties keywords = seoPageMetadata.getKeywords();
        fillMultilangProperty(keywords, elementRoot, KEYWORDS_ELEMENT_NAME);
        if (null != seoPageMetadata.getComplexParameters()) {
            Element complexConfigElement = new Element(COMPLEX_PARAMS_ELEMENT_NAME);
            ExtraConfigDOMUtil.addComplexParameters(complexConfigElement, seoPageMetadata.getComplexParameters());
            elementRoot.addContent(complexConfigElement);
        }
    }
    
    private static void fillMultilangProperty(ApsProperties property, Element elementToFill, String elementName) {
        if (null != property && property.size() > 0) {
            Element mlElement = new Element(elementName);
            elementToFill.addContent(mlElement);
            Iterator<Object> iterator = property.keySet().iterator();
            while (iterator.hasNext()) {
                String langCode = (String) iterator.next();
                Element langElement = new Element(PROPERTY_ELEMENT_NAME);
                langElement.setAttribute(KEY_ATTRIBUTE_NAME, langCode);
                ObjectMetatag metatag = (ObjectMetatag) property.get(langCode);
                langElement.setAttribute(USE_DEFAULT_LANG_ELEMENT_NAME, String.valueOf(metatag.isUseDefaultLangValue()));
                langElement.setText(metatag.getValue());
                mlElement.addContent(langElement);
            }
        }
    }
    
    public static void addComplexParameters(Element elementRoot, Map<String, Map<String, ObjectMetatag>> parameters) {
        if (null == parameters) {
            return;
        }
        Iterator<String> iter1 = parameters.keySet().iterator();
        while (iter1.hasNext()) {
            String langCode = iter1.next();
            Map<String, ObjectMetatag> metas = parameters.get(langCode);
            if (langCode.equals("default")) {
                Iterator<String> iter2 = metas.keySet().iterator();
                while (iter2.hasNext()) {
                    String key2 = iter2.next();
                    Element parameterElement = new Element("parameter");
                    ObjectMetatag metatag = metas.get(key2);
                    parameterElement.setAttribute("key", metatag.getKey());
                    parameterElement.setText(metatag.getValue());
                    elementRoot.addContent(parameterElement);
                }
            } else {
                Element langElement = new Element("lang");
                langElement.setAttribute("code", langCode);
                Iterator<String> iter2 = metas.keySet().iterator();
                while (iter2.hasNext()) {
                    String key2 = iter2.next();
                    Element metaElement = new Element("meta");
                    ObjectMetatag metatag = metas.get(key2);
                    metaElement.setAttribute("key", metatag.getKey());
                    metaElement.setAttribute("attributeName", metatag.getKeyAttribute());
                    metaElement.setAttribute(USE_DEFAULT_LANG_ELEMENT_NAME, String.valueOf(metatag.isUseDefaultLangValue()));
                    metaElement.setText(metatag.getValue());
                    langElement.addContent(metaElement);
                }
                elementRoot.addContent(langElement);
            }
        }
    }
    
    /**
     * Utility method
     * @param seoParameters
     * @param defaultLang
     * @return 
     */
    public static Map<String, Map<String, ObjectMetatag>> extractRightParams(Map<String, Map<String, ObjectMetatag>> seoParameters, Lang defaultLang) {
        Map<String, Map<String, ObjectMetatag>> newMap = new HashMap<>();
        Map<String, ObjectMetatag> defaultMetas = null;
        Iterator<String> iter = seoParameters.keySet().iterator();
        while (iter.hasNext()) {
            String langKey = iter.next();
            Map<String, ObjectMetatag> metas = seoParameters.get(langKey);
            if (langKey.equals("default")) {
                defaultMetas = metas;
            } else {
                newMap.put(langKey, metas);
            }
        }
        if (null != defaultMetas) {
            String defaultLangCode = defaultLang.getCode();
            Map<String, ObjectMetatag> currentDefaultMetas = newMap.get(defaultLangCode);
            if (null == currentDefaultMetas) {
                currentDefaultMetas = new HashMap<>();
                newMap.put(defaultLangCode, currentDefaultMetas);
            }
            Iterator<String> iter2 = defaultMetas.keySet().iterator();
            while (iter2.hasNext()) {
                String key = iter2.next();
                ObjectMetatag meta = defaultMetas.get(key);
                meta.setLangCode(defaultLangCode);
                currentDefaultMetas.put(key, meta);
            }
        }
        Map<String, ObjectMetatag> currentDefaultMetas = newMap.get(defaultLang.getCode());
        List<String> langKeys = new ArrayList<>(newMap.keySet());
        Iterator<ObjectMetatag> iterMetatag = currentDefaultMetas.values().iterator();
        while (iterMetatag.hasNext()) {
            ObjectMetatag defaultMeta = iterMetatag.next();
            for (String langKey : langKeys) {
                if (langKey.equals(defaultLang.getCode())) {
                    continue;
                }
                Map<String, ObjectMetatag> otherMetas = newMap.get(langKey);
                if (null == otherMetas.get(defaultMeta.getKey())) {
                    ObjectMetatag missingMeta = new ObjectMetatag(langKey, defaultMeta.getKey(), null);
                    missingMeta.setKeyAttribute(defaultMeta.getKeyAttribute());
                    otherMetas.put(defaultMeta.getKey(), missingMeta);
                }
            }
        }
        return newMap;
    }
    
}
