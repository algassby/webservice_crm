/**
 * 
 */
package com.ynov.crm.restcontroller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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
public class UserManageRoleRestController {

	private UserService userService;

	/**
	 * @param userService
	 * @param dateManagement
	 */
	@Autowired
	public UserManageRoleRestController(UserService userService) {
		super();
		this.userService = userService;
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
	
	
	
	 
}
