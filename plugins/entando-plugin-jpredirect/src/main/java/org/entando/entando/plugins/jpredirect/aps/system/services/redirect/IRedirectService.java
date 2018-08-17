/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpredirect.aps.system.services.redirect;

import  org.entando.entando.plugins.jpredirect.aps.system.services.redirect.model.RedirectDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.plugins.jpredirect.web.redirect.model.RedirectRequest;

public interface IRedirectService {

    public String BEAN_NAME = "jpredirectRedirectService";

    public PagedMetadata<RedirectDto> getRedirects(RestListRequest requestList);

    public RedirectDto updateRedirect(RedirectRequest redirectRequest);

    public RedirectDto addRedirect(RedirectRequest redirectRequest);

    public void removeRedirect(int id);

    public RedirectDto getRedirect(int  id);

}

