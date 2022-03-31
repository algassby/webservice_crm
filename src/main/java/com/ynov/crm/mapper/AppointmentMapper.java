package com.ynov.crm.mapper;

import com.ynov.crm.enties.Appointment;
import com.ynov.crm.requestdto.AppointmentRequestDto;
import com.ynov.crm.responsedto.AppointmentResponseDto;

public interface AppointmentMapper {
	public AppointmentResponseDto appointmentToApointmentResponseDto(Appointment  appointment);
	public Appointment appointmentRequestDtoToAppointment (AppointmentRequestDto appointmentRequestDto);


}
