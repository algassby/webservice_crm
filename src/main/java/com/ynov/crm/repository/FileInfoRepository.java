package com.ynov.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ynov.crm.enties.FileInfo;

/**
 * @author algas
 *
 */
public interface FileInfoRepository extends JpaRepository<FileInfo, String>{

	public FileInfo findByFileName(String fileName);
}