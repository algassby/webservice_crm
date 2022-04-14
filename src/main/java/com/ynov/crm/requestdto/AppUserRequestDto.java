package com.ynov.crm.requestdto;



import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)

public class AppUserRequestDto {

	@NotEmpty(message = "Not be empty")
	@NotBlank(message = "Not Valid")
	@Size(min = 3, max = 60)
	private  String firstName;
	

	@NotEmpty(message = "Not be empty")
	@NotBlank(message = "Not valid")
	@Size(min = 3, max = 60)
	private  String lastName;
	
	@NotEmpty(message = "Not be empty")
	@NotBlank(message = "Email not valid")
	@Size(min = 3, max = 60, message = "Email too long")
	@Email(message = "Email not valid")
	private  String email;


	@NotEmpty(message = "Not be empty")
	@NotBlank(message = "username not valid")
	@Size(min = 3, max = 60, message = "username too long")
	private  String username;

//	@NotBlank
//	@JsonProperty(access = Access.WRITE_ONLY)
//    private String userKey;

	@Size(max = 15, message = "role too long")
	private String roleName;
    
}