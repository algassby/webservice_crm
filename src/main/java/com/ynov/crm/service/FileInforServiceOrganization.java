/**
 * 
 */
package com.ynov.crm.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ynov.crm.enties.FileInfo;
import com.ynov.crm.enties.Organization;
import com.ynov.crm.exception.FileUploadException;
import com.ynov.crm.mapper.FileInfoMapper;
import com.ynov.crm.repository.FileInfoRepository;
import com.ynov.crm.repository.OrganizationRepository;
import com.ynov.crm.responsedto.FileInfoResponseDto;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author algas
 *
 */
@Service("FileInforServiceOrganization")
@Transactional
@Getter
@Setter
@Slf4j
public class FileInforServiceOrganization implements FileInfoService {

	private FileInfoRepository fileInfoRepository;
	private FileInfoMapper fileInfoMapper;
	private OrganizationRepository orgaRepo;
	private final Path root = Paths.get("uploads");
	private final Path orgaRoot = Paths.get(new StringBuffer().append(root.toString()).append(File.separatorChar).append("organization").toString());
	private UserPrinciple currentUser;
	private Logger logger = LoggerFactory.getLogger(FileInfoServiceImpl.class);
	
	/**
	 * @param fileInfoRepository
	 * @param fileInfoMapper
	 * @param orgaRepo
	 */
	@Autowired
	public FileInforServiceOrganization(FileInfoRepository fileInfoRepository, FileInfoMapper fileInfoMapper,
			OrganizationRepository orgaRepo) {
		super();
		this.fileInfoRepository = fileInfoRepository;
		this.fileInfoMapper = fileInfoMapper;
		this.orgaRepo = orgaRepo;
	}

