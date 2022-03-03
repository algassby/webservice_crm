package com.ynov.crm.responsedto;



import java.util.HashSet;

import java.util.Set;



import com.ynov.crm.enties.AppUser;
import com.ynov.crm.enties.Customer;

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
	private AppUser user;
	private Set<Customer> customers = new HashSet<>();


}