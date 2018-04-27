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
package org.entando.entando.plugins.jpkiebpm.aps.system.services;

import org.entando.entando.plugins.jpkiebpm.KieTestParameters;
import org.entando.entando.plugins.jpkiebpm.aps.ApsPluginBaseTestCase;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IDmnManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.json.JSONObject;

public class TestKieDmnManager extends ApsPluginBaseTestCase implements KieTestParameters {

    protected IDmnManager dmnManager;

    private static final String CONTAINER_ID = "credit-dispute-decisions_1.0-SNAPSHOT";

    @Override
    public void setUp() throws Exception {
        super.setUp();
        dmnManager = (IDmnManager) this.getService(IDmnManager.BEAN_NAME);
    }

    protected KieBpmConfig getConfigForTests() {
        KieBpmConfig cfg = new KieBpmConfig();
        cfg.setActive(TEST_ENABLED);
        cfg.setHostname("localhost");
        cfg.setPassword("krisv");
        cfg.setPort(8080);
        cfg.setSchema(SCHEMA);
        cfg.setUsername("krisv");
        cfg.setWebapp(WEBAPP);
        cfg.setTimeoutMsec(TIMEOUT);
        return cfg;
    }

    public void testGetDmnModel() throws Throwable {
        KieBpmConfig current = dmnManager.getConfig();

        try {
            // update configuration to reflect test configuration
            dmnManager.updateConfig(this.getConfigForTests());

            JSONObject dmnResult = dmnManager.getDmnModel(CONTAINER_ID);

            assertNotNull(dmnResult);
        } catch (Throwable t) {
            throw t;
        } finally {
            dmnManager.updateConfig(current);
        }
    }




}