	public void initCurrentUser(){
		 currentUser  =  (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
    	if(Files.notExists(orgaRoot)) {
    		try {
    	        Files.createDirectory(orgaRoot);
    	      } catch (IOException e) {
    	        throw new RuntimeException("Could not initialize folder for Organization!");
    	      }
    	}
      
    }
    
    
    
    public FileInfo save(MultipartFile file, String objectId) throws FileUploadException {
    	return null;
    }
    public byte[] getPhoto(String fileId) throws Exception{
    	return null;
	 }
    @Override
    public Resource load(String filename) {
      FileInfo fileInfo = fileInfoRepository.findByFileName(filename);
      try {
    	  
    	Path path = Paths.get(new StringBuffer().append(fileInfo.getOrganization().getOrgaId()).append(File.separatorChar).append(filename).toString());
        Path file = orgaRoot.resolve(path);
        log.info(file.toString());
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
	public String deleteFile(String fileName, String objectId) {
		log.info(root.toString());
		Path path = Paths.get(String.join("", root.toString(),new StringBuilder().append(File.separatorChar).append(fileName).toString()));
		Organization organization =  orgaRepo.findById(objectId).get();
		if(fileInfoRepository.existsById(organization.getFileInfo().getFileId())) {
			fileInfoRepository.deleteById(organization.getFileInfo().getFileId());
		}
		organization.setFileInfo(null);
		orgaRepo.save(organization);
		 Path rootUserDirectory = Paths.get(new StringBuilder().append(this.orgaRoot.toString()).append(File.separatorChar).append(fileName).toString());
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
	        		fileInfo.setOrganization(null);
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
	public String deleteObjectDirectory(String objectId) {
		if(!orgaRepo.existsById(objectId)) {
			return "cannot deleted orga directory, because it doest not exists";
		}
		Path path = Paths.get(String.join("", orgaRoot.toString(),new StringBuilder().append(File.separatorChar).append(objectId).toString()));
		Organization organization = orgaRepo.findById(objectId).get();
		if(organization.getFileInfo()!=null) {
			organization.getFileInfo().setOrganization(null);
			try {
				Files.walk(path)
				  .sorted(Comparator.reverseOrder())
				  .map(Path::toFile)
				  .forEach(File::delete);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "Orga delete directory";
			
		}
		return "Failed Orga delete directory";
//		  if (path.toFile().exists()) {
//			  this.fileInfoRepository.findAll().stream().forEach(image->{
//				  if(image.getOrganization()!=null) {
//					  if(image.getOrganization().getAdminId()==null) {
//						  image.setOrganization(null);
//						  fileInfoRepository.deleteById(image.getFileId());
//					  }
//					 
//				  }
//			  });
			  
				
		
		
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
			this.init();
			this.initCurrentUser();
			Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
			 
	        Page<FileInfo> pagedResult = fileInfoRepository.findAll(paging);
	        List<FileInfo> images = new ArrayList<>();
			 pagedResult.getContent().forEach(image->{
				 if(image.getOrganization()!=null) {
					 if(currentUser.getUsername().equals("admin") || currentUser.getUserId().equals(image.getOrganization().getAdminId())) {
						images.add(image);
					} 
				 }

				});
			 return 
					images
					.stream()
					.map(image->fileInfoMapper.fileInfoToFileInfoResponseDto(image))
					.collect(Collectors.toList());
		}
		@Override
		public List<FileInfoResponseDto> findAllFileByCustomer(String customerId) {
			this.initCurrentUser();
			List<FileInfo> images = new Vector<>();
			  fileInfoRepository.findAllFileByCustomer(customerId)
					 
					 .forEach(image->{
						
						if(image.getCustomer().getOrganization().getAdminId().equals(currentUser.getUserId()) || currentUser.getUsername().equals("admin")) {
							images.add(image);
						}
					 });
					return  images.stream()
							.map(image->fileInfoMapper.fileInfoToFileInfoResponseDto(image))
					 .collect(Collectors.toList());
				
		}
		@Override
		public FileInfo getFile(String fileId) {
			return fileInfoRepository.findById(fileId).get();
		}
		@Override
		public FileInfo uploadToLocalFileSystem(String objectId, MultipartFile file) {
			  Organization organization = orgaRepo.findById(objectId).get();
			  if(organization.getFileInfo()!=null) {
				  if(fileInfoRepository.existsByFileName(organization.getFileInfo().getFileName())) {
						fileInfoRepository.deleteById(organization.getFileInfo().getFileId());
				
					}
			  }
			
			if(organization.getFileInfo()!=null) {
				if(fileInfoRepository.existsById(organization.getFileInfo().getFileId())) {
					fileInfoRepository.deleteById(organization.getFileInfo().getFileId());
				}
			}
			
			
			   Path rootCurrent = Paths.get(new StringBuilder().append(this.orgaRoot.toString()).append(File.separatorChar).append(organization.getOrgaId()).toString());

			   if(rootCurrent.toFile().exists()) {
				    FileSystemUtils.deleteRecursively(rootCurrent.toFile());

			   }
			  
			FileInfo fileInfo = new FileInfo();

		    Path rootOrgaDirectory = Paths.get(new StringBuilder().append(this.orgaRoot.toString()).append(File.separatorChar).append(organization.getOrgaId()).toString());
		   
		    if(Files.notExists(rootOrgaDirectory)) {
		    	try {
					Files.createDirectories(rootOrgaDirectory);
				} catch (IOException e) {
		
					throw new RuntimeException("Could not initialize folder for upload!");
				}
		    }
		    
			String fileName = StringUtils.cleanPath(new StringBuilder(organization.getOrgaId()).append("_").append(file.getOriginalFilename()).toString());
			Path path = rootOrgaDirectory.resolve(fileName);
		
			try {
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
					
					.path("/api/files/organization/download/")
					.path(fileName)
					.toUriString();
			
			fileInfo.setFileName(fileName).setFileUrl(fileDownloadUri).setType(file.getContentType()).setSize(file.getSize()).setOrganization(organization).setLastUpdate(new Date());
			FileInfo fileInfoSaved = fileInfoRepository.save(fileInfo);
			organization.setFileInfo(fileInfoSaved);
			orgaRepo.save(organization);
			return fileInfoSaved;
		}


		@Override
		public FileInfo uploadToLocalFileSystemForOrganization(String objectId, MultipartFile file) {
			FileInfo fileInfo = null;
			if(fileInfoRepository.existsByFileName(String.join("_", objectId, file.getOriginalFilename()))) {
			   fileInfo =  fileInfoRepository.findByFileName(String.join("_", objectId, file.getOriginalFilename()));
			  
			   log.info("ok if");
			}
			else {
				fileInfo = new FileInfo();
				log.info("ok else");
			
			}
		
		    Organization organization = orgaRepo.findById(objectId).get();
		    Path rootOrgaDirectory = Paths.get(new StringBuilder().append(this.orgaRoot.toString()).append(File.separatorChar).append(organization.getOrgaId()).toString());
		    log.info(rootOrgaDirectory.toString());
		    if(Files.notExists(rootOrgaDirectory)) {
		    	try {
					Files.createDirectories(rootOrgaDirectory);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException("Could not initialize folder for upload!");
				}
		    }
			
			String fileName = StringUtils.cleanPath(String.join("_", organization.getOrgaId(), file.getOriginalFilename()));
			Path path = rootOrgaDirectory.resolve(fileName);
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
			
			fileInfo.setFileName(fileName).setFileUrl(fileDownloadUri).setType(file.getContentType()).setSize(file.getSize()).setOrganization(organization).setLastUpdate(new Date());
			FileInfo fileInfoSaved = fileInfoRepository.save(fileInfo);
			organization.setFileInfo(fileInfoSaved);
			orgaRepo.save(organization);
			return fileInfoRepository.save(fileInfo);
		}
		

		

}
