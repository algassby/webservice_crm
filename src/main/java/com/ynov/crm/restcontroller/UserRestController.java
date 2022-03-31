/**
 * 
 */
package com.ynov.crm.restcontroller;


import java.util.Date;


import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ynov.crm.requestdto.AppUserRequestDto;
import com.ynov.crm.responsedto.AppUserResponseDto;
import com.ynov.crm.responsedto.ResponseMessage;
import com.ynov.crm.service.UserService;
import com.ynov.crm.utils.DateManagement;

import lombok.extern.slf4j.Slf4j;

/**
 * @author algas
 *
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
@Validated
public class UserRestController {

	private UserService userService;
	private DateManagement dateManagement;
	
	
	

	
	/**
	 * @param userService
	 * @param dateManagement
	 */
	@Autowired
	public UserRestController(UserService userService, DateManagement dateManagement) {
		super();
		this.userService = userService;
		this.dateManagement = dateManagement;

	}
	
	@GetMapping("/{userId}")
	ResponseEntity<?> getUser(@PathVariable String userId){
		if(userId==null || userId.isEmpty()) {
			return new ResponseEntity<>(new ResponseMessage("Wrong or empty username"), HttpStatus.BAD_REQUEST);
		}
		else {
			if(userService.existsById(userId)) {
				return new ResponseEntity<>(userService.getUser(userId), HttpStatus.FOUND);
			}
			else  {
				return new ResponseEntity<>(new ResponseMessage("User not found"), HttpStatus.NOT_FOUND);
			}
		}
	}
	
	@PostMapping("/save")
	ResponseEntity<?> save(@Valid @RequestBody AppUserRequestDto userDto){
		Date minDate = userService.getAllUsers().stream().map(AppUserResponseDto::getLastUpdate).max(Date::compareTo).get();
		if(!dateManagement.dateCompare(new Date(),minDate)) {
			log.info(String.valueOf(dateManagement.dateCompare(new Date(),userService.getAllUsers().stream().map(AppUserResponseDto::getLastUpdate).max(Date::compareTo).get())));
			return new ResponseEntity<>(new ResponseMessage("Must be wait for 1 minute"),HttpStatus.OK);
		}
		if(userService.existsByEmail(userDto.getEmail()) || userService.existsByUsername(userDto.getUsername())) {
			return new ResponseEntity<>(new ResponseMessage("User already taken"),HttpStatus.BAD_REQUEST);
		}
		if (userService.save(userDto) != null){
			return new ResponseEntity<>(new ResponseMessage("L'administrateur a été ajouté avec succès!"), HttpStatus.OK);
		}
		return  new ResponseEntity<>(new ResponseMessage("Erreur lors de l'ajout de l'administrateur."), HttpStatus.EXPECTATION_FAILED);
	}
	
	@PutMapping("/{userId}/update")
	@ExceptionHandler(ConstraintViolationException.class)
	ResponseEntity<?> update(@Valid @RequestBody AppUserRequestDto userDto, @PathVariable String userId){
		if(!userService.existsById(userId)){
			return new ResponseEntity<>(new ResponseMessage("User not found"),HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<>(userService.update(userDto, userId),HttpStatus.OK);
	}
	
	
	@DeleteMapping("/{userId}/delete")
	ResponseEntity<?> removeUser(@PathVariable String userId){
	log.debug(userId);
		
		
			if(userId!=null) {
				if(!userService.existsById(userId)){
					return new ResponseEntity<>(new ResponseMessage("User not found"),HttpStatus.NOT_FOUND);
				}
				else {
					return new ResponseEntity<>(new ResponseMessage(userService.removeUser(userId)),HttpStatus.OK);
				}
			}
			else {
				return new ResponseEntity<>(new ResponseMessage("Wrong userId"),HttpStatus.BAD_REQUEST);
			}
		
		
	}
	@PutMapping("/{username}/update/role/{roleName}")
	ResponseEntity<?> addRoleToUser(@PathVariable String username, @PathVariable String roleName){
		if(!userService.existsByUsername(username)){
			return new ResponseEntity<>(new ResponseMessage("User not found"),HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(new ResponseMessage(userService.addRoleToUser(username, roleName)),HttpStatus.NOT_FOUND);
	}
	@PutMapping("/{username}/delete/role/{roleName}")
	ResponseEntity<?> removeRoleToUser(@PathVariable String username, @PathVariable String roleName){
		if(!userService.existsByUsername(username)){
			return new ResponseEntity<>(new ResponseMessage("User not found"),HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(new ResponseMessage(userService.removeRoleToUser(username, roleName)),HttpStatus.NOT_FOUND);
	}
	
	@GetMapping
	public ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10")
		Integer pageSize, @RequestParam(defaultValue = "lastUpdate")  String sortBy) {

        return new ResponseEntity<>(userService.findAllUsersByPaging(pageNo, pageSize, sortBy),HttpStatus.OK);
       
    }
	
	 
}
