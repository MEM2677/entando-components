/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpredirect.aps.system.services.redirect.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;
import org.entando.entando.plugins.jpredirect.aps.system.services.redirect.Redirect;


public class RedirectChangedEvent extends ApsEvent {
	
	@Override
	public void notify(IManager srv) {
		((RedirectChangedObserver) srv).updateFromRedirectChanged(this);
	}
	
	@Override
	public Class getObserverInterface() {
		return RedirectChangedObserver.class;
	}
	
	public int getOperationCode() {
		return _operationCode;
	}
	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}
	
	public Redirect getRedirect() {
		return _redirect;
	}
	public void setRedirect(Redirect redirect) {
		this._redirect = redirect;
	}

	private Redirect _redirect;
	private int _operationCode;
	
	public static final int INSERT_OPERATION_CODE = 1;
	public static final int REMOVE_OPERATION_CODE = 2;
	public static final int UPDATE_OPERATION_CODE = 3;

}
