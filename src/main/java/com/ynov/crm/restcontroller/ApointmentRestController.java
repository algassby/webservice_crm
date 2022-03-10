package com.ynov.crm.restcontroller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.ynov.crm.requestdto.AppointmentRequestDto;
import com.ynov.crm.requestdto.CustomerRequestDto;
import com.ynov.crm.service.AppointmentService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
@RestController
@RequestMapping("/api/appointment")
@Slf4j
public class ApointmentRestController {

	@Autowired
	private AppointmentService appointmentService;

	@PostMapping("/save")
	public ResponseEntity<?> saveAppointment(@Valid @RequestBody AppointmentRequestDto appointmentRequestDto) {
		// TODO tester les champs surtout date

		return new ResponseEntity<>(appointmentService.save(appointmentRequestDto), HttpStatus.CREATED);
	}
//	//TODO à faire
//	@PutMapping("/update/{customerId}")
//	public ResponseEntity<?> update(@RequestBody CustomerRequestDto customerRequestDto, @PathVariable String customerId) {
//		return new ResponseEntity<>(customerService.update(customerRequestDto, customerId), HttpStatus.OK); 
//		
//	}
//	
//	//TODO à faire
//	@DeleteMapping("/delete")
//	public ResponseEntity<?> update(@RequestParam(value = "customerId") String customerId) {
//		return new ResponseEntity<>(customerService.delete(customerId), HttpStatus.OK); 
//		
//	}

	@GetMapping("/find/{appointmentId}")
	public ResponseEntity<?> findAppointmentById(@Valid @PathVariable String appointmentId) {

		if (appointmentService.getAppointment(appointmentId) != null) {
			return new ResponseEntity<>(appointmentService.getAppointment(appointmentId), HttpStatus.FOUND);
		} else {
			return new ResponseEntity<>("This appointment is unknow.", HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/find/customer/{customerId}/{page}")
	public ResponseEntity<?> findAppointmentByCustomer(@Valid @PathVariable String customerId, @PathVariable int page) {

		if (appointmentService.getAppointment(customerId) != null) {
			return new ResponseEntity<>(appointmentService.getAppointmentByCustommer(customerId, page),
					HttpStatus.FOUND);
		} else {
			return new ResponseEntity<>("This customer is unknow.", HttpStatus.NOT_FOUND);
		}

	}
	
	

}
