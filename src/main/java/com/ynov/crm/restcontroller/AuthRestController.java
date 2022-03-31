package com.ynov.crm.restcontroller;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ynov.crm.config.JwtProvider;
import com.ynov.crm.enties.AppRole;
import com.ynov.crm.enties.AppUser;
import com.ynov.crm.repository.AppRoleRepository;
import com.ynov.crm.repository.AppUserRepository;
import com.ynov.crm.requestdto.AppUserRequestDto;
import com.ynov.crm.requestdto.LoginForm;
import com.ynov.crm.responsedto.JwtResponse;
import com.ynov.crm.responsedto.ResponseMessage;

@CrossOrigin(origins = "*", maxAge = 3600,allowedHeaders = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired 
	private AppUserRepository userRepository;


	@Autowired
	AppRoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtProvider jwtProvider;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getUserKey()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtProvider.generateJwtToken(authentication);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody AppUserRequestDto userDto) {
		if (userRepository.existsByUsername(userDto.getUsername())) {
			return new ResponseEntity<>(new ResponseMessage("Fail -> Username is already taken!"),
					HttpStatus.BAD_REQUEST);
		}

		if (userRepository.existsByEmail(userDto.getEmail())) {
			return new ResponseEntity<>(new ResponseMessage("Fail -> Email is already in use!"),
					HttpStatus.BAD_REQUEST);
		}
//		Set<String> strRoles = userDto.getRole();
		AppRole userRole = roleRepository.findByRoleName("ROLE_USER");
		Set<AppRole> roles = new HashSet<>();
		roles.add(userRole);
		// Creating user's account
		AppUser user = new AppUser();
		user.setFirstName(userDto.getFirstName()).setLastName(userDto.getLastName()).setUsername(userDto.getUsername()).setEmail(userDto.getEmail())
			.setUserKey(encoder.encode(userDto.getUserKey())).setRoles(roles);

		

//		strRoles.forEach(role -> {
//			switch (role) {
//			case "ADMIN":
//				Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
//						.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
//				roles.add(adminRole);
//
//				break;
//			case "SUPER":
//				Role pmRole = roleRepository.findByName(RoleName.ROLE_SUPER_ADMIN)
//						.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
//				roles.add(pmRole);
//
//				break;
//			default:
//				Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
//						.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
//				roles.add(userRole);
//			}
//		});roles.add(userRole);
//		user.setRoles(roles);
//		
		userRepository.save(user);

		return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
	}
	}