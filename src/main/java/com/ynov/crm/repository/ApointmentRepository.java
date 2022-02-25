package com.ynov.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ynov.crm.enties.Appointment;

public interface ApointmentRepository extends JpaRepository<Appointment, Integer>{

}
