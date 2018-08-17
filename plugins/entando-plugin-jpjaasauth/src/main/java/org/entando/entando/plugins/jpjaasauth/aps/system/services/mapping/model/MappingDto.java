/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpjaasauth.aps.system.services.mapping.model;



public class MappingDto {

	private int id;
	private String jrole;
	private String egroup;
	private String erole;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getJrole() {
		return jrole;
	}
	public void setJrole(String jrole) {
		this.jrole = jrole;
	}

	public String getEgroup() {
		return egroup;
	}
	public void setEgroup(String egroup) {
		this.egroup = egroup;
	}

	public String getErole() {
		return erole;
	}
	public void setErole(String erole) {
		this.erole = erole;
	}


    public static String getEntityFieldName(String dtoFieldName) {
		return dtoFieldName;
    }
    
}
