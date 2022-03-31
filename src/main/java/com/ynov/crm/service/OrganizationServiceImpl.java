package com.ynov.crm.service;

import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.tomcat.jni.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import com.ynov.crm.enties.AppUser;
import com.ynov.crm.enties.Organization;
import com.ynov.crm.mapper.OrganisationMapper;
import com.ynov.crm.repository.AppUserRepository;
import com.ynov.crm.repository.OrganizationRepository;
import com.ynov.crm.requestdto.OrganizationRequestDto;
import com.ynov.crm.responsedto.OrganizationResponsDto;
import com.ynov.crm.utils.CheckAccessAdmin;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
@Data
@Service
@Transactional
@Slf4j
public class OrganizationServiceImpl implements OrganizationService{
	
	private OrganizationRepository organizationRepository;
	private AppUserRepository userRepository;
	private OrganisationMapper organisationMapper;
    private CheckAccessAdmin checkAccessAdmin;
    private UserPrinciple currentUser;
	
	
	public OrganizationServiceImpl(OrganizationRepository organizationRepository, AppUserRepository userRepository,
			OrganisationMapper organisationMapper, CheckAccessAdmin checkAccessAdmin) {
		super();
		this.organizationRepository = organizationRepository;
		this.userRepository = userRepository;
		this.organisationMapper = organisationMapper;
		this.checkAccessAdmin = checkAccessAdmin;
		

		
	}
	public void initCurrentUser(){
		 currentUser  =  (UserPrinciple) SecurityContextHolder.getContext().getAuthentication(). getPrincipal();
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
		this.initCurrentUser();
		if(organizationRepository.existsById(orgId)) {
			Organization  organization= organizationRepository.findById(orgId).get();
			if(checkAccessAdmin.checkAccess(organization.getAdminId(), currentUser)) {
				return organisationMapper.OrganisationToOrganizationResponseDto(organization); 
			}
		}
		
		return new OrganizationResponsDto();
		
	}

	@Override
	public List<OrganizationResponsDto> findAll(Integer pageNo, Integer pageSize, String sortBy) {
		this.initCurrentUser();
		log.debug(organizationRepository.findAll().toString());
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Page<Organization> pagedResult = organizationRepository.findAll(paging);
		List<Organization> organizations = new Vector<>();
		
		pagedResult.forEach(organization->{
			if(checkAccessAdmin.checkAccess(organization.getAdminId(), currentUser)) {
				organizations.add(organization);
			}
	
		});
		return organizations
				.stream()
				.map(organization -> organisationMapper.OrganisationToOrganizationResponseDto(organization))
				.collect(Collectors.toList());
		
		
	}
	
	@Override
	public List<OrganizationResponsDto> findAllByAdminId(String userId, Integer pageNo, Integer pageSize,String sortBy) {
		this.initCurrentUser();
		log.debug(organizationRepository.findAll().toString());
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Page<Organization> pagedResult = null;
		List<Organization> organizations = new Vector<>();
		if(userId == null) {
			pagedResult = organizationRepository.findAll(paging);
		}
		else {
			pagedResult = organizationRepository.findAllByAdminId(userId, paging);
		}
		
		List<Organization> organiz = pagedResult.getContent();
		organiz.forEach(organization->{
			if(checkAccessAdmin.checkAccess(organization.getAdminId(), currentUser)) {
				organizations.add(organization);
			}
	
		});
		return organizations
				.stream()
				.map(organization -> organisationMapper.OrganisationToOrganizationResponseDto(organization))
				.collect(Collectors.toList());
		
	}

	@Override
	public OrganizationResponsDto save(OrganizationRequestDto organizationRequestDto) {
		this.initCurrentUser();
		AppUser user = userRepository.findByUsername(getCurrentUser().getUsername()).get();
		Organization organizationSaved = organisationMapper
				.OrganisationRequestDtoToOrganization(organizationRequestDto).setAdminId(userRepository.findByUsername(user.getUsername())
						.get().getUserId()).setAppUser(user);
		
		return organisationMapper
				.OrganisationToOrganizationResponseDto
				(organizationRepository.save(organizationSaved));
	}

	@Override
	public OrganizationResponsDto update(OrganizationRequestDto organizationRequestDto, String orgaId) {
		this.initCurrentUser();
		Organization organization = organizationRepository.findById(orgaId).get();
		if(this.getCheckAccessAdmin().checkAccess(organization.getAdminId(), getCurrentUser())) {
			
			Organization organizationUpdated = organizationRepository.findById(orgaId).get();
			organizationUpdated.setName(organizationRequestDto.getName())
			.setAddress(organizationRequestDto.getAddress())
			.setLogo(organizationRequestDto.getLogo())
			.setNbSalaris(organizationRequestDto.getNbSalaris());
			
			
			return organisationMapper
					.OrganisationToOrganizationResponseDto
					(organizationRepository.save(organizationUpdated));
		}
		
		return new OrganizationResponsDto();
		
		
	}

	@Override
	public String remove(String orgaId) {
		this.initCurrentUser();
		
		if (organizationRepository.existsById(orgaId)) {
			Organization organization = organizationRepository.findById(orgaId).get();
			if(checkAccessAdmin.checkAccess(organization.getAdminId(), this.currentUser)) {
				organizationRepository.deleteById(orgaId);
				return "remove organization successfully";
			}
			else {
				return "The admin cannot remove this  organization!";
			}

		}else {
			return "remove organization fail";
		}

		
	}
	@Override
	public Boolean existsById(String orgaId) {
		
		return organizationRepository.existsById(orgaId);
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
