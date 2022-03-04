/**
 * 
 */
package com.ynov.crm.responsedto;

import java.util.HashSet;
import java.util.Set;

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
	private Organization organization;
	private Set<FileInfo> fileInfos =  new HashSet<>();	
}
