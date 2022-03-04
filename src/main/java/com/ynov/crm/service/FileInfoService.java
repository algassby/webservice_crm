/**
 * 
 */
package com.ynov.crm.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.ynov.crm.enties.Customer;
import com.ynov.crm.enties.FileInfo;


/**
 * @author algas
 *
 */
public interface FileInfoService {
	public void init();
    public FileInfo save(MultipartFile file);
    public byte[] getPhoto(String fileId)throws Exception;
    public String deleteFile(String fileName);
    public Resource load(String filename);
    public void deleteAll();
}
