package com.ynov.crm.restcontroller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import com.ynov.crm.requestdto.LoginForm;
import com.ynov.crm.responsedto.JwtResponse;
import com.ynov.crm.service.AuthServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*", maxAge = 3600,allowedHeaders = "*")
@RestController
@RequestMapping("/api/auth")
@Tag(name = "User Authentification", description = "API Authentification allowed to access customer and organization management")
public class AuthRestController {

	@Autowired
	private AuthServiceImpl authServiceImpl;
	
	  
	@Operation(summary = "User Authentification", description = "Auth by username and userKey", tags = { "Appoitement" })
	  @ApiResponses(value = {
		        @ApiResponse(responseCode = "200", description = "successful operation, return Jwt and user inforation or unhautorized 401",
		        content = @Content(schema = @Schema(implementation = JwtResponse.class))),
		        @ApiResponse(responseCode = "401", description = "Invalid username or useKey, unhautorized "),
		        @ApiResponse(responseCode = "405", description = "Validation exception") })

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(
			@Parameter(description="loginRequest cannot be empty or null", required = true,
					schema=@Schema(implementation = LoginForm.class))
			@Valid @RequestBody LoginForm loginRequest) {
		return ResponseEntity.ok(authServiceImpl.signin(loginRequest));
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