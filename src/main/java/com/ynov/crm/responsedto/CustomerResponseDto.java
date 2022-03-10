/**
 * 
 */
package com.ynov.crm.responsedto;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.ynov.crm.enties.FileInfo;
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
	@JsonProperty(access = Access.WRITE_ONLY)
	private Organization organization;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Set<FileInfo> fileInfos =  new HashSet<>();	
}
