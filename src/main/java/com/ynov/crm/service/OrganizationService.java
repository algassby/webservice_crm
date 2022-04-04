package com.ynov.crm.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ynov.crm.requestdto.OrganizationRequestDto;
import com.ynov.crm.responsedto.OrganizationResponsDto;

public interface OrganizationService {
	
	public Boolean existsByName(String name);
	public Boolean existsById(String orgaId);
	public OrganizationResponsDto findByName(String name);
	public OrganizationResponsDto getOrganization(String orgId);
	public List<OrganizationResponsDto> findAll(Integer pageNo, Integer pageSize,String sortBy);
	public List<OrganizationResponsDto> findAllByAdminId(String userId, Integer pageNo, Integer pageSize,String sortBy);
	public OrganizationResponsDto save(OrganizationRequestDto organizationRequestDto, MultipartFile file);
	public OrganizationResponsDto update(OrganizationRequestDto organizationRequestDto,String orgaId, MultipartFile file);
	public String remove(String orgaId);
	

}
