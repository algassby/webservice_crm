package com.ynov.crm.service;

import java.util.List;

import com.ynov.crm.requestdto.OrganizationRequestDto;
import com.ynov.crm.responsedto.OrganizationResponsDto;

public interface OrganizationService {
	
	public Boolean existsByName(String name);
	public OrganizationResponsDto findByName(String name);
	public OrganizationResponsDto getOrganization(String orgId);
	public List<OrganizationResponsDto> findAll();
	
	public OrganizationResponsDto save(OrganizationRequestDto organizationRequestDto);
	public OrganizationResponsDto update(OrganizationRequestDto organizationRequestDto,String orgaId);
	public String remove(String orgaId);
	
//	public String addCustomerToOrganization(String orgaId, String customerId);
//	public String removeCustomerToOrganization(String orgaId,String customerId);

}
