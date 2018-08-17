package org.entando.entando.plugins.jpjaasauth.aps.system.services.login.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class JAASServiceConfig {

	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String[] getRealms() {
		return realms;
	}
	public void setRealms(String[] realms) {
		this.realms = realms;
	}
	
	public boolean isPrivileged() {
		return privileged;
	}
	public void setPrivileged(boolean priviledged) {
		this.privileged = priviledged;
	}

	private boolean enabled;
	private String[] realms;
	private boolean privileged;
	
}
