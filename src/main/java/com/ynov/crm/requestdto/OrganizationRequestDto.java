package com.ynov.crm.requestdto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class OrganizationRequestDto {
	
	private String name;
	private String address;
	private int nbSalaris;	
	private String logo;


}
