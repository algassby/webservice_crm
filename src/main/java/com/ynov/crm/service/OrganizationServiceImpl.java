package com.ynov.crm.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ynov.crm.enties.Organization;
import com.ynov.crm.mapper.OrganisationMapper;
import com.ynov.crm.repository.AppUserRepository;
import com.ynov.crm.repository.OrganizationRepository;
import com.ynov.crm.requestdto.OrganizationRequestDto;
import com.ynov.crm.responsedto.OrganizationResponsDto;

import lombok.Data;
@Data
@Service
@Transactional
public class OrganizationServiceImpl implements OrganizationService{
	
	private OrganizationRepository organizationRepository;
	private AppUserRepository userRepository;
	private OrganisationMapper organisationMapper;
	
	
	@Autowired
	public OrganizationServiceImpl(OrganizationRepository organizationRepository, AppUserRepository userRepository,
			OrganisationMapper organisationMapper) {
		super();
		this.organizationRepository = organizationRepository;
		this.userRepository = userRepository;
		this.organisationMapper = organisationMapper;
	}
	
	@Override
	public Boolean existsByName(String name) {
		
		return organizationRepository.existsByName(name);
	}

	@Override
	public OrganizationResponsDto findByName(String name) {

		
		return (organizationRepository.existsByName(name))?
				organisationMapper.OrganisationToOrganizationResponseDto(organizationRepository.findByName(name)):
					null;
	}

	@Override
	public OrganizationResponsDto getOrganization(String orgId) {
		return (organizationRepository.existsById(orgId))?
				organisationMapper.OrganisationToOrganizationResponseDto(organizationRepository.findById(orgId).get()):
					null;
	}

	@Override
	public List<OrganizationResponsDto> findAll() {
		return (organizationRepository.findAll())
				.stream()
				.map(organization -> organisationMapper.OrganisationToOrganizationResponseDto(organization))
				.collect(Collectors.toList())
				;
	}

	@Override
	public OrganizationResponsDto save(OrganizationRequestDto organizationRequestDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrganizationResponsDto update(OrganizationRequestDto organizationRequestDto, String orgaId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String remouve(String orgaId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String addCustomerToOrganization(String orgaName, String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String remouveCustomerToOrganization(String orgaName, String userName) {
		// TODO Auto-generated method stub
		return null;
	}

}
