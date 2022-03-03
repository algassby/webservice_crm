/**
 * 
 */
package com.ynov.crm.responsedto;

import com.ynov.crm.enties.Organization;

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
public class CustomerResponseDto {

	private String  customerId;
	private String  firstName;
	private String  lastName;
	private String  phoneNumer;
	private Organization organization;
}
