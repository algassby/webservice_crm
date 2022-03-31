package com.ynov.crm.requestdto;



import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)

public class AppUserRequestDto {


	@NotEmpty
	private  String firstName;
	

	@NotEmpty
	private  String lastName;
	
	@NotEmpty
	@Email
	private  String email;

	@NotEmpty
	private  String username;

//	@NotBlank
//	@JsonProperty(access = Access.WRITE_ONLY)
//    private String userKey;

	@NotEmpty
	@NotBlank
	@NotNull
	private String roleName;
    
}