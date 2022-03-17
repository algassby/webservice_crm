package com.ynov.crm.mapper;

import org.mapstruct.Mapper;

import com.ynov.crm.enties.Appointment;
import com.ynov.crm.requestdto.AppointmentRequestDto;
import com.ynov.crm.responsedto.AppointmentResponseDto;

@Mapper(componentModel = "spring") 
public class AppointmentMapperImpl implements AppointmentMapper {

    @Override
    public AppointmentResponseDto appointmentToApointmentResponseDto(Appointment appointment) {
        if ( appointment == null ) {
            return null;
        }

        AppointmentResponseDto appointmentResponseDto = new AppointmentResponseDto();

        appointmentResponseDto.setApointId( appointment.getApointId() );
        appointmentResponseDto.setLabel( appointment.getLabel() );
        if ( appointment.getDate() != null ) {
            appointmentResponseDto.setDate(appointment.getDate());
        }
        appointmentResponseDto.setCustomerId(appointment.getCustomer().getCustomerId());
        appointmentResponseDto.setPlace(appointment.getPlace());
        return appointmentResponseDto;
    }

    @Override
    public Appointment appointmentRequestDtoToAppointment(AppointmentRequestDto appointmentRequestDto) {
        if ( appointmentRequestDto == null ) {
            return null;
        }

        Appointment appointment = new Appointment();

        appointment.setLabel( appointmentRequestDto.getLabel() );
        appointment.setPlace(appointmentRequestDto.getPlace());
        if ( appointmentRequestDto.getDate() != null ) {
            appointment.setDate(appointmentRequestDto.getDate());
        }

        return appointment;
    }
}

