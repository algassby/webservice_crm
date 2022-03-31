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
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletContext;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ynov.crm.enties.AppUser;
import com.ynov.crm.enties.Customer;
import com.ynov.crm.enties.FileInfo;
import com.ynov.crm.exception.FileUploadException;
import com.ynov.crm.mapper.FileInfoMapper;
import com.ynov.crm.repository.CustomerRepository;
import com.ynov.crm.repository.FileInfoRepository;
import com.ynov.crm.responsedto.FileInfoResponseDto;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
	private FileInfoMapper fileInfoMapper;
	private CustomerRepository customerRepo;
	private final Path root = Paths.get("uploads");
	
	/**
	 * @param fileInfoRepository
	 */
	@Autowired
	public FileInfoServiceImpl(FileInfoRepository fileInfoRepository, FileInfoMapper fileInfoMapper, CustomerRepository customerRepo) {
		super();
		this.fileInfoRepository = fileInfoRepository;
		this.fileInfoMapper  = fileInfoMapper;
		this.customerRepo = customerRepo;
	}
   

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
    public FileInfo save(MultipartFile file, String customerId) throws FileUploadException {
    	Customer customer = customerRepo.findById(customerId).get();
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
        	fileInfo.setCustomer(customer);
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
	  public Stream<Path> loadAll() {
	    try {
	    	logger.info(Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize).toString());
	      return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
	    } catch (IOException e) {
	      throw new RuntimeException("Could not load the files!");
	    }
	  }
	@Override
	public String deleteFile(String fileName, String customerId) {
		log.info(root.toString());
		 Path path = Paths.get(String.join("", root.toString(),new StringBuilder().append(File.separatorChar).append(fileName).toString()));
		
		 Path rootUserDirectory = Paths.get(new StringBuilder().append(this.root.toString()).append(File.separatorChar).append(customerId).append(File.separatorChar).append(fileName).toString());
		 log.info(rootUserDirectory.toString());
		 if (rootUserDirectory.toFile().exists()) {
			   logger.debug(String.join(" "," Path =", rootUserDirectory.toString()));
		        try {
		            // Delete file or directory
		        		Files.delete(rootUserDirectory);

		        } catch (NoSuchFileException ex) {
		            log.info("No such file or directory: %s\n", path);
		        } catch (DirectoryNotEmptyException ex) {
		        	log.info("Directory %s is not empty\n", path);
		        } catch (IOException ex) {
		        	log.info(ex.toString());
		        }
				return String.join(" ","Suppression de l'image", fileName, "reussie avec succès!");
		   }
		   return String.join(" ","Image doest not exists", fileName);
		   
		 
	}
	@Override
	public String deleteFileWithUser(String fileName) {
		log.info(root.toString());
		 Path path = Paths.get(String.join("", root.toString(),new StringBuilder().append(File.separatorChar).append(fileName).toString()));
	    if (path.toFile().exists()) {
	    	logger.info(String.join(" "," Path =", path.toString()));
	        try {
	            // Delete file or directory
	        	FileInfo fileInfo = fileInfoRepository.findByFileName(fileName);
	        		fileInfo.setCustomer(null);
	        		fileInfoRepository.deleteById(fileInfo.getFileId());
	        		Files.delete(path);
	        		
	        } catch (NoSuchFileException ex) {
	            System.out.printf("No such file or directory: %s\n", path);
	        } catch (DirectoryNotEmptyException ex) {
	            System.out.printf("Directory %s is not empty\n", path);
	        } catch (IOException ex) {
	            System.out.println(ex);
	        }
			return String.join(" ","Suppression de l'image", fileName, "reussie avec succès!");
	    }
	    return String.join(" ","the image doest not exist", fileName);
		 
	}
	@Override
	public String deleteCustomerDirectory(String customerId) {
		Path path = Paths.get(String.join("", root.toString(),new StringBuilder().append(File.separatorChar).append(customerId).toString()));

		log.info(path.toString());
		  if (path.toFile().exists()) {
			  this.fileInfoRepository.findAll().stream().forEach(image->{
				  image.setCustomer(null);
				  fileInfoRepository.deleteById(image.getFileId());
			  });
			  //fileInfoRepository.deleteAll();
			  
			 fileInfoRepository.removeAll();
				
				//fileInfoRepository.deleteById(fileInfo.getFileId());

				try {
					Files.walk(path)
					  .sorted(Comparator.reverseOrder())
					  .map(Path::toFile)
					  .forEach(File::delete);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "user delete directory";
		  }
		  return "failed delete user directory";
		
//		FileSystemUtils.deleteRecursively(root.toFile());
	}
	
	 @Override
	  public void deleteAll() {
	    FileSystemUtils.deleteRecursively(root.toFile());
	  }
	 
		@Override
		public Boolean existsByFileName(String fileName) {
			return  fileInfoRepository.existsByFileName(fileName);
		}
		@Override
		public List<FileInfoResponseDto> findAllFile(Integer pageNo, Integer pageSize,
				String sortBy) {
			Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
			 
	        Page<FileInfo> pagedResult = fileInfoRepository.findAll(paging);
			return pagedResult.getContent()
					.stream()
					.map(image->{
						
						FileInfoResponseDto fileInfoDto  = fileInfoMapper.fileInfoToFileInfoResponseDto(image);
						
						return fileInfoDto;
					})
					.collect(Collectors.toList());
		}
		@Override
		public List<FileInfoResponseDto> findAllFileByCustomer(String customerId) {
			
			 return fileInfoRepository.findAllFileByCustomer(customerId)
					 .stream()
					 .map(image->fileInfoMapper.fileInfoToFileInfoResponseDto(image))
					 .collect(Collectors.toList());
				
		}
		@Override
		public FileInfo getFile(String fileId) {
			
			return fileInfoRepository.findById(fileId).get();
		}
		@Override
		public FileInfo uploadToLocalFileSystem(String customerId, MultipartFile file) {
		
			FileInfo fileInfo = null;
			if(fileInfoRepository.existsByFileName(String.join("_", customerId, file.getOriginalFilename()))) {
			   fileInfo =  fileInfoRepository.findByFileName(String.join("_", customerId, file.getOriginalFilename()));
			   log.info("ok if");
			}
			else {
				fileInfo = new FileInfo();
				log.info("ok else");
			
			}
			
		    Customer customer = customerRepo.findById(customerId).get();
		    Path rootUserDirectory = Paths.get(new StringBuilder().append(this.root.toString()).append(File.separatorChar).append(customer.getCustomerId()).toString());
		    log.info(rootUserDirectory.toString());
		    if(Files.notExists(rootUserDirectory)) {
		    	try {
					Files.createDirectories(rootUserDirectory);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException("Could not initialize folder for upload!");
				}
		    }
			
			
			String fileName = StringUtils.cleanPath(String.join("_", customer.getCustomerId(), file.getOriginalFilename()));
			Path path = rootUserDirectory.resolve(fileName);
			log.info(path.toString());
			try {
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
					
					.path("/api/files/download/")
					.path(fileName)
					.toUriString();
			log.info(fileInfo.toString());
			fileInfo.setFileName(fileName).setFileUrl(fileDownloadUri).setType(file.getContentType()).setSize(file.getSize()).setCustomer(customer).setLastUpdate(new Date());

			return fileInfoRepository.save(fileInfo);
		}
		

}
