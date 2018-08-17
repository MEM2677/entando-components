/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package org.entando.entando.plugins.jpjaasauth.aps.system.services.login;

import javax.security.auth.callback.*;

/**
 *
 * @author super
 */
public class JAASCallbackHandler  implements CallbackHandler {
        
    public JAASCallbackHandler(String user, String pass) {
        this.username = user;
        this.password = pass.toCharArray();
    }
    
    public void handle(Callback[] callbacks) throws java.io.IOException, UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof NameCallback) {
                ((NameCallback)callbacks[i]).setName(username);
            } else if (callbacks[i] instanceof PasswordCallback) {
                ((PasswordCallback)callbacks[i]).setPassword(password);
            } else {
                throw new UnsupportedCallbackException(callbacks[i], "Callback class not supported");
            }
        }
    }
 
    private String username;
    private char[] password;
    
}