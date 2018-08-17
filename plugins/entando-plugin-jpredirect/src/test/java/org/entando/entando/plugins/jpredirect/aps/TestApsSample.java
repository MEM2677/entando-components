package org.entando.entando.plugins.jpredirect.aps;

import com.agiletec.aps.system.ApsSystemUtils;
import org.entando.entando.plugins.jpredirect.aps.system.services.redirect.IRedirectManager;

public class TestApsSample extends ApsPluginBaseTestCase {
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
    
    public void test() {
        assertNotNull(this.redirectManager);
    }
    
    private void init() throws Exception {
        try {
            // init services
            this.redirectManager =
                  (IRedirectManager) this.getService(IRedirectManager.BEAN_ID);
        } catch (Exception e) {
            throw e;
        }
    }
    
    private IRedirectManager redirectManager;
}
