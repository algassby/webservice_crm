package com.ynov.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ynov.crm.enties.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String>{
	public boolean existsById(String name);
	public List<Appointment> findByCustomer(String customer);
}
