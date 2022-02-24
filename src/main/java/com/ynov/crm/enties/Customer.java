package com.ynov.crm.enties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author gonza
 *
 */
@Entity(name = "Customer")
@Table(name = "Customer")

@Data
@AllArgsConstructor
@NoArgsConstructor

@Accessors(chain = true )
public class Customer {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "CustomerId")
	private String CustomerId;
	
	@Column(name = "fistName")
	private String fistName;
	
	@Column(name = "lastName")
	private String lastName;
	
	@Email
	@Column(name = "mailAddress")
	private String mailAddress;
	
	@Column(name = "numTel")
	private String numTel;
	
	@Column(name = "organizationId")
	private Organization organization;

}
