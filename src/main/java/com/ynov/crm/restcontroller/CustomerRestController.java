/**
 * 
 */
package com.ynov.crm.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ynov.crm.requestdto.CustomerRequestDto;
import com.ynov.crm.requestdto.JsonObjectDto;
import com.ynov.crm.responsedto.CustomerResponseDto;
import com.ynov.crm.responsedto.ResponseMessage;
import com.ynov.crm.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import lombok.Data;

/**
 * @author algas
 *
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
@RequestMapping("/api/customers")

public class CustomerRestController {

	private CustomerService customerService;

	/**
	 * @param customerService
	 */
	@Autowired
	public CustomerRestController(CustomerService customerService) {
		super();
		this.customerService = customerService;
	}
	
	@GetMapping
	@Operation(summary = "get All Customers")
    @ApiResponse(responseCode = "400", description = "Invalid Name Supplied")
    @ApiResponse(responseCode = "404", description = "customer not found")
    
	public ResponseEntity<?> getAllCustomer(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10")
	Integer pageSize, @RequestParam(defaultValue = "lastName")  String sortBy) {
		return new ResponseEntity<>(customerService.getAllCustomer(pageNo, pageSize, sortBy), HttpStatus.OK);
		
	}
	@GetMapping("/findbyOragnizationId")
	public ResponseEntity<?> findByOrganization (@RequestParam String orgnizationId){
		return ResponseEntity.ok().body(customerService.findByOrganization(orgnizationId));
	}
	@GetMapping("/{customerId}")
	public ResponseEntity<?> getCustomer(@PathVariable String customerId) {
		if(customerId ==null) {
			return new ResponseEntity<>(new ResponseMessage("User not found"), HttpStatus. NOT_FOUND); 
		}
	
		if(customerService.existsById(customerId)) {
			return new ResponseEntity<>(customerService.getCustomer(customerId), HttpStatus.OK); 
		}

		return new ResponseEntity<>(new ResponseMessage("User not found"), HttpStatus. NOT_FOUND); 
		
		
		
	}
	@PostMapping("/save")
	public ResponseEntity<?> save(@Valid @RequestBody CustomerRequestDto customerRequestDto) {
		if(customerRequestDto.getOrgaId()==null) {
			return new ResponseEntity<>(new ResponseMessage("Organization inexistante"), HttpStatus.OK); 
		}
	
		CustomerResponseDto customerResponseDto = customerService.save(customerRequestDto);
		return customerResponseDto.getCustomerId()!=null ? new ResponseEntity<>(customerResponseDto, HttpStatus.OK)
				:  new ResponseEntity<>(new ResponseMessage("Cannot save the customer, because the organization is not found"), HttpStatus.OK);  
		
	}
	
	@PutMapping("/{customerId}/update")
	public ResponseEntity<?> update(@Valid @RequestBody CustomerRequestDto customerRequestDto, @PathVariable String customerId) {
		if(customerId ==null) {
			return new ResponseEntity<>(new ResponseMessage("User not found"), HttpStatus. NOT_FOUND); 
		}
		if(customerService.existsById(customerId)) {
			
			return new ResponseEntity<>(customerService.update(customerRequestDto, customerId), HttpStatus.OK); 
		}
		return new ResponseEntity<>(new ResponseMessage("Cannot  Updated User, because doest not exists!"), HttpStatus.BAD_REQUEST); 
		
	}
	
	@DeleteMapping("/{customerId}/delete")
	public ResponseEntity<?> update(@PathVariable String customerId) {
		return new ResponseEntity<>(new ResponseMessage(customerService.delete(customerId)), HttpStatus.OK); 
		
	}
	
	@PutMapping("/update/{customerId}/images/save")
	public ResponseEntity<?> addManyImage(@PathVariable  String customerId, @RequestParam(value = "files") MultipartFile files []) {
		if(files.length==0) {
			return new ResponseEntity<>(new ResponseMessage("Cannot  Upload images to customer, cause file is empty!"), HttpStatus.BAD_REQUEST); 
		}
		return new ResponseEntity<>(new ResponseMessage(customerService.addAllImageToCustomer(customerId, files)), HttpStatus.OK); 
		
	}
	
	
	@PutMapping("/update/{customerId}/images/delete")
	public ResponseEntity<?> removeManyImageToCustomer(@PathVariable  String customerId, @RequestBody JsonObjectDto jsonRequestDto) {
		if(jsonRequestDto.getImages().isEmpty()) {
			return new ResponseEntity<>(new ResponseMessage("Cannot  delete images from customer, cause files list is empty!"), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(new ResponseMessage(customerService.removeManyImageToCustomer(customerId, jsonRequestDto)), HttpStatus.OK); 
		
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
