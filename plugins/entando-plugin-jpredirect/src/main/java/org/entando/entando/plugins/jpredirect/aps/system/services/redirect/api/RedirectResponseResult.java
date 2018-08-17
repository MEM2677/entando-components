/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpredirect.aps.system.services.redirect.api;

import javax.xml.bind.annotation.XmlElement;

import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;


public class RedirectResponseResult extends AbstractApiResponseResult {
    
    @Override
    @XmlElement(name = "redirect", required = false)
    public JAXBRedirect getResult() {
        return (JAXBRedirect) this.getMainResult();
    }
    
}