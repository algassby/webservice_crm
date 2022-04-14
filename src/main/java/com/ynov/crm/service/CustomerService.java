/**
 * 
 */
package com.ynov.crm.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ynov.crm.requestdto.CustomerRequestDto;
import com.ynov.crm.requestdto.JsonObjectDto;
import com.ynov.crm.responsedto.CustomerResponseDto;

/**
 * @author algas
 *
 */
public interface CustomerService {

	List<CustomerResponseDto> getAllCustomer(Integer pageNo, Integer pageSize,
			String sortBy);
	List<CustomerResponseDto> findByOrganization(String organizationId);
	CustomerResponseDto getCustomer(String customerId);
	Boolean existsById(String customerId);
	CustomerResponseDto save(CustomerRequestDto customerRequestDto);
	CustomerResponseDto update(CustomerRequestDto customerRequestDto, String customerId);
	String delete(String customerId);
	String addImageToCustomer(String customerId, MultipartFile file );
	String addAllImageToCustomer(String customerId, MultipartFile file [] );
	String removeImageToCustomer(String customerId, String imageName);
	String removeManyImageToCustomer(String customerId, JsonObjectDto jsonObjectDto);
	
}
