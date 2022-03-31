/**
 * 
 */
package com.ynov.crm.service;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@Value("${crm.superAdmin}")
	private String superAdmin;

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
			FileInfoService fileService, PasswordEncoder encoder) {
		super();
		this.appUserRepo = appUserRepo;
		this.userMapper = userMapper;
		this.appRoleRepo = appRoleRepo;
		this.fileService = fileService;
		this.encoder = encoder;
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
		AppUser appUser =  userMapper.appUserRequestDtoToAppUser(userDto).setLastUpdate(new Date()).setAdminId(currentUser.getUserId()).setUserKey(encoder.encode(userDto.getUserKey()));

		Set<AppRole> roles = appUser.getRoles();

		if(userDto.getRoleName()!=null) {
			if(userDto.getRoleName().isEmpty()) {
				AppRole role = appRoleRepo.findByRoleName("ROLE_USER");
				roles.add(role);
			}

			else {
				roles.add(appRoleRepo.findByRoleName(userDto.getRoleName()));
			}

		}
		else {
			roles.add(appRoleRepo.findByRoleName("ROLE_USER"));
		}

		appUser.setRoles(roles);
		return userMapper.appUserToAppUserResponseDto(appUserRepo.save(appUser));
	}

}
