/**
 * 
 */
package com.ynov.crm.service;

import com.ynov.crm.requestdto.LoginForm;
import com.ynov.crm.responsedto.JwtResponse;

/**
 * @author algas
 *
 */
public interface AuthService {
	public JwtResponse signin(LoginForm loginForm);
}
