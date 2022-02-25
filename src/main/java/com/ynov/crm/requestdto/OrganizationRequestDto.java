package com.ynov.crm.requestdto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

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

	public Boolean verifObligatoryField() {
		if (name==null||name.equals("")
			||address==null ||address.equals("")) {
			return false;
		}
		return true;
	}
	public Boolean verifOptionalField() {
		if(nbSalaris<0) {
			return false;
		}
		return true;
		
	}

}
