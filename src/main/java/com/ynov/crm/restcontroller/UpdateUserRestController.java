/**
 * 
 */
package com.ynov.crm.restcontroller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ynov.crm.requestdto.AppUserRequestDto;
import com.ynov.crm.responsedto.AppUserResponseDto;
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
@Tag(name = "Updated User", description = "Update user information")
public class UpdateUserRestController {
	
	private UserService userService;

	/**
	 * @param userService
	 * @param dateManagement
	 */
	@Autowired
	public UpdateUserRestController(UserService userService) {
		super();
		this.userService = userService;


	}
	  
	@Operation(summary = "Update an existing user", description = "Update a user by his id", tags = { "User" })
	@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "successful operation, or error",
    content = @Content(schema = @Schema(implementation = AppUserResponseDto.class))),
    @ApiResponse(responseCode = "405", description = "Validation exception", content = @Content(schema = 
    @Schema(implementation = MethodArgumentNotValidException.class)) ) })
	@PutMapping("/{userId}/update")
	@ExceptionHandler(ConstraintViolationException.class)
	ResponseEntity<?> update(
			@Parameter(description = "UserRequestDto cannot be null or empty", required = true,
			content = @Content(schema = @Schema(implementation = AppUserRequestDto.class)))
			@Valid @RequestBody AppUserRequestDto userDto,
			@Parameter(description="user id Cannot be empty or null.", required = true)
			@PathVariable String userId){
		if(!userService.existsById(userId)){
			return new ResponseEntity<>(new ResponseMessage("User not found"),HttpStatus.NOT_FOUND);
		}
		AppUserResponseDto user =  userService.update(userDto, userId);
		
		return  (user.getUserId()!=null?  new ResponseEntity<>(user,HttpStatus.OK) :
			new ResponseEntity<>(new ResponseMessage("Cannot Update user, please verify the user information"),HttpStatus.BAD_REQUEST)) ;
	}
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
	

}
