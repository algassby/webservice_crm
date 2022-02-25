/**
 * 
 */
package com.ynov.crm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.ynov.crm.enties.AppUser;

/**
 * @author algas
 *
 */
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, String> {
	AppUser findByEmail(String email);
	Optional<AppUser> findByUsername(String username);
	Boolean existsByEmail(String email);
	Boolean existsByUsername(String email);

}
