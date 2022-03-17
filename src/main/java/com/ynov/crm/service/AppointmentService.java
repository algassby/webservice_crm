package com.ynov.crm.service;

import org.springframework.http.ResponseEntity;

import com.ynov.crm.requestdto.AppointmentRequestDto;


public interface AppointmentService {
	
	public ResponseEntity<Object> save(AppointmentRequestDto appointmentRequestDto);
	public ResponseEntity<Object> update(AppointmentRequestDto appointmentRequestDto,
			String apointmentId);
	public ResponseEntity<Object>  remove(String appointmentId);
	public ResponseEntity<Object> getAppointment(String appointmentId);
	public ResponseEntity<Object> getAppointmentByCustommer(String custommerId, int page);

}
