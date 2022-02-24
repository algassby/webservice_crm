package com.ynov.crm.enties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
/**
 * 
 * @author gonza
 *
 */

@Entity(name = "Organization")
@Table(name = "Organization")

@Data
@AllArgsConstructor
@NoArgsConstructor

@Accessors(chain = true )
public class Organization {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "orgaID")
	private String orgaID;
	@Column(name = "name")
	private String name;
	@Column(name = "Address")
	private String Address;
	
}
