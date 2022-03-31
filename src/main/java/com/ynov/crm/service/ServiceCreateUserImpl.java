/**
 * 
 */
package com.ynov.crm.service;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ynov.crm.enties.AppRole;
import com.ynov.crm.enties.AppUser;
import com.ynov.crm.mapper.UserMapper;
import com.ynov.crm.repository.AppRoleRepository;
import com.ynov.crm.repository.AppUserRepository;
import com.ynov.crm.requestdto.AppUserRequestDto;
import com.ynov.crm.responsedto.AppUserResponseDto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author algas
 *
 */
@Service
@Transactional
@Data
@Slf4j
public class ServiceCreateUserImpl implements ServiceCreateUser {

	private AppUserRepository appUserRepo;
	private UserMapper userMapper;
	private AppRoleRepository  appRoleRepo;
	private FileInfoService fileService;
	private UserPrinciple currentUser;
	private PasswordEncoder encoder;
	private MailService mailService;
	@Value("${crm.superAdmin}")
	private String superAdmin;
	private String subject;
	

	/**
	 * @param appUserRepo
	 * @param userMapper
	 * @param appRoleRepo
	 * @param fileService
	 * @param currentUser
	 * @param encoder
	 * @param superAdmin
	 */
	@Autowired
	public ServiceCreateUserImpl(AppUserRepository appUserRepo, UserMapper userMapper, AppRoleRepository appRoleRepo,
			FileInfoService fileService, PasswordEncoder encoder, MailService mailService) {
		super();
		this.appUserRepo = appUserRepo;
		this.userMapper = userMapper;
		this.appRoleRepo = appRoleRepo;
		this.fileService = fileService;
		this.encoder = encoder;
		this.mailService = mailService;
	}
	
	/**
	 * init current user
	 */
	public void initCurrentUser(){
		 currentUser  =  (UserPrinciple) SecurityContextHolder.getContext().getAuthentication(). getPrincipal();
	}
	@Override
	public AppUserResponseDto save(AppUserRequestDto userDto) {
		this.initCurrentUser();
		String text = null;
		String newLine = System.getProperty("line.separator");
		String apiKeyUser = UUID.randomUUID().toString();
		AppUser appUser =  userMapper.appUserRequestDtoToAppUser(userDto)
				.setLastUpdate(new Date()).setAdminId(currentUser.getUserId()).setUserKey(encoder.encode(apiKeyUser));

		Set<AppRole> roles = appUser.getRoles();

		if(userDto.getRoleName()!=null) {
			if(userDto.getRoleName().isEmpty()) {
				AppRole role = appRoleRepo.findByRoleName("ROLE_USER");
				roles.add(role);
			}

			else {
				roles.add(appRoleRepo.findByRoleName("ROLE_USER"));
			}

		}
		else {
			roles.add(appRoleRepo.findByRoleName(userDto.getRoleName()));
		}

		appUser.setRoles(roles);
		AppUser appUserSaved = appUserRepo.save(appUser);
		text = String.join(" ",
				"Hello ",
				appUser.getFirstName(),
				appUser.getLastName().toUpperCase(),
				", ",
				"Your admin account is created successFully.",
				newLine,
				"Here your username and your userKey to allowed you to management Organization and customer:",
				newLine,
				"username: ",
				appUser.getUsername(),
				" And userKey: ",
				apiKeyUser,
				".",
				newLine,
				"This key is  unique and it's only for you.",
				"Please Keep it safe and dont share it to other.",
				newLine,
				"Here the API documentation with open UI swagger:",
				"http://localhost:8084/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#",
				".",
				newLine,
				"Please Use Postman to test all futures.",
				newLine,
				"Thank you to choosing our api for your CRM.",
				"Best Regards, ",
				newLine,
				"TEAM BSM YNOV"
				);
		
		this.mailService.sendEmail("Create administrator account", text, userDto.getEmail());
		return userMapper.appUserToAppUserResponseDto(appUserSaved);
	}

}
