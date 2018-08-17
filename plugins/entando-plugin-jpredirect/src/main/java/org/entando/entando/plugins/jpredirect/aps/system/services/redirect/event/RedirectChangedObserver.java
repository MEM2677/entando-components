/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpredirect.aps.system.services.redirect.event;

import com.agiletec.aps.system.common.notify.ObserverService;

public interface RedirectChangedObserver extends ObserverService {
	
	public void updateFromRedirectChanged(RedirectChangedEvent event);
	
}
