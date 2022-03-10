/**
 * 
 */
package com.ynov.crm.restcontroller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ynov.crm.requestdto.CustomerRequestDto;
import com.ynov.crm.requestdto.JsonObjectDto;
import com.ynov.crm.responsedto.ResponseMessage;
import com.ynov.crm.service.CustomerService;

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
	public CustomerRestController(CustomerService customerService) {
		super();
		this.customerService = customerService;
	}
	
	@GetMapping
	public ResponseEntity<?> getAllCustomer(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10")
	Integer pageSize, @RequestParam(defaultValue = "lastName")  String sortBy) {
		return new ResponseEntity<>(customerService.getAllCustomer(pageNo, pageSize, sortBy), HttpStatus.OK);
		
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
	public ResponseEntity<?> save(@RequestBody CustomerRequestDto customerRequestDto) {
		return new ResponseEntity<>(customerService.save(customerRequestDto), HttpStatus.OK); 
		
	}
	
	@PutMapping("/update/{customerId}")
	public ResponseEntity<?> update(@RequestBody CustomerRequestDto customerRequestDto, @PathVariable String customerId) {
		if(customerId ==null) {
			return new ResponseEntity<>(new ResponseMessage("User not found"), HttpStatus. NOT_FOUND); 
		}
		if(customerService.existsById(customerId)) {
			
			return new ResponseEntity<>(customerService.update(customerRequestDto, customerId), HttpStatus.OK); 
		}
		return new ResponseEntity<>(new ResponseMessage("Cannot  Updated User, because doest not exists!"), HttpStatus.BAD_REQUEST); 
		
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> update(@RequestParam(value = "customerId") String customerId) {
		return new ResponseEntity<>(customerService.delete(customerId), HttpStatus.OK); 
		
	}
	@PutMapping("/update/addImage/{customerId}")
	public ResponseEntity<?> addImageToCustomer(@PathVariable  String customerId, @RequestParam(value = "file") MultipartFile file ) {
		
		return new ResponseEntity<>(customerService.addImageToCustomer(customerId, file), HttpStatus.OK) ;
		
	}
	@PutMapping("/update/addManyImage/{customerId}")
	public ResponseEntity<?> addManyImage(@PathVariable  String customerId, @RequestParam(value = "files") MultipartFile files []) {
		if(files.length==0) {
			return new ResponseEntity<>(new ResponseMessage("Cannot  Upload images to customer, cause file is empty!"), HttpStatus.BAD_REQUEST); 
		}
		return new ResponseEntity<>(customerService.addAllImageToCustomer(customerId, files), HttpStatus.OK); 
		
	}
	
	
	@PutMapping("/update/deleteManyImage/{customerId}")
	public ResponseEntity<?> removeManyImageToCustomer(@PathVariable  String customerId, @RequestBody JsonObjectDto jsonRequestDto) {
		if(jsonRequestDto.getImages().isEmpty()) {
			return new ResponseEntity<>(new ResponseMessage("Cannot  delete images from customer, cause files list is empty!"), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(customerService.removeManyImageToCustomer(customerId, jsonRequestDto), HttpStatus.OK); 
		
	}
	
	@PutMapping("/update/removeImage/{customerId}")
	public ResponseEntity<?> addImageToCustomer(@PathVariable  String customerId, @RequestParam(value = "imageName") String imageName) {
		
		return new ResponseEntity<>(customerService.removeImageToCustomer(customerId, imageName), HttpStatus.OK); 
		
	}
	
	
}
