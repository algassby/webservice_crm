package com.ynov.crm.service;

import org.springframework.http.ResponseEntity;

import com.ynov.crm.requestdto.AppointmentRequestDto;
import com.ynov.crm.responsedto.AppointmentResponseDto;


public interface AppointmentService {
	
	public AppointmentResponseDto save(AppointmentRequestDto appointmentRequestDto);
	public ResponseEntity<Object> update(AppointmentRequestDto appointmentRequestDto,
			String apointmentId);
	public ResponseEntity<Object>  remove(String appointmentId);
	public ResponseEntity<Object> getAppointment(String appointmentId);
	public ResponseEntity<Object> getAppointmentByCustommer(String custommerId, int page);

}
