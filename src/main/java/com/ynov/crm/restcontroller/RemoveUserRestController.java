/**
 * 
 */
package com.ynov.crm.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ynov.crm.responsedto.ResponseMessage;
import com.ynov.crm.service.UserService;

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
public class RemoveUserRestController {
	
	private UserService userService;

	/**
	 * @param userService
	 * @param dateManagement
	 */
	@Autowired
	public RemoveUserRestController(UserService userService) {
		super();
		this.userService = userService;
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
}
