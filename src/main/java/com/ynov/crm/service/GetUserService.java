/**
 * 
 */
package com.ynov.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynov.crm.enties.AppUser;
import com.ynov.crm.mapper.UserMapper;
import com.ynov.crm.repository.AppUserRepository;
import com.ynov.crm.responsedto.AppUserResponseDto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author algas
 *
 */
@Service
@Data
@NoArgsConstructor
@Transactional

public class GetUserService {

	private AppUserRepository appUserRepo;
	private UserMapper userMapper;
	private UserPrinciple currentUser;
	private UserExistByFieldService userExistByFieldService;
	
	/**
	 * @param appUserRepo
	 * @param userMapper
	 * @param userExistByFieldService
	 */
	@Autowired
	public GetUserService(AppUserRepository appUserRepo, UserMapper userMapper,
			UserExistByFieldService userExistByFieldService) {
		super();
		this.appUserRepo = appUserRepo;
		this.userMapper = userMapper;
		this.userExistByFieldService = userExistByFieldService;
	}
	
	@Value("${crm.superAdmin}")
	private String superAdmin;
	public AppUserResponseDto getUser(String userId) {
		this.initCurrentUser();
		if(this.currentUser.getUsername().equals(superAdmin)) {
			return userMapper.appUserToAppUserResponseDto(appUserRepo.findById(userId).get());
		}
		if(userExistByFieldService.existsById(userId)) {
			AppUser appUser = appUserRepo.findById(userId).get();
			if(appUser.getUserId().equals(currentUser.getUserId())) {
				return userMapper.appUserToAppUserResponseDto(appUser); 
			}
			
		}
		return new AppUserResponseDto();
	    
	}
	
	/**
	 * init current user
	 */
	public void initCurrentUser(){
		 currentUser  =  (UserPrinciple) SecurityContextHolder.getContext().getAuthentication(). getPrincipal();
	}

	
}
