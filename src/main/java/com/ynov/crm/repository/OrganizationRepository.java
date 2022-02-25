package com.ynov.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ynov.crm.enties.Organization;


@Repository
public interface OrganizationRepository extends JpaRepository<Organization, String>{
	public Boolean existsByName(String name);
	public Organization findByName(String name);
}
