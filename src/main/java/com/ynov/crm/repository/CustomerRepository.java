/**
 * 
 */
package com.ynov.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ynov.crm.enties.Customer;

/**
 * @author algas
 *
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
// @Query(value = "select * from Customers c join file_info f on c.customer_id = f.customer_id", nativeQuery = true)
// List<Customer> findAll();
	
}
