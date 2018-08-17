package org.entando.entando.plugins.jpjaasauth.aps.system.services.login;

import org.entando.entando.plugins.jpjaasauth.aps.system.services.login.model.JAASServiceConfig;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.IUserManager;

public interface IJAASUserManager extends IUserManager {

	public JAASServiceConfig getConfig();

	public void updateConfig(JAASServiceConfig cfg) throws ApsSystemException;

}
