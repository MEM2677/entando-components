/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpjaasauth.aps.system.services.mapping.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;
import org.entando.entando.plugins.jpjaasauth.aps.system.services.mapping.Mapping;


public class MappingChangedEvent extends ApsEvent {
	
	@Override
	public void notify(IManager srv) {
		((MappingChangedObserver) srv).updateFromMappingChanged(this);
	}
	
	@Override
	public Class getObserverInterface() {
		return MappingChangedObserver.class;
	}
	
	public int getOperationCode() {
		return _operationCode;
	}
	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}
	
	public Mapping getMapping() {
		return _mapping;
	}
	public void setMapping(Mapping mapping) {
		this._mapping = mapping;
	}

	private Mapping _mapping;
	private int _operationCode;
	
	public static final int INSERT_OPERATION_CODE = 1;
	public static final int REMOVE_OPERATION_CODE = 2;
	public static final int UPDATE_OPERATION_CODE = 3;

}
