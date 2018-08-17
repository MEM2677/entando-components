/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpjaasauth.web.mapping.model;


import javax.validation.constraints.*;
import org.hibernate.validator.constraints.NotBlank;

public class MappingRequest {

    @NotNull(message = "mapping.id.notBlank")	
	private Integer id;
	
	@NotBlank(message = "mapping.jrole.notBlank")
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


}
