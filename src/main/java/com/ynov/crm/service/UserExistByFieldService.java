/**
 * 
 */
package com.ynov.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ynov.crm.repository.AppUserRepository;

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

public class UserExistByFieldService {

	private AppUserRepository appUserRepo;

	/**
	 * @param appUserRepo
	 */
	@Autowired
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
	public Boolean existsById(String userId) {
		return appUserRepo.existsById(userId);
	}

}
