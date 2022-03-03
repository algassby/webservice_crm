/**
 * 
 */
package com.ynov.crm.service;

import java.util.List;

import com.ynov.crm.requestdto.CustomerRequestDto;
import com.ynov.crm.responsedto.CustomerResponseDto;

/**
 * @author algas
 *
 */
public interface CustomerService {

	List<CustomerResponseDto> getAllCustomer(Integer pageNo, Integer pageSize,
			String sortBy);
	CustomerResponseDto getCustomer(String customerId);
	CustomerResponseDto save(CustomerRequestDto customerRequestDto);
	CustomerResponseDto update(CustomerRequestDto customerRequestDto, String customerId);
	String delete(String customerId);
}
