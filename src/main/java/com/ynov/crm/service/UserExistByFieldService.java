/**
 * 
 */
package com.ynov.crm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ynov.crm.repository.AppUserRepository;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author algas
 *
 */
@Service
@Data
@NoArgsConstructor
@Transactional
@Slf4j
public class UserExistByFieldService {

	private AppUserRepository appUserRepo;

	/**
	 * @param appUserRepo
	 */
	public UserExistByFieldService(AppUserRepository appUserRepo) {
		super();
		this.appUserRepo = appUserRepo;
	}
	
	
	public Boolean existsByUsername(String email) {
		return appUserRepo.existsByUsername(email);
	}
	public Boolean existsByEmail(String email) {
		return appUserRepo.existsByEmail(email);
	}

}
