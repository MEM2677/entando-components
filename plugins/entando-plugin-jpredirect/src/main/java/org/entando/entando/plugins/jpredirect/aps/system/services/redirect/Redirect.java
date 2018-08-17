/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpredirect.aps.system.services.redirect;



public class Redirect {

	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	public String getPagecode() {
		return _pagecode;
	}
	public void setPagecode(String pagecode) {
		this._pagecode = pagecode;
	}

	public String getGroupname() {
		return _groupname;
	}
	public void setGroupname(String groupname) {
		this._groupname = groupname;
	}

	public String getRolename() {
		return _rolename;
	}
	public void setRolename(String rolename) {
		this._rolename = rolename;
	}

	
	private int _id;
	private String _pagecode;
	private String _groupname;
	private String _rolename;

}
