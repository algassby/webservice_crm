/**
 * 
 */
package com.ynov.crm.enties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author algas
 *
 */
@Entity(name = "Administrator")
@Table(name = "Administrator")

@Data
@AllArgsConstructor
@NoArgsConstructor

@Accessors(chain = true )
public class Administrator {
	
	@Column(name = "adminId")
	private String adminId;
	
	@Column(name = "fistName")
	private String fistName;
	
	@Column(name = "lastName")
	private String lastName;
	
	@Email
	@Column(name = "mailAddress")
	private String mailAddress;

}
