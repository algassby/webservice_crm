package com.ynov.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ynov.crm.enties.Appointment;
import com.ynov.crm.enties.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{

}
