/*
*
* <Your licensing text here>
*
*/
package org.entando.entando.plugins.jpredirect.aps.system.services.redirect;

import java.util.List;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface IRedirectDAO {
    
    public List<Integer> searchRedirects(FieldSearchFilter[] filters);
    
    public Redirect loadRedirect(int id);
    
    public List<Integer> loadRedirects();
    
    public void removeRedirect(int id);
    
    public void updateRedirect(Redirect redirect);
    
    public void insertRedirect(Redirect redirect);
    
    public int countRedirects(FieldSearchFilter[] filters);
    
    public List<Redirect> loadRedirectObjects();
}