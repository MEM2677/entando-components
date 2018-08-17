/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpredirect.aps.system.services.redirect.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;
import org.entando.entando.aps.system.services.api.model.ListResponse;

@XmlSeeAlso({JAXBRedirect.class})
public class RedirectListResponseResult extends AbstractApiResponseResult {
    
    @XmlElement(name = "items", required = false)
    public ListResponse<JAXBRedirect> getResult() {
        if (this.getMainResult() instanceof Collection) {
            List<JAXBRedirect> redirects = new ArrayList<JAXBRedirect>();
            redirects.addAll((Collection<JAXBRedirect>) this.getMainResult());
            ListResponse<JAXBRedirect> entity = new ListResponse<JAXBRedirect>(redirects) {};
            return entity;
        }
        return null;
    }

}