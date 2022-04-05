/**
 * 
 */
package com.ynov.crm.service;


import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ynov.crm.enties.AppUser;
import com.ynov.crm.enties.Customer;
import com.ynov.crm.enties.FileInfo;
import com.ynov.crm.enties.Log;
import com.ynov.crm.enties.Organization;
import com.ynov.crm.mapper.CustomerMapper;
import com.ynov.crm.repository.CustomerRepository;
import com.ynov.crm.repository.LogRepository;
import com.ynov.crm.repository.OrganizationRepository;
import com.ynov.crm.requestdto.CustomerRequestDto;
import com.ynov.crm.requestdto.JsonObjectDto;
import com.ynov.crm.responsedto.AppUserResponseDto;
import com.ynov.crm.responsedto.CustomerResponseDto;
import com.ynov.crm.utils.CheckAccessAdmin;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author algas
 *
 */
@Service
@Transactional
@Data
@Slf4j

public class CustomerServiceImpl implements CustomerService {

	private CustomerRepository customerRepo;
	private CustomerMapper customerMapper;
	private OrganizationRepository organizationRepo;
	private FileInfoService fileService;
	private LogRepository logRepository;
	private UserPrinciple currentUser;
	private CheckAccessAdmin checkAccessAdmin;
	
	/**
	 * @param customerRepo
	 * @param customerMapper
	 */
	@Autowired
	public CustomerServiceImpl(CustomerRepository customerRepo,
							   CustomerMapper customerMapper,
							   OrganizationRepository organizationRepo,
							   LogRepository logRepository,
							   @Qualifier("FileInfoServiceImpl")  FileInfoService fileService,
							   CheckAccessAdmin checkAccessAdmin
							   )
	{
		super();
		this.customerRepo = customerRepo;
		this.customerMapper = customerMapper;
		this.organizationRepo = organizationRepo;
		this.fileService = fileService;
		this.logRepository = logRepository;
		this.checkAccessAdmin = checkAccessAdmin;
		this.fileService.init();
	}
	public void initCurrentUser(){
		 currentUser  =  (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	@Override
	public List<CustomerResponseDto> getAllCustomer(Integer pageNo, Integer pageSize,
			String sortBy) {
		this.initCurrentUser();
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		List<Customer> customers = new Vector<>();
		log.info(currentUser.toString());
		Page<Customer> pagedResult = customerRepo.findAll(paging);
		pagedResult.forEach(customer->{
			log.info(customer.getOrganization().getAdminId());
			log.info(String.valueOf(checkAccessAdmin.checkAccess(customer.getOrganization().getAdminId(), currentUser)));
			if(currentUser!=null) {
				log.info(customer.getOrganization().toString());
				if(checkAccessAdmin.checkAccess(customer.getOrganization().getAdminId(), currentUser)) {
					customers.add(customer);
				}
			}
			
		});
		if(!customers.isEmpty()) {
			return customers
					.stream()
					.map(customer->{
						CustomerResponseDto customerResponse = customerMapper.customerToCustomerResponseDto(customer);
					
						return customerResponse;
					})
					.collect(Collectors.toList());
		}
		else {
			return new  Vector<>();
		}
		
	}


	@Override
	public CustomerResponseDto getCustomer(String customerId) {
		this.initCurrentUser();
		if(!customerRepo.existsById(customerId)) {
			return new CustomerResponseDto();
		}
		else {
			Customer customer = customerRepo.findById(customerId).get();
			if(checkAccessAdmin.checkAccess(customer.getOrganization().getAdminId(), currentUser)) {
				return customerMapper.customerToCustomerResponseDto(customer);
			}
			return new CustomerResponseDto();
			
		}

		
	}

	@Override
	public CustomerResponseDto save(CustomerRequestDto customerRequestDto) {
		this.initCurrentUser();
		Customer customer = customerMapper.customerRequestDtoToCustomer(customerRequestDto).setLastUpdate(new Date());
		
		if(organizationRepo.existsById(customerRequestDto.getOrgaId())) {
			customer.setOrganization(organizationRepo.findById(customerRequestDto.getOrgaId()).get());
			Customer customerSaved = customerRepo.save(customer);
			if(customerSaved!=null) {
				
				logRepository.save(new Log().setUsername(currentUser.getUsername())
						.setDescription(new StringBuffer().append("L'admin").append(currentUser.getUsername()).append(" a ajouter ").append(" un customer ")
								.append(" à ").append(new Date()).append(".").toString()).setLastUpdate(new Date()));
			}
			return customerMapper.customerToCustomerResponseDto(customerSaved);
		}
		return new CustomerResponseDto();
		
	}

	@Override
	public CustomerResponseDto update(CustomerRequestDto customerRequestDto, String customerId) {
		this.initCurrentUser();
		if(!customerRepo.existsById(customerId)) {
			return new CustomerResponseDto();
		}
		Customer customer = customerRepo.findById(customerId).get();
		
		if(customerRequestDto.getOrgaId()!=null) {
			if(organizationRepo.existsById(customerRequestDto.getOrgaId())) {
				Organization organization = organizationRepo.findById(customerRequestDto.getOrgaId()).get();
				customer.setOrganization(organization);
			}
			
		}
	
		if(checkAccessAdmin.checkAccess(customer.getOrganization().getAppUser().getUserId(), currentUser)) {
			
			customer.setFirstName(customerRequestDto.getFirstName()).setLastName(customerRequestDto.getLastName())
			.setPhoneNumer(customerRequestDto.getPhoneNumer()).setLastUpdate(new Date());
			
			return customerMapper.customerToCustomerResponseDto(customerRepo.save(customer));
		}
		
		return new CustomerResponseDto();
	}

	@Override
	public String delete(String customerId) {
		this.initCurrentUser();
		if(customerId==null) {
			 return new StringBuffer("Not Found Customer").toString();
		}
	
		if(customerRepo.existsById(customerId)) {
			Customer customer = customerRepo.findById(customerId).get();

			if(checkAccessAdmin.checkAccess(customer.getOrganization().getAdminId(), currentUser)) {
				log.info(customer.toString());
				log.info(checkAccessAdmin.checkAccess(customer.getOrganization().getAdminId(), currentUser).toString());
				fileService.deleteObjectDirectory(customerId);
				customerRepo.deleteById(customerId);
				if(!customerRepo.existsById(customerId)) {
					logRepository.save(new Log().setUsername(currentUser.getUsername())
							.setDescription(new StringBuffer().append("L'admin").append(currentUser.getUsername()).append(" a supprimer ").append(" un customer ")
									.append(" à ").append(new Date()).append(".").toString()).setLastUpdate(new Date()));
				}
				return new StringBuffer().append("Delete Customer successfully!").toString();
			}
			
		}
		return new StringBuffer().append("Failed deleted, cause Customer doest not exists!").toString(); 
		
	}
	@Override
	public String addImageToCustomer(String customerId, MultipartFile file) {
		this.initCurrentUser();
		if(customerId==null) {
			 return new StringBuffer("Not Found Customer").toString();
		}
		if(!customerRepo.existsById(customerId)) {
			return new StringBuffer("Not Found Customer").toString();
		}
		
		Customer customer = customerRepo.findById(customerId).get();
		if(checkAccessAdmin.checkAccess(customer.getOrganization().getAdminId(), currentUser)) {
			FileInfo image = fileService.uploadToLocalFileSystem(customerId, file);
			Set<FileInfo> images  = customer.getFileInfos();
			images.add(image);
			customer.setFileInfos(images);
			customerRepo.save(customer);
			return new StringBuffer().append("added successfully !").toString();
		}
		
		return new StringBuffer().append("the admin cannot add image!").toString(); 
	}
	@Override
	public String removeImageToCustomer(String customerId, String imageName) {
		this.initCurrentUser();
		if(customerId==null) {
			 return new StringBuffer("Not Found Customer").toString();
		}
		if(!customerRepo.existsById(customerId)) {
			return new StringBuffer("Not Found Customer").toString();
		}
		if(!fileService.existsByFileName(imageName)) {
			return new StringBuffer("Image doest not Exist").toString();
		}
		
	
		Customer customer = customerRepo.findById(customerId).get();
		if(checkAccessAdmin.checkAccess(customer.getOrganization().getAdminId(), currentUser)) {
			fileService.deleteFile(imageName, customerId);
			Set<FileInfo> images  = customer.getFileInfos();
			images.removeIf(image->image.getFileName().equals(imageName));
			customer.setFileInfos(images);
			customerRepo.save(customer);
			return new StringBuffer().append("Delete success !").toString(); 
		}
		
		return new StringBuffer().append("Admin cannot delete the file!").toString(); 
	}
	@Override
	public Boolean existsById(String customerId) {
		return customerRepo.existsById(customerId);
	}
	@Override
	public String addAllImageToCustomer(String customerId, MultipartFile [] files) {
		this.initCurrentUser();
		if(customerId==null) {
			 return new StringBuffer("Not Found Customer").toString();
		}
		if(!customerRepo.existsById(customerId)) {
			return new StringBuffer("Not Found Customer").toString();
		}
		Customer customer = customerRepo.findById(customerId).get();
		if(checkAccessAdmin.checkAccess(customer.getOrganization().getAdminId(), currentUser)) {
			Set<FileInfo> images  = customer.getFileInfos();
			
			      List<String> fileNames = new ArrayList<>();
			      Arrays.asList(files).stream().forEach(file -> {
			    	  
			      FileInfo fileInfo =   fileService.uploadToLocalFileSystem(customerId, file);
			        fileNames.add(file.getOriginalFilename());
			        images.add(fileInfo);
			        
			      });
			      
			customer.setFileInfos(images);
			customerRepo.save(customer);
			return new StringBuffer().append("added all files successfully !").toString(); 
		}
		
		return new StringBuffer().append("Cannot delete  files!").toString(); 

	}
	@Override
	public String removeManyImageToCustomer(String customerId, JsonObjectDto jsonObjectDto) {
		this.initCurrentUser();
		if(customerId==null) {
			 return new StringBuffer("Not Found Customer").toString();
		}
		if(!customerRepo.existsById(customerId)) {
			return new StringBuffer("Not Found Customer").toString();
		}
		
		Set<String> imagesStr = jsonObjectDto.getImages();

		Customer customer = customerRepo.findById(customerId).get();
		
		
		if(checkAccessAdmin.checkAccess(customer.getOrganization().getAdminId(), currentUser)) {
			Set<FileInfo> images  = customer.getFileInfos();
			
			if(imagesStr.isEmpty()) {
				return new StringBuffer().append("Failed deleted files successfully, please verify your images's name!").toString(); 
			}
			
			imagesStr.stream().forEach(image->{
				//images.removeIf(img->img.getFileName().equals(image));                        
				
				images.removeIf(img->img.getFileName().equals(image));
				fileService.deleteFile(image, customerId);
				
			});
			
			
			
			customerRepo.save(customer);
			return new StringBuffer().append("Delete files successfully !").toString(); 
			
		}
		return new StringBuffer().append("The admin cannot delete the customer !").append(customer.getFirstName()).toString(); 
		

	}
	@Override
	public List<CustomerResponseDto> findByOrganization(String organizationId) {
		this.initCurrentUser();
		List<Customer> customers = new Vector<>();
		customerRepo.findByOrganization(organizationId).forEach(customer->{
			log.debug(customer.getOrganization().getAdminId());
			if(currentUser!=null) {
				if(checkAccessAdmin.checkAccess(customer.getOrganization().getAdminId(), currentUser)) {
					customers.add(customer);
				}
			}
			
		});
		return customers
				.stream()
				.map(customer->customerMapper.customerToCustomerResponseDto(customer))
				.collect(Collectors.toList());
	}

	

}
