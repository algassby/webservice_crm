/**
 * 
 */
package com.ynov.crm.restcontroller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
public class GetAllUserController {

	private UserService userService;

	/**
	 * @param userService
	 */
	public GetAllUserController(UserService userService) {
		super();
		this.userService = userService;
	}
	@GetMapping
	public ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10")
		Integer pageSize, @RequestParam(defaultValue = "lastUpdate")  String sortBy) {

        return new ResponseEntity<>(userService.findAllUsersByPaging(pageNo, pageSize, sortBy),HttpStatus.OK);
       
    }
	
}
