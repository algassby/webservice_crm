package com.ynov.crm.requestdto.verifRequestDto;



import java.util.Set; 

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.ynov.crm.requestdto.AppUserRequestDto;
import com.ynov.crm.requestdto.AppointmentRequestDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VerifAppointment {
	static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	static Validator validator = factory.getValidator();
	
	public static String verifChampAppointment(AppointmentRequestDto appointmentRequestDto) {
		
		Set<ConstraintViolation<AppointmentRequestDto>> violations = validator.validate(appointmentRequestDto);
		for (ConstraintViolation<AppointmentRequestDto> violation : violations) {
			log.error("erreur : {}",violation.getMessageTemplate());
			if (!violations.isEmpty()) {
				return violation.getMessage();
			}
		}
		
		
		return null;
		
	}
public static String verifChampAppUserRequestDto(AppUserRequestDto appUserRequestDto) {
		
		Set<ConstraintViolation<AppUserRequestDto>> violations = validator.validate(appUserRequestDto);
		for (ConstraintViolation<AppUserRequestDto> violation : violations) {
			log.error("erreur : {}",violation.getMessageTemplate());
			if (!violations.isEmpty()) {
				return violation.getMessage();
			}
		}
		
		
		return null;
		
	}

}
