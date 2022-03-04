package com.ynov.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ynov.crm.enties.Appointment;
import com.ynov.crm.enties.Organization;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String>{
	public boolean existsById(String name);
}
