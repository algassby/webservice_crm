/**
 * 
 */
package com.ynov.crm.responsedto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.ynov.crm.enties.AppRole;
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

public class AppUserResponseDto {

	private  String userId;
	private  String firstName;
	private  String lastName;
	private  String email;
	private  String username;
	@JsonProperty(access = Access.WRITE_ONLY)
    private String password;
    private Set<AppRole> roles = new HashSet<>();
    @JsonProperty(access = Access.WRITE_ONLY)
    private String adminId;
	@Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
	private Set<Organization> organizations = new HashSet<>();
	

}
