/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpredirect.web.redirect.model;


import javax.validation.constraints.*;
import org.hibernate.validator.constraints.NotBlank;

public class RedirectRequest {

    @NotNull(message = "redirect.id.notBlank")	
	private Integer id;
	
	@Size(max = 30, message = "string.size.invalid")
	@NotBlank(message = "redirect.pagecode.notBlank")
	private String pagecode;
	
	@Size(max = 20, message = "string.size.invalid")
	@NotBlank(message = "redirect.groupname.notBlank")
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


}
