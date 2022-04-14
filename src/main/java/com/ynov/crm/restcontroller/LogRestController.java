/**
 * 
 */
package com.ynov.crm.restcontroller;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ynov.crm.enties.Log;
import com.ynov.crm.repository.LogRepository;
import com.ynov.crm.responsedto.ResponseMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author algas
 *
 */
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
@RestController
@RequestMapping("/api/logs")
@Tag(name = "Log Controller", description = "Get the logs")
public class LogRestController {
	private LogRepository logRepository;

	/**
	 * @param logRepository
	 */
	@Autowired
	public LogRestController(LogRepository logRepository) {
		super();
		this.logRepository = logRepository;
	}
	
	@Operation(summary = "Find Appointment by Customer Id", description = "get All Logs", tags = { "Appoitement" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation, return appointment if exists else not found", 
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = Log.class)))) })	
	@GetMapping
	ResponseEntity<?> getAll(@Valid @Positive(message = "the nbLogs must be positive") @RequestParam(name = "nbLogs", required = false, defaultValue = "500")  int nbLogs){
		
		if(nbLogs < 0) {
			return new ResponseEntity<>(new ResponseMessage("the nbLogs must be positive"), HttpStatus.OK);
		}
		return new ResponseEntity<>(logRepository.findAll().stream().limit(nbLogs)
				.collect(Collectors.toList()), HttpStatus.OK);
	}
	@GetMapping("/{username}")
	ResponseEntity<?> getAllByUsername(@PathVariable String username,
			@Valid @Positive(message = "the nbLogs must be positive") @RequestParam(name = "nbLogs", required = false, defaultValue = "500") int nbLogs){
		
		if(nbLogs < 0) {
			return new ResponseEntity<>(new ResponseMessage("the nbLogs must be positive"), HttpStatus.OK);
		}
		
		return new ResponseEntity<>(logRepository.findAllByUsername(username).stream().limit(nbLogs)
				.collect(Collectors.toList()), HttpStatus.OK);
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
