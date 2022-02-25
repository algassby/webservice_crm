package com.ynov.crm.responsedto;

import java.util.ArrayList;
import java.util.List;

import com.ynov.crm.enties.AppUser;

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
	private String Address;
	private int nbSalaris;	
	private String logo;
	private List<AppUser> users = new ArrayList<AppUser>();


}