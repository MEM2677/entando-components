/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpjaasauth.aps.system.services.mapping.event;

import com.agiletec.aps.system.common.notify.ObserverService;

public interface MappingChangedObserver extends ObserverService {
	
	public void updateFromMappingChanged(MappingChangedEvent event);
	
}
