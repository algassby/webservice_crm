package com.ynov.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ynov.crm.enties.FileInfo;
import com.ynov.crm.responsedto.FileInfoResponseDto;

/**
 * @author algas
 *
 */
public interface FileInfoRepository extends JpaRepository<FileInfo, String>{

	//public List<FileInfoResponseDto> findAllFile();
	@Query("select f from FileInfo f join fetch f.customer c where c.customerId =:customerId")
	public List<FileInfo> findAllFileByCustomer(String customerId);
	public FileInfo findByFileName(String fileName);
	public Boolean existsByFileName(String customerId);
	
}