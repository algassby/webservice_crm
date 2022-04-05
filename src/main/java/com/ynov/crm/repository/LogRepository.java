/**
 * 
 */
package com.ynov.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ynov.crm.enties.Log;

/**
 * @author algas
 *
 */
@Repository
public interface LogRepository extends JpaRepository<Log, String> {
	  @Query(value = "select l from Log l where l.username =:username")
	  List<Log> findAllByUsername(String username);

}
