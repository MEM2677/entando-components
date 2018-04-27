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
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie;

import java.util.HashMap;
import java.util.Map;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants;
import org.entando.entando.plugins.jprestapi.aps.core.Endpoint;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants.HEADER_KEY_ACCEPT;
import static org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants.HEADER_VALUE_JSON;

public class DmnManager extends KieFormManager implements IDmnManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public JSONObject getDmnModel(String containerId) throws ApsSystemException {
        Map<String, String> headersMap = new HashMap<>();
        String result = null;
        JSONObject json = null;
        if (!super.getConfig().getActive() || StringUtils.isBlank(containerId)) {
            return json;
        }
        try {
            // process endpoint first
            //containerId = "credit-dispute-decisions_1.0-SNAPSHOT";
            Endpoint ep = KieEndpointDictionary.create().get(KieBpmSystemConstants.API_GET_DMN_MODEL).resolveParams(containerId);
            // add header
            headersMap.put(HEADER_KEY_ACCEPT, HEADER_VALUE_JSON);
            // generate client from the current configuration
            KieClient client = super.getCurrentClient();
            // perform query
            result = (String) new KieRequestBuilder(client)
                                                           .setEndpoint(ep)
                                                           .setHeaders(headersMap)
                                                           .setDebug(super.getConfig().getDebug())
                                                           .doRequest();

            if (!result.isEmpty()) {
                json = new JSONObject(result);
                logger.debug("received successful message: ", result);
            } else {
                logger.debug("received empty dmn model message: ");
            }

        } catch (Throwable t) {
            throw new ApsSystemException("Error getting the dmn model", t);
        }

        return json;
    }


}
