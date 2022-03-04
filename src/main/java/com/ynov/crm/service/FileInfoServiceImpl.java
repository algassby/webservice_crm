/**
 * 
 */
package com.ynov.crm.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ynov.crm.enties.Customer;
import com.ynov.crm.enties.FileInfo;
import com.ynov.crm.exception.FileUploadException;
import com.ynov.crm.repository.FileInfoRepository;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
/**
 * @author algas
 *
 */
@Service
@Transactional
@Getter
@Setter
@Slf4j
public class FileInfoServiceImpl implements FileInfoService {

	private FileInfoRepository fileInfoRepository;
	
	/**
	 * @param fileInfoRepository
	 */
	@Autowired
	public FileInfoServiceImpl(FileInfoRepository fileInfoRepository) {
		super();
		this.fileInfoRepository = fileInfoRepository;
	}
	@Autowired 
	ServletContext context;

    private final Path root = Paths.get("uploads");

    @Override
    public void init() {
    	
    	if(Files.notExists(root)) {
    		try {
    	        Files.createDirectory(root);
    	      } catch (IOException e) {
    	        throw new RuntimeException("Could not initialize folder for upload!");
    	      }
    	}
      
    }
    
    private Logger logger = LoggerFactory.getLogger(FileInfoServiceImpl.class);
    public FileInfo save(MultipartFile file) throws FileUploadException {
    	
        try {
        	FileInfo fileInfo = new FileInfo();
            Path resolve = root.resolve(file.getOriginalFilename());
         	
            
            
            logger.info("resolve: "+resolve.toString());
            logger.info("route: "+root);
            logger.info("file: "+root.toFile());
            if (resolve.toFile()
                       .exists()) {
                throw new FileUploadException("File already exists: " + file.getOriginalFilename());
            }
            logger.info(resolve.toString());

            fileInfo.setFileName(resolve.getFileName().toString());
        	fileInfo.setFileUrl(resolve.toString());
        	fileInfo.setLastUpdate(new Date());
        	//fileInfo.setCustomer(customer);
        	fileInfoRepository.save(fileInfo);
        	logger.info(fileInfo.toString());
            Files.copy(file.getInputStream(), resolve);
            return fileInfo;
        } catch (Exception e) {
            throw new FileUploadException("Could not store the file. Error: " + e.getMessage());
        }
    }
    public byte[] getPhoto(String fileId) throws Exception{
    	FileInfo fileInfo = null;
    	
    	if(fileInfoRepository.existsById(fileId)) {
    		 fileInfo   = fileInfoRepository.findById(fileId).get();
    		 
    	}
    	String encodeImage = Base64.getEncoder().withoutPadding().encodeToString(Files.readAllBytes(Paths.get(String.join("",root.getRoot().toString(),new StringBuffer().append(File.separatorChar).append(fileInfo.getFileName()))))); 
    	
		return Files.readAllBytes(Paths.get(String.join("",root.getRoot().toString(),new StringBuffer().append(File.separatorChar).append(fileInfo.getFileName()))));
		
    	
	 }
    @Override
    public Resource load(String filename) {
      try {
        Path file = root.resolve(filename);
        Resource resource = new UrlResource(file.toUri());
        if (resource.exists() || resource.isReadable()) {
          return resource;
        } else {
          throw new RuntimeException("Could not read the file!");
        }
      } catch (MalformedURLException e) {
        throw new RuntimeException("Error: " + e.getMessage());
      }
    }
	@Override
	public String deleteFile(String fileName) {
		log.info(root.toString());
		 Path path = Paths.get(String.join("", root.toString(),new StringBuilder().append(File.separatorChar).append(fileName).toString()));
		
		 logger.info(String.join(" "," Path =", path.toString()));
	        try {
	            // Delete file or directory
	        	if(!fileName.isEmpty() || fileName!=null) {
	        		fileInfoRepository.deleteById(fileInfoRepository.findByFileName(fileName).getFileId());
	        		Files.delete(path);
	        		
	        	}
	            
	        } catch (NoSuchFileException ex) {
	            System.out.printf("No such file or directory: %s\n", path);
	        } catch (DirectoryNotEmptyException ex) {
	            System.out.printf("Directory %s is not empty\n", path);
	        } catch (IOException ex) {
	            System.out.println(ex);
	        }
			return String.join(" ","Suppression de l'image", fileName, "reussie avec succès!");
	}
	
	 @Override
	  public void deleteAll() {
	    FileSystemUtils.deleteRecursively(root.toFile());
	  }
	 
		@Override
		public Boolean existsByFileName(String fileName) {
			return  fileInfoRepository.existsByFileName(fileName);
		}

	 
}