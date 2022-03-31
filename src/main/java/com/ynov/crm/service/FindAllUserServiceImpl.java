/**
 * 
 */
package com.ynov.crm.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
import lombok.extern.slf4j.Slf4j;

/**
 * @author algas
 *
 */
@Service
@Transactional
@Data
@Slf4j
public class FindAllUserServiceImpl {

	private  AppUserRepository appUserRepo;
	private UserPrinciple currentUser;
	@Value("${crm.superAdmin}")
	private String superAdmin;
	
	/**
	 * @param appUserRepo
	 * @param userMapper
	 */
	@Autowired
	public FindAllUserServiceImpl(AppUserRepository appUserRepo) {
		super();
		this.appUserRepo = appUserRepo;
		
	}

	public void initCurrentUser(){
		 currentUser  =  (UserPrinciple) SecurityContextHolder.getContext().getAuthentication(). getPrincipal();
	}
	
	public List<AppUser> findAllUsers() {
		this.initCurrentUser();
		log.debug(currentUser.toString());
		log.debug(String.valueOf(this.currentUser.getUsername().equals(getSuperAdmin())));
		if(this.currentUser.getUsername().equals(getSuperAdmin())) {
			return appUserRepo.findAll().stream()
					.sorted(Comparator.comparing(AppUser::getLastUpdate).reversed())
					.collect(Collectors.toList());
		}
		return appUserRepo.findAll().stream()
				.sorted(Comparator.comparing(AppUser::getLastUpdate).reversed())
				.filter(user->user.getUsername().equals(this.currentUser.getUsername()))
				.collect(Collectors.toList());
		
	}
	

}
