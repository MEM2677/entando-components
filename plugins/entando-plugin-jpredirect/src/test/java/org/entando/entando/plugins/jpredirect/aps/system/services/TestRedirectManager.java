/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpredirect.aps.system.services;

import org.entando.entando.plugins.jpredirect.aps.JpredirectBaseTestCase;
import org.entando.entando.plugins.jpredirect.aps.system.services.redirect.IRedirectManager;

public class TestRedirectManager extends JpredirectBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testGetRedirect() {
		//TODO complete test
		assertNotNull(this._redirectManager);
	}

	public void testGetRedirects() {
		//TODO complete test
		assertNotNull(this._redirectManager);
	}
	
	public void testSearchRedirects() {
		//TODO complete test
		assertNotNull(this._redirectManager);
	}

	public void testAddRedirect() {
		//TODO complete test
		assertNotNull(this._redirectManager);
	}

	public void testUpdateRedirect() {
		//TODO complete test
		assertNotNull(this._redirectManager);
	}

	public void testDeleteRedirect() {
		//TODO complete test
		assertNotNull(this._redirectManager);
	}
	
	private void init() {
		//TODO add the spring bean id as constant
		this._redirectManager = (IRedirectManager) this.getService("jpredirectRedirectManager");
	}
	
	private IRedirectManager _redirectManager;
}

