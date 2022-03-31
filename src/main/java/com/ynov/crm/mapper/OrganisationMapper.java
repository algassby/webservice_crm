/**
 * 
 */
package com.ynov.crm.mapper;

import org.mapstruct.Mapper;

import com.ynov.crm.enties.Organization;
import com.ynov.crm.requestdto.OrganizationRequestDto;
import com.ynov.crm.responsedto.OrganizationResponsDto;

/**
 * @author gonza
 *
 */
@Mapper(componentModel ="spring")
public interface OrganisationMapper {
	public Organization OrganisationRequestDtoToOrganization(OrganizationRequestDto organizationRequestDto);
	public OrganizationResponsDto OrganisationToOrganizationResponseDto(Organization organization);

}
