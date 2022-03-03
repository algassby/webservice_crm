package com.ynov.crm.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
		UserPrinciple user =  (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Organization organizationSaved = organisationMapper
				.OrganisationRequestDtoToOrganization(organizationRequestDto).setUser(userRepository.findByUsername(user.getUsername()).get());
		
		return organisationMapper
				.OrganisationToOrganizationResponseDto
				(organizationRepository.save(organizationSaved));
	}

	@Override
	public OrganizationResponsDto update(OrganizationRequestDto organizationRequestDto, String orgaName) {
		
		Organization organizationUpdated = organizationRepository.findByName(orgaName);
		organizationUpdated.setName(organizationRequestDto.getName())
		.setAddress(organizationRequestDto.getAddress())
		.setLogo(organizationRequestDto.getLogo())
		.setNbSalaris(organizationRequestDto.getNbSalaris());
		
		return organisationMapper
				.OrganisationToOrganizationResponseDto
				(organizationRepository.save(organizationUpdated));
	}

	@Override
	public String remove(String orgaId) {
		if (organizationRepository.existsById(orgaId)) {
			organizationRepository.deleteById(orgaId);
			
			return "remove organization successfully";
		}else {
			return "remove organization fail";
		}

		
	}
 
//	@Override
//	public String addCustomerToOrganization(String orgaName, String userName) {
//	Organization organization = organizationRepository
//		.findByName(userName);
//	organization.getUsers()
//	.add(userRepository.findByUsername(userName).get());
//		organizationRepository.save(organization);
//		
//		return "add customer" +userName+" to organisation "+orgaName+" successfully";
//		
//	}
//
//	@Override
//	public String removeCustomerToOrganization(String orgaName, String userName) {
//		
//		Organization organization = organizationRepository
//				.findByName(userName);
//		
//		organization.getUsers().removeIf(user->user.getUsername().equals(userName));
//		organizationRepository.save(organization);
//		
//		return "remove customer" +userName+" to organisation "+orgaName+" successfully";
//	}

}
