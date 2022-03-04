/**
 * 
 */
package com.ynov.crm.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.ynov.crm.mapper.CustomerMapper;
import com.ynov.crm.repository.CustomerRepository;
import com.ynov.crm.repository.OrganizationRepository;
import com.ynov.crm.requestdto.CustomerRequestDto;
import com.ynov.crm.requestdto.JsonObjectDto;
import com.ynov.crm.responsedto.AppUserResponseDto;
import com.ynov.crm.responsedto.CustomerResponseDto;

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
	
	/**
	 * @param customerRepo
	 * @param customerMapper
	 */
	@Autowired
	public CustomerServiceImpl(CustomerRepository customerRepo, CustomerMapper customerMapper, OrganizationRepository organizationRepo, FileInfoService fileService) {
		super();
		this.customerRepo = customerRepo;
		this.customerMapper = customerMapper;
		this.organizationRepo = organizationRepo;
		this.fileService = fileService;
		this.fileService.init();
	}
	@Override
	public List<CustomerResponseDto> getAllCustomer(Integer pageNo, Integer pageSize,
			String sortBy) {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		log.debug(customerRepo.findAll().toString());
		Page<Customer> pagedResult = customerRepo.findAll(paging);
		if(pagedResult.hasContent()) {
			return customerRepo.findAll()
					.stream()
					.map(customer->customerMapper.customerToCustomerResponseDto(customer))
					.collect(Collectors.toList());
		}
		else {
			return new ArrayList<>();
		}
		
	}


	@Override
	public CustomerResponseDto getCustomer(String customerId) {
		if(!customerRepo.existsById(customerId)) {
			return new CustomerResponseDto();
		}
		return customerMapper.customerToCustomerResponseDto(customerRepo.findById(customerId).get());
	}

	@Override
	public CustomerResponseDto save(CustomerRequestDto customerRequestDto) {
		Customer customer = customerMapper.customerRequestDtoToCustomer(customerRequestDto);
		
		if(customerRequestDto.getOrgaId()!=null) {
			customer.setOrganization(organizationRepo.findById(customerRequestDto.getOrgaId()).get());
		}
		
		
		return customerMapper.customerToCustomerResponseDto(customerRepo.save(customer));
	}

	@Override
	public CustomerResponseDto update(CustomerRequestDto customerRequestDto, String customerId) {
		
		Customer customer = customerRepo.findById(customerId).get();
		if(customerRequestDto.getOrgaId()!=null) {
			customer.setOrganization(organizationRepo.findById(customerRequestDto.getOrgaId()).get());
		}
		
		customer.setFirstName(customerRequestDto.getFirstName()).setLastName(customerRequestDto.getLastName())
		.setPhoneNumer(customerRequestDto.getPhoneNumer());
		return customerMapper.customerToCustomerResponseDto(customerRepo.save(customer));
	}

	@Override
	public String delete(String customerId) {
		if(customerId==null) {
			 return new StringBuffer("Not Found Customer").toString();
		}
		
		if(customerRepo.existsById(customerId)) {
			Customer customer = customerRepo.findById(customerId).get();
			customer.getFileInfos().stream().forEach(file->{
				fileService.deleteFile(file.getFileName());
			});
			customerRepo.deleteById(customerId);
			
			return new StringBuffer().append("Delete Customer successfully!").toString();
		}
		return new StringBuffer().append("Failed deleted, cause Customer doest not exists!").toString(); 
		
		
	}
	@Override
	public String addImageToCustomer(String customerId, MultipartFile file) {
		if(customerId==null) {
			 return new StringBuffer("Not Found Customer").toString();
		}
		if(!customerRepo.existsById(customerId)) {
			return new StringBuffer("Not Found Customer").toString();
		}
		
		Customer customer = customerRepo.findById(customerId).get();
		FileInfo image = fileService.save(file);
		Set<FileInfo> images  = customer.getFileInfos();
		images.add(image);
		customer.setFileInfos(images);
		customerRepo.save(customer);
		return new StringBuffer().append("added successfully !").toString(); 
	}
	@Override
	public String removeImageToCustomer(String customerId, String imageName) {
		if(customerId==null) {
			 return new StringBuffer("Not Found Customer").toString();
		}
		if(!customerRepo.existsById(customerId)) {
			return new StringBuffer("Not Found Customer").toString();
		}
		fileService.deleteFile(imageName);
		Customer customer = customerRepo.findById(customerId).get();
		Set<FileInfo> images  = customer.getFileInfos();
		images.removeIf(image->image.getFileName().equals(imageName));
		customer.setFileInfos(images);
		customerRepo.save(customer);
		return new StringBuffer().append("Failed deleted !").toString(); 
	}
	@Override
	public Boolean existsById(String customerId) {
		return customerRepo.existsById(customerId);
	}
	@Override
	public String addAllImageToCustomer(String customerId, MultipartFile [] files) {
		if(customerId==null) {
			 return new StringBuffer("Not Found Customer").toString();
		}
		if(!customerRepo.existsById(customerId)) {
			return new StringBuffer("Not Found Customer").toString();
		}
		Customer customer = customerRepo.findById(customerId).get();
		Set<FileInfo> images  = customer.getFileInfos();
		
		      List<String> fileNames = new ArrayList<>();
		      Arrays.asList(files).stream().forEach(file -> {
		      FileInfo fileInfo =   fileService.save(file);
		        fileNames.add(file.getOriginalFilename());
		        images.add(fileInfo);
		        
		      });
		      
		customer.setFileInfos(images);
		customerRepo.save(customer);
		return new StringBuffer().append("added all files successfully !").toString(); 

	}
	@Override
	public String removeManyImageToCustomer(String customerId, JsonObjectDto jsonObjectDto) {
		if(customerId==null) {
			 return new StringBuffer("Not Found Customer").toString();
		}
		if(!customerRepo.existsById(customerId)) {
			return new StringBuffer("Not Found Customer").toString();
		}
		
		Customer customer = customerRepo.findById(customerId).get();
		Set<FileInfo> images  = customer.getFileInfos();
		images.forEach(item->{
			
		});
		jsonObjectDto.getImages().stream().forEach(image->{
			fileService.deleteFile(image);
			images.removeIf(img->img.getFileName().equals(image));
		});
		
		customer.setFileInfos(images);
		customerRepo.save(customer);
		return new StringBuffer().append("Delete all files successfully !").toString(); 

	}

	

}
