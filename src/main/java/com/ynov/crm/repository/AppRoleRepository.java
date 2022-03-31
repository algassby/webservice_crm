/**
 * 
 */
package com.ynov.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ynov.crm.enties.AppRole;

/**
 * @author algas
 *
 */
@Repository
public interface AppRoleRepository extends JpaRepository<AppRole, Integer> {
	Boolean existsByRoleName(String roleName);
	AppRole findByRoleName(String roleName);
}
