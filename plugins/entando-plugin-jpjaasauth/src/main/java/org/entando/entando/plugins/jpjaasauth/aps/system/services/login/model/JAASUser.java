package org.entando.entando.plugins.jpjaasauth.aps.system.services.login.model;

import javax.security.auth.Subject;

import com.agiletec.aps.system.services.user.User;

public class JAASUser extends User {

	@Override
	public boolean isEntandoUser() {
		return false;
	}
	
	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	
	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	private Subject subject;
	private String realm;
}
