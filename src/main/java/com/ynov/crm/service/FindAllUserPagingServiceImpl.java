/**
 * 
 */
package com.ynov.crm.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public class FindAllUserPagingServiceImpl implements FindAllUserService {

	private  AppUserRepository appUserRepo;
	private UserMapper userMapper;
	private UserPrinciple currentUser;
	@Value("${crm.superAdmin}")
	private String superAdmin;
	
	/**
	 * @param appUserRepo
	 * @param userMapper
	 */
	@Autowired
	public FindAllUserPagingServiceImpl(AppUserRepository appUserRepo, UserMapper userMapper) {
		super();
		this.appUserRepo = appUserRepo;
		this.userMapper = userMapper;
	}

	public void initCurrentUser(){
		 currentUser  =  (UserPrinciple) SecurityContextHolder.getContext().getAuthentication(). getPrincipal();
	}
	@Override
	public List<AppUserResponseDto> findAllUsersByPaging(Integer pageNo, Integer pageSize, String sortBy) {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		this.initCurrentUser();
		
		log.debug(String.valueOf(this.currentUser.getUsername().equals(getSuperAdmin())));
		 Page<AppUser> pagedResult = appUserRepo.findAll(paging);
		if(this.currentUser.getUsername().equals(getSuperAdmin())) {
			return pagedResult.stream().map(user->userMapper.appUserToAppUserResponseDto(user))
					.sorted(Comparator.comparing(AppUserResponseDto::getLastUpdate).reversed())
					.collect(Collectors.toList());
		}
		return pagedResult.stream().map(user->userMapper.appUserToAppUserResponseDto(user))
				.sorted(Comparator.comparing(AppUserResponseDto::getLastUpdate).reversed())
				.filter(user->user.getUsername().equals(this.currentUser.getUsername()))
				.collect(Collectors.toList());
	}
}
