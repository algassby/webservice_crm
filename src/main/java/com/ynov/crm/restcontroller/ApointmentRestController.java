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

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
@RestController
@RequestMapping("/api/appointment")
@Slf4j
@Tag(name = "Appointment", description = "Manage the customer appointment")
public class ApointmentRestController {

	@Autowired
	private AppointmentService appointmentService;

	@PostMapping("/save")
	public ResponseEntity<?> saveAppointment(@RequestBody AppointmentRequestDto appointmentRequestDto) {
		return appointmentService.save(appointmentRequestDto);
	}

	@PutMapping("/update/{appointmentId}")
	public ResponseEntity<?> update(@RequestBody AppointmentRequestDto appointmentRequestDto,
			@PathVariable String appointmentId) {
		return appointmentService.update(appointmentRequestDto, appointmentId);
	}

	@DeleteMapping("/delete/{appointmentId}")
	public ResponseEntity<?> update(@Valid @PathVariable String appointmentId) {
		return appointmentService.remove(appointmentId);

	}

	@GetMapping("/find/{appointmentId}")
	public ResponseEntity<?> findAppointmentById(@Valid @PathVariable String appointmentId) {
		return appointmentService.getAppointment(appointmentId);
	}

	@GetMapping("/find/customer/{customerId}")
	public ResponseEntity<?> findAppointmentByCustomer(@Valid @PathVariable String customerId,@Valid @RequestParam int page) {
		return appointmentService.getAppointmentByCustommer(customerId, page);
				
	}

}
