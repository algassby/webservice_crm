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

import io.swagger.v3.oas.annotations.media.Schema;
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

@Schema(name="Customer", description="Entity contains Customer information") 

public class CustomerResponseDto {

	private String  customerId;
	@Schema(description="Customer firstName", maximum="250")
	private String  firstName;
	private String  lastName;
	private String  phoneNumer;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Organization organization;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Set<FileInfo> fileInfos =  new HashSet<>();	
}
