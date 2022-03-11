/**
 * 
 */
package com.ynov.crm.service;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.ynov.crm.enties.FileInfo;
import com.ynov.crm.responsedto.FileInfoResponseDto;


/**
 * @author algas
 *
 */
public interface FileInfoService {
	public void init();
    public FileInfo save(MultipartFile file, String customerId);
    public FileInfo getFile(String fileId);
    public Boolean existsByFileName(String fileName);
    public byte[] getPhoto(String fileId)throws Exception;
    public String deleteFile(String fileName);
    public String deleteFileWithUser(String fileName);
    public Resource load(String filename);
    public Stream<Path> loadAll();
    public void deleteAll();
    public List<FileInfoResponseDto> findAllFile();
    public List<FileInfoResponseDto> findAllFileByCustomer(String customerId);
}
