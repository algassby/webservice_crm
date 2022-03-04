package com.ynov.crm.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynov.crm.mapper.CustomerMapper;
import com.ynov.crm.repository.CustomerRepository;
import com.ynov.crm.repository.OrganizationRepository;
import com.ynov.crm.requestdto.AppointmentRequestDto;
import com.ynov.crm.responsedto.AppointmentResponseDto;

import lombok.Data;


public interface AppointmentService {
	
	public AppointmentResponseDto save(AppointmentRequestDto appointmentRequestDto);
	public AppointmentResponseDto update(AppointmentRequestDto appointmentRequestDto,
			String apointmentId);
	public String remove(String apointmentId);
	public AppointmentResponseDto getAppointment(String appointmentId);
	public List<AppointmentResponseDto> getAppointmentByCustommer(String custommerId);

}
