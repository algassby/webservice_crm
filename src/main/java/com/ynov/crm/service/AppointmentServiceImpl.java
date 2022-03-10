package com.ynov.crm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynov.crm.enties.Appointment;
import com.ynov.crm.enties.Customer;
import com.ynov.crm.mapper.AppointmentMapper;
import com.ynov.crm.mapper.CustomerMapper;
import com.ynov.crm.mapper.OrganisationMapper;
import com.ynov.crm.repository.AppUserRepository;
import com.ynov.crm.repository.AppointmentRepository;
import com.ynov.crm.repository.CustomerRepository;
import com.ynov.crm.repository.OrganizationRepository;
import com.ynov.crm.requestdto.AppointmentRequestDto;
import com.ynov.crm.responsedto.AppointmentResponseDto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
@Service
@Transactional
@Data
@Slf4j
public class AppointmentServiceImpl implements AppointmentService{
	
	private AppointmentRepository appointmentRepository;
	private CustomerRepository customerRepository;
	private AppointmentMapper appointmentMapper;
	
	@Autowired
	public AppointmentServiceImpl(CustomerRepository customerRepository,
			AppointmentRepository appointmentRepository,
			AppointmentMapper appointmentMapper) {
		super();
		this.appointmentRepository = appointmentRepository;
		this.customerRepository = customerRepository;
		this.appointmentMapper = appointmentMapper;
	}
	
	@Override
	public AppointmentResponseDto save(AppointmentRequestDto appointmentRequestDto) {
		Appointment appointment = appointmentMapper.appointmentRequestDtoToAppointment(appointmentRequestDto);
		if(appointmentRequestDto.getCustomerId()!=null) {
			appointment.setCustomer(customerRepository.findById(appointmentRequestDto.getCustomerId()).get());
		}else {
			log.error("customer not found");
		}
		 
		return appointmentMapper.appointmentToApointmentResponseDto(appointmentRepository.save(appointment));
			
	}
	@Override
	public AppointmentResponseDto update(AppointmentRequestDto appointmentRequestDto, String apointmentId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String remove(String apointmentId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AppointmentResponseDto getAppointment(String appointmentId) {
		return appointmentMapper.appointmentToApointmentResponseDto(appointmentRepository.getById(appointmentId));
	}

	@Override 
	public List<AppointmentResponseDto> getAppointmentByCustommer(String custommerId,int page) {
		final int size = 10;
		Pageable paging = PageRequest.of(page, size);
		Page<Appointment> pagedResult = appointmentRepository.findAll(paging);
		if(pagedResult.hasContent()) {
			return pagedResult
					.stream()
					.map(appointment->appointmentMapper.appointmentToApointmentResponseDto(appointment))
					.collect(Collectors.toList());
		}
		else {
			return new ArrayList<>();
		}
	}
	
	

}
