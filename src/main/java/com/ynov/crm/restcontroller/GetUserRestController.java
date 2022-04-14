/**
 * 
 */
package com.ynov.crm.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ynov.crm.responsedto.AppUserResponseDto;
import com.ynov.crm.responsedto.ResponseMessage;
import com.ynov.crm.service.GetUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author algas
 *
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
@Tag(name = "Get a single user", description = "the Contact API")
public class GetUserRestController {

	
	private GetUserService getUserService;
	
	/**
	 * @param getUserService
	 */
	@Autowired
	public GetUserRestController(GetUserService getUserService) {
		super();
		this.getUserService = getUserService;
	}

	@Operation(summary = "get a user")
    @ApiResponse(responseCode = "200", description = "Get a users  nothing if not exists by message Execption", 
    content = @Content(schema = @Schema(implementation = AppUserResponseDto.class)))
	@ApiResponse(responseCode = "404", description = "user not found")
	@GetMapping("/{userId}")
	ResponseEntity<?> getUser(
			@PathVariable  String userId){
		if(userId==null || userId.isEmpty()) {
			return new ResponseEntity<>(new ResponseMessage("Wrong or empty username"), HttpStatus.BAD_REQUEST);
		}
		else {
			AppUserResponseDto appUserResponseDto = getUserService.getUser(userId);
			if(appUserResponseDto.getUserId()!=null) {
				return new ResponseEntity<>(appUserResponseDto, HttpStatus.OK);
			}
			else  {
				return new ResponseEntity<>(new ResponseMessage("User not found"), HttpStatus.NOT_FOUND);
			}
		}
	}
}
