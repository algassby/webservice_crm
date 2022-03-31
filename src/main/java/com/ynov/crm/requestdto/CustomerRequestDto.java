/**
 * 
 */
package com.ynov.crm.requestdto;



import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author algas
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true )
public class CustomerRequestDto {

	@NotEmpty(message = "Not be empty")
	@NotBlank(message = "Not valid" )
	@Size(min = 3, max = 60, message = "firstName is too long or short")
	private String  firstName;
	@NotEmpty(message = "Not be empty")
	@NotBlank(message = "Not valid")
	@Size(min = 3, max = 60, message = "lastName is too long or short")
	private String  lastName;
	@NotEmpty(message = "Not be empty")
	@NotBlank(message = "Not valid")
	@Size(min = 10, max = 10,message =  "phoneNumber is not valid")
	private String  phoneNumer;
	@NotEmpty(message = "Not be empty")
	@NotBlank(message = "Not valid")
	@Size(min = 3, max = 250, message = "OrgaId is too long or too short")
	private String orgaId;
}
