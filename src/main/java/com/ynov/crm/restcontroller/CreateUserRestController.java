/**
 * 
 */
package com.ynov.crm.restcontroller;

import java.util.Date;
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

import com.ynov.crm.enties.AppUser;
import com.ynov.crm.requestdto.AppUserRequestDto;
import com.ynov.crm.requestdto.verifRequestDto.VerifAppointment;
import com.ynov.crm.responsedto.ResponseMessage;
import com.ynov.crm.service.FindAllUserServiceImpl;
import com.ynov.crm.service.ServiceCreateUser;
import com.ynov.crm.service.UserExistByFieldService;
import com.ynov.crm.utils.DateManagement;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

/**
 * @author algas
 *
 */
@RestController
@Data
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping("/api/users")
@Tag(name = "Appointment", description = "Manage the customer appointment")
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
