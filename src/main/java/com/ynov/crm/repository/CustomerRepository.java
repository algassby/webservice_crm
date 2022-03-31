/**
 * 
 */
package com.ynov.crm.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import com.ynov.crm.enties.Customer;


/**
 * @author algas
 *
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
// @Query(value = "select c.first_name, c.last_name, c.phone_numer from customers c where c.customer_id = :customerId", nativeQuery = true)
// Optional<Customer> findById(@Param(value = "customerId") String customerId);
 
 //@Query(value = "select customer_id, first_name, last_name, phone_numer from customers", nativeQuery = true)
 List<Customer> findAll();
 
 @Modifying(clearAutomatically = true, flushAutomatically = true)
 @Query(value = "DELETE FROM Customer c WHERE customer_id = ?1")
 public void deleteById(String customerId);
 
 @Query("SELECT customer FROM Customer customer  JOIN FETCH customer.organization org where org.orgaId = :organizationId")
 List<Customer> findByOrganization(String organizationId);
 
	
}
