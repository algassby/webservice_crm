package com.ynov.crm.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.ynov.crm.enties.Organization;


@Repository
public interface OrganizationRepository extends JpaRepository<Organization, String>{
//	//@Query(value = "select * from organization", nativeQuery = true)
//	public List<Organization> findAll();
	public Boolean existsByName(String name);
	public Organization findByName(String name);
}
