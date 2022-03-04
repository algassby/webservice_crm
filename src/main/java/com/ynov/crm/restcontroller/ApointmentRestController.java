package com.ynov.crm.restcontroller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ynov.crm.requestdto.AppointmentRequestDto;
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
		//TODO tester les champs surtout date
		
		return new ResponseEntity<>(appointmentService.save(appointmentRequestDto),HttpStatus.CREATED);
	}
	
	@GetMapping("/find/{appointmentId}")
	public ResponseEntity<?> findAppointmentById(@Valid @PathVariable String appointmentId) {

		if(appointmentService.getAppointment(appointmentId)!=null) {
			return new ResponseEntity<>(appointmentService.getAppointment(appointmentId),HttpStatus.FOUND);
		}else {
			return new ResponseEntity<>("This appointment is unknow.",HttpStatus.NOT_FOUND);
		}

	}

}
