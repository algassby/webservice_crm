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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ynov.crm.responsedto.AppUserResponseDto;
import com.ynov.crm.responsedto.FileInfoResponseDto;
import com.ynov.crm.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
@Tag(name = "Get All users", description = "Allowed the get All users")
public class GetAllUserController {

	private UserService userService;

	/**
	 * @param userService
	 */
	@Autowired
	public GetAllUserController(UserService userService) {
		super();
		this.userService = userService;
	}
	@Operation(summary = "get All users")
    @ApiResponse(responseCode = "200", description = "Get All users or empty", 
    content = @Content(array = @ArraySchema( schema = @Schema(implementation = AppUserResponseDto.class))))
	@GetMapping
	public ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10")
		Integer pageSize, @RequestParam(defaultValue = "lastUpdate")  String sortBy) {

        return new ResponseEntity<>(userService.findAllUsersByPaging(pageNo, pageSize, sortBy),HttpStatus.OK);
       
    }
	
}
