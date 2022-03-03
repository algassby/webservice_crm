/**
 * 
 */
package com.ynov.crm.service;

import java.util.ArrayList;
import java.util.List;import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynov.crm.enties.AppUser;
import com.ynov.crm.enties.Customer;
import com.ynov.crm.mapper.CustomerMapper;
import com.ynov.crm.repository.CustomerRepository;
import com.ynov.crm.repository.OrganizationRepository;
import com.ynov.crm.requestdto.CustomerRequestDto;
import com.ynov.crm.responsedto.AppUserResponseDto;
import com.ynov.crm.responsedto.CustomerResponseDto;

import lombok.Data;

/**
 * @author algas
 *
 */
@Service
@Transactional
@Data
public class CustomerServiceImpl implements CustomerService {

	private CustomerRepository customerRepo;
	private CustomerMapper customerMapper;
	private OrganizationRepository organizationRepo;
	
	/**
	 * @param customerRepo
	 * @param customerMapper
	 */
	@Autowired
	public CustomerServiceImpl(CustomerRepository customerRepo, CustomerMapper customerMapper, OrganizationRepository organizationRepo) {
		super();
		this.customerRepo = customerRepo;
		this.customerMapper = customerMapper;
		this.organizationRepo = organizationRepo;
	}
	@Override
	public List<CustomerResponseDto> getAllCustomer(Integer pageNo, Integer pageSize,
			String sortBy) {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
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
		
		if(customerRepo.existsById(customerId)) {
			customerRepo.deleteById(customerId);
			return new StringBuffer().append("Delete Customer successfully !").toString();
		}
		return new StringBuffer().append("Failed deleted !").toString(); 
		
		
	}

	

}
