package com.ynov.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ynov.crm.enties.FileInfo;


/**
 * @author algas
 *
 */
@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, String>{


	@Query("select f from FileInfo f join fetch f.customer c where c.customerId =:customerId")
	public List<FileInfo> findAllFileByCustomer(String customerId);
	public FileInfo findByFileName(String fileName);
	public Boolean existsByFileName(String fileName);
	 @Modifying(clearAutomatically = true, flushAutomatically = true)
	 @Query(value = "DELETE FROM FileInfo f WHERE f.fileId = ?1")
	 public void deleteById(String fileId);
	 @Modifying(clearAutomatically = true, flushAutomatically = true)
	 @Query(value = "DELETE FROM FileInfo")
	 public void removeAll();
	
}