package com.ynov.crm.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynov.crm.enties.Appointment;
import com.ynov.crm.mapper.AppointmentMapper;
import com.ynov.crm.repository.AppointmentRepository;
import com.ynov.crm.repository.CustomerRepository;
import com.ynov.crm.requestdto.AppointmentRequestDto;
import com.ynov.crm.requestdto.verifRequestDto.VerifAppointment;
import com.ynov.crm.responsedto.AppointmentResponseDto;
import com.ynov.crm.responsedto.ResponseMessage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Data
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

	private AppointmentRepository appointmentRepository;
	private CustomerRepository customerRepository;
	private AppointmentMapper appointmentMapper;

	@Autowired
	public AppointmentServiceImpl(CustomerRepository customerRepository, AppointmentRepository appointmentRepository,
			AppointmentMapper appointmentMapper) {
		super();
		this.appointmentRepository = appointmentRepository;
		this.customerRepository = customerRepository;
		this.appointmentMapper = appointmentMapper;
	}

	@Override
	public ResponseEntity<Object> save(AppointmentRequestDto appointmentRequestDto) {
		// TODO tester champ date
		String resultVerif = VerifAppointment.verifChampAppointment(appointmentRequestDto);
		if (resultVerif != null) {
			return new ResponseEntity<>(resultVerif, HttpStatus.NOT_ACCEPTABLE);
		}

		Appointment appointment = appointmentMapper.appointmentRequestDtoToAppointment(appointmentRequestDto);

		if (customerRepository.existsById(appointmentRequestDto.getCustomerId())) {
			appointment.setCustomer(customerRepository.findById(appointmentRequestDto.getCustomerId()).get());

			return new ResponseEntity<>(
					appointmentMapper.appointmentToApointmentResponseDto(appointmentRepository.save(appointment)),
					HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("customer id not found", HttpStatus.BAD_REQUEST);

		}

	}

	@Override
	public ResponseEntity<Object> update(AppointmentRequestDto appointmentRequestDto, String appointmentId) {
		// TODO verif champ date
		String resultVerif = VerifAppointment.verifChampAppointment(appointmentRequestDto);
		if (resultVerif != null) {
			return new ResponseEntity<>(resultVerif, HttpStatus.NOT_ACCEPTABLE);
		}
		if (appointmentRepository.existsById(appointmentId)) {

			Appointment appointment = appointmentRepository.findById(appointmentId).get();
			if (customerRepository.existsById(appointmentRequestDto.getCustomerId())) {
				appointment.setCustomer(customerRepository.findById(appointmentRequestDto.getCustomerId()).get());

				appointment.setDate(appointmentRequestDto.getDate()).setLabel(appointmentRequestDto.getLabel());
				appointmentRepository.save(appointment);
				return new ResponseEntity<>(new ResponseMessage("appointment modify"), HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>("customer id not found", HttpStatus.NOT_ACCEPTABLE);
			}

		} else {
			return new ResponseEntity<>(new ResponseMessage("appointment id not found "), HttpStatus.BAD_REQUEST);
		}

	}

	@Override
	public ResponseEntity<Object> remove(String appointmentId) {
		log.info("appointmentId found : {}", appointmentRepository.existsById(appointmentId));

		if (appointmentRepository.existsById(appointmentId)) {

			appointmentRepository.deleteById(appointmentId);
			return new ResponseEntity<>(new ResponseMessage("appointment delete."), HttpStatus.OK);

		} else {
			return new ResponseEntity<>(new ResponseMessage("unknow id appointment."), HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<Object> getAppointment(String appointmentId) {
		if (appointmentRepository.existsById(appointmentId)) {
			return new ResponseEntity<>(
					appointmentMapper.appointmentToApointmentResponseDto(appointmentRepository.getById(appointmentId)),
					HttpStatus.FOUND);
		} else {
			return new ResponseEntity<>(new ResponseMessage("unknow id appointment."), HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<Object> getAppointmentByCustommer(String custommerId, int page) {
		log.info("customer found : {}", customerRepository.existsById(custommerId));
		if (customerRepository.existsById(custommerId)) {
			final int size = 10;
			Pageable paging = PageRequest.of(page, size);
			Page<Appointment> pagedResult = appointmentRepository.findAll(paging);
			if (pagedResult.hasContent()) {
				List<AppointmentResponseDto> returnPage = pagedResult.stream()
						.map(appointment -> appointmentMapper.appointmentToApointmentResponseDto(appointment))
						.collect(Collectors.toList());
				return new ResponseEntity<>(returnPage, HttpStatus.FOUND);
			} else {
				return new ResponseEntity<>(new ResponseMessage("unknow page."), HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>(new ResponseMessage("customer id unknow."), HttpStatus.BAD_REQUEST);
		}
	}

}
