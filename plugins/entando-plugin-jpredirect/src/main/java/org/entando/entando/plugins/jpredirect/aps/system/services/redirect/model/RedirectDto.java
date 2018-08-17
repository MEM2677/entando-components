/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpredirect.aps.system.services.redirect.model;



public class RedirectDto {

	private int id;
	private String pagecode;
	private String groupname;
	private String rolename;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getPagecode() {
		return pagecode;
	}
	public void setPagecode(String pagecode) {
		this.pagecode = pagecode;
	}

	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}


    public static String getEntityFieldName(String dtoFieldName) {
		return dtoFieldName;
    }
    
}
