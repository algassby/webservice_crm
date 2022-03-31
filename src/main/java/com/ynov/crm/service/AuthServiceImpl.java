/**
 * 
 */
package com.ynov.crm.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ynov.crm.config.JwtProvider;
import com.ynov.crm.repository.AppRoleRepository;
import com.ynov.crm.repository.AppUserRepository;
import com.ynov.crm.requestdto.LoginForm;
import com.ynov.crm.responsedto.JwtResponse;

import lombok.Data;

/**
 * @author algas
 *
 */
 @Service
 @Transactional
 @Data
public class AuthServiceImpl implements AuthService {
	
	
	private AuthenticationManager authenticationManager;
	private AppRoleRepository roleRepository;
	private PasswordEncoder encoder;
	private JwtProvider jwtProvider;
	
	

	/**
	 * @param authenticationManager
	 * @param userRepository
	 * @param roleRepository
	 * @param encoder
	 * @param jwtProvider
	 */
	public AuthServiceImpl(AuthenticationManager authenticationManager,
			AppRoleRepository roleRepository, PasswordEncoder encoder, JwtProvider jwtProvider) {
		super();
		this.authenticationManager = authenticationManager;
		this.roleRepository = roleRepository;
		this.encoder = encoder;
		this.jwtProvider = jwtProvider;
	}



	@Override
	public JwtResponse signin(LoginForm loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getUserKey()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtProvider.generateJwtToken(authentication);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		return new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities());
	}

}
