package com.ynov.crm.mapper;

import org.mapstruct.Mapper;

import com.ynov.crm.enties.Appointment;
import com.ynov.crm.requestdto.AppointmentRequestDto;
import com.ynov.crm.responsedto.AppointmentResponseDto;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
	public AppointmentResponseDto appointmentToApointmentResponseDto(Appointment  appointment);
	public Appointment appointmentRequestDtoToAppointment (AppointmentRequestDto appointmentRequestDto);


}
