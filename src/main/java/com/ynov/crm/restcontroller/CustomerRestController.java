/**
 * 
 */
package com.ynov.crm.restcontroller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ynov.crm.requestdto.CustomerRequestDto;
import com.ynov.crm.responsedto.CustomerResponseDto;
import com.ynov.crm.service.CustomerService;

import lombok.Data;

/**
 * @author algas
 *
 */
@RestController
@RequestMapping("/api/customers")
@Data
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
		return new ResponseEntity<>(customerService.getCustomer(customerId), HttpStatus.OK); 
		
	}
	@PostMapping("/save")
	public ResponseEntity<?> save(@RequestBody CustomerRequestDto customerRequestDto) {
		return new ResponseEntity<>(customerService.save(customerRequestDto), HttpStatus.OK); 
		
	}
	
	@PutMapping("/update/{customerId}")
	public ResponseEntity<?> update(@RequestBody CustomerRequestDto customerRequestDto, @PathVariable String customerId) {
		return new ResponseEntity<>(customerService.update(customerRequestDto, customerId), HttpStatus.OK); 
		
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> update(@RequestParam(value = "customerId") String customerId) {
		return new ResponseEntity<>(customerService.delete(customerId), HttpStatus.OK); 
		
	}
	
	
}
