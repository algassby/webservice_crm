package com.ynov.crm.responsedto;



import java.util.HashSet;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.ynov.crm.enties.AppUser;
import com.ynov.crm.enties.Customer;
import com.ynov.crm.enties.FileInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class OrganizationResponsDto {
	
	private String orgaId;
	private String name;
	private String address;
	private int nbSalaris;
	private String logo;
	@JsonProperty(access = Access.WRITE_ONLY)
	private AppUser user;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Set<Customer> customers = new HashSet<>();
	private FileInfo fileInfo;
	


}