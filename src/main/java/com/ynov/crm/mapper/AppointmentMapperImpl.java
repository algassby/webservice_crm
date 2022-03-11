package com.ynov.crm.mapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

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
            appointmentResponseDto.setDate( Date.from( appointment.getDate().toInstant( ZoneOffset.UTC ) ) );
        }
        appointmentResponseDto.setCustomerId(appointment.getCustomer().getCustomerId());
        return appointmentResponseDto;
    }

    @Override
    public Appointment appointmentRequestDtoToAppointment(AppointmentRequestDto appointmentRequestDto) {
        if ( appointmentRequestDto == null ) {
            return null;
        }

        Appointment appointment = new Appointment();

        appointment.setLabel( appointmentRequestDto.getLabel() );
        if ( appointmentRequestDto.getDate() != null ) {
            appointment.setDate( LocalDateTime.ofInstant( appointmentRequestDto.getDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }

        return appointment;
    }
}
