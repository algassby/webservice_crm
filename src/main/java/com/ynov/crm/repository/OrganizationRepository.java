package com.ynov.crm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.ynov.crm.enties.Organization;


@Repository
public interface OrganizationRepository extends JpaRepository<Organization, String>{
    @Query(value = "select o from Organization o left join o.appUser u where u.userId =:userId")
	public Page<Organization> findAllByAdminId(String userId, Pageable pageable);
	public Boolean existsByName(String name);
	public Organization findByName(String name);
}
