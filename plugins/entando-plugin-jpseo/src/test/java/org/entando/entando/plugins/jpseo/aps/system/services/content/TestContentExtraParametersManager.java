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

import java.util.Map;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.HypertextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoListAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.TextAttribute;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.LinkAttribute;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.ResourceAttributeInterface;

/**
 * @author M. Morini - E.Santoboni
 */
public class TestContentExtraParametersManager extends BaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testLoadContent() throws Throwable {
		Content content = this._contentManager.loadContent("ART111", false);
		assertEquals(Content.STATUS_PUBLIC, content.getStatus());
		assertEquals("coach", content.getMainGroup());
		assertEquals(2, content.getGroups().size());
		assertTrue(content.getGroups().contains("customers"));
		assertTrue(content.getGroups().contains("helpdesk"));
        
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
