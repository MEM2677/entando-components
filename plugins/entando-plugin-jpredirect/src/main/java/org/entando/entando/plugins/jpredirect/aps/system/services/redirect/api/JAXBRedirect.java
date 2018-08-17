/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpredirect.aps.system.services.redirect.api;



import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.entando.entando.plugins.jpredirect.aps.system.services.redirect.Redirect;

@XmlRootElement(name = "redirect")
@XmlType(propOrder = {"id", "pagecode", "groupname", "rolename"})
public class JAXBRedirect {

    public JAXBRedirect() {
        super();
    }

    public JAXBRedirect(Redirect redirect) {
		this.setId(redirect.getId());
		this.setPagecode(redirect.getPagecode());
		this.setGroupname(redirect.getGroupname());
		this.setRolename(redirect.getRolename());
    }
    
    public Redirect getRedirect() {
    	Redirect redirect = new Redirect();
		redirect.setId(this.getId());
		redirect.setPagecode(this.getPagecode());
		redirect.setGroupname(this.getGroupname());
		redirect.setRolename(this.getRolename());
    	return redirect;
    }

	@XmlElement(name = "id", required = true)
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	@XmlElement(name = "pagecode", required = true)
	public String getPagecode() {
		return _pagecode;
	}
	public void setPagecode(String pagecode) {
		this._pagecode = pagecode;
	}

	@XmlElement(name = "groupname", required = true)
	public String getGroupname() {
		return _groupname;
	}
	public void setGroupname(String groupname) {
		this._groupname = groupname;
	}

	@XmlElement(name = "rolename", required = true)
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
