/**
 * 
 */
package com.ynov.crm.restcontroller;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ynov.crm.enties.AppUser;
import com.ynov.crm.requestdto.AppUserRequestDto;
import com.ynov.crm.responsedto.ResponseMessage;
import com.ynov.crm.service.FindAllUserServiceImpl;
import com.ynov.crm.service.ServiceCreateUser;
import com.ynov.crm.service.UserExistByFieldService;
import com.ynov.crm.utils.DateManagement;

import lombok.Data;

/**
 * @author algas
 *
 */
@RestController
@Data
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class CreateUserRestController {
	
	private ServiceCreateUser userService;
	private DateManagement dateManagement;
	private FindAllUserServiceImpl findAllUserService;
	private UserExistByFieldService userExistByFieldService;

	
	/**
	 * @param userService
	 * @param dateManagement
	 * @param findAllUserService
	 * @param userExistByUsernameService
	 */
	@Autowired
	public CreateUserRestController(ServiceCreateUser userService, DateManagement dateManagement,
			FindAllUserServiceImpl findAllUserService, UserExistByFieldService userExistByFieldService) {
		super();
		this.userService = userService;
		this.dateManagement = dateManagement;
		this.findAllUserService = findAllUserService;
		this.userExistByFieldService = userExistByFieldService;
	}
	/**
	 * @param serviceUser
	 */
	@PostMapping("/save")
	ResponseEntity<?> save(@Valid @RequestBody AppUserRequestDto userDto){
		Date minDate = findAllUserService.findAllUsers().stream().map(AppUser::getLastUpdate).max(Date::compareTo).get();
		if(!dateManagement.dateCompare(new Date(),minDate)) {
			return new ResponseEntity<>(new ResponseMessage("Must be wait for 1 minute"),HttpStatus.OK);
		}
		if(userExistByFieldService.existsByEmail(userDto.getEmail()) || userExistByFieldService.existsByUsername(userDto.getUsername())) {
			return new ResponseEntity<>(new ResponseMessage("User already taken"),HttpStatus.BAD_REQUEST);
		}
		if (userService.save(userDto) != null){
			return new ResponseEntity<>(new ResponseMessage("L'administrateur a été ajouté avec succès!"), HttpStatus.OK);
		}
		return  new ResponseEntity<>(new ResponseMessage("Erreur lors de l'ajout de l'administrateur."), HttpStatus.EXPECTATION_FAILED);
	}

	
	


}
