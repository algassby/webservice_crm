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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
/**
 * @author algas
 *
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
@Tag(name = "User's role management", description = "role management")
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

	@Operation(summary = "Add role to user", description = "add role to user if exists", tags = { "User" })
	@ApiResponses(value = { 
    @ApiResponse(responseCode = "200", description = "successful operation", 
					content = @Content(schema = @Schema(implementation = ResponseMessage.class))),
	@ApiResponse(responseCode = "404", description = "appoitment not found",
			content = @Content(schema = @Schema(implementation = ResponseMessage.class))) })
	@PutMapping("/{username}/update/role/{roleName}")
	ResponseEntity<?> addRoleToUser(
			@Parameter(description = "username is required", required = true,
			content = @Content( schema = @Schema(implementation = String.class)))
			@PathVariable String username,
			@Parameter(description = "roleName is required", required = true,
			content = @Content( schema = @Schema(implementation = String.class)))
			@PathVariable String roleName){
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
