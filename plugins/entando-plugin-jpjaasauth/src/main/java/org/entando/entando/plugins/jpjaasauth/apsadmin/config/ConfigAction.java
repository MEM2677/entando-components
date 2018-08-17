package org.entando.entando.plugins.jpjaasauth.apsadmin.config;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpjaasauth.aps.system.services.login.IJAASUserManager;
import org.entando.entando.plugins.jpjaasauth.aps.system.services.login.model.JAASServiceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.system.BaseAction;

public class ConfigAction extends BaseAction {

	private static final Logger logger = LoggerFactory.getLogger(ConfigAction.class);
	
	public String edit() {
		try {
			JAASServiceConfig cfg = this.getUserManager().getConfig();
			toForm(cfg);
		} catch (Throwable t) {
			logger.error("error loading jaas service configuration", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String save() {
		try {
			JAASServiceConfig cfg = toConfig();
			if (this.isEnabled()
					&& StringUtils.isBlank(this.getRealmscsv())) {
				this.addActionError(this.getText("jpjaasauth.error.emptyrealm"));
//				this.addFieldError(this.getText("jpjaasauth.realm"), 
//						this.getText("jpjaasauth.error.emptyrealm"));
				return INPUT;
			}
			this.getUserManager().updateConfig(cfg);
		} catch (Throwable t) {
			logger.error("error saving jaas service configuration", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private void toForm(JAASServiceConfig cfg) throws Throwable {
		final StringBuffer str = new StringBuffer();
		
		this.setEnabled(cfg.isEnabled());
		this.setPrivileged(cfg.isPrivileged());
		Arrays.asList(cfg.getRealms())
			.forEach(r -> str.append(',' + r));
		this.setRealmscsv(str.toString().substring(1));
	}
	
	private JAASServiceConfig toConfig() throws Throwable {
		JAASServiceConfig cfg = new JAASServiceConfig();
		
		cfg.setEnabled(this.isEnabled());
		cfg.setPrivileged(this.isPrivileged());
		if (StringUtils.isNotBlank(this.getRealmscsv())) {
			String[] realms = this.getRealmscsv().split(",");
			
			cfg.setRealms(realms);
		}
		return cfg;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean active) {
		this.enabled = active;
	}
	public boolean isPrivileged() {
		return privileged;
	}
	public void setPrivileged(boolean provoleged) {
		this.privileged = provoleged;
	}
	public String getRealmscsv() {
		return realmscsv;
	}
	public void setRealmscsv(String realmscsv) {
		this.realmscsv = realmscsv;
	}
	
	public IJAASUserManager getUserManager() {
		return userManager;
	}
	public void setUserManager(IJAASUserManager userManager) {
		this.userManager = userManager;
	}

	private boolean enabled;
	private boolean privileged;
	private String realmscsv;
	
	private IJAASUserManager userManager;
}
