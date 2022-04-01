package com.ynov.crm.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ynov.crm.requestdto.OrganizationRequestDto;
import com.ynov.crm.responsedto.OrganizationResponsDto;

public interface OrganizationService {
	
	public Boolean existsByName(String name);
	public Boolean existsById(String orgaId);
	public OrganizationResponsDto findByName(String name);
	public OrganizationResponsDto getOrganization(String orgId);
	public ResponseEntity<?> findAll(Integer pageNo, Integer pageSize,String sortBy);
	public List<OrganizationResponsDto> findAllByAdminId(String userId, Integer pageNo, Integer pageSize,String sortBy);
	
	public ResponseEntity<Object> save(OrganizationRequestDto organizationRequestDto);
	public OrganizationResponsDto update(OrganizationRequestDto organizationRequestDto,String orgaId);
	public String remove(String orgaId);
	

}
