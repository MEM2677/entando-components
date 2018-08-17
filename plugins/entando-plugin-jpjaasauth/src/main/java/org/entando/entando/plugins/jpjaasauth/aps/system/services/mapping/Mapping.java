/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpjaasauth.aps.system.services.mapping;



public class Mapping {

	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	public String getJrole() {
		return _jrole;
	}
	public void setJrole(String jrole) {
		this._jrole = jrole;
	}

	public String getEgroup() {
		return _egroup;
	}
	public void setEgroup(String egroup) {
		this._egroup = egroup;
	}

	public String getErole() {
		return _erole;
	}
	public void setErole(String erole) {
		this._erole = erole;
	}

	
	private int _id;
	private String _jrole;
	private String _egroup;
	private String _erole;

}
