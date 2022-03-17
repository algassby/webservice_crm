/**
 * 
 */
package com.ynov.crm.mapper;

import org.mapstruct.Mapper;

import com.ynov.crm.enties.Customer;
import com.ynov.crm.requestdto.CustomerRequestDto;
import com.ynov.crm.responsedto.CustomerResponseDto;

/**
 * @author algas
 *
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper {

	public CustomerResponseDto customerToCustomerResponseDto(Customer  customer);
	public Customer customerRequestDtoToCustomer (CustomerRequestDto customerRequestDto);
}
