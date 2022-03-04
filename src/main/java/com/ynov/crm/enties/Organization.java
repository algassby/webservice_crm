package com.ynov.crm.enties;

import java.util.HashSet;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

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
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "orgaId", length = 60)
	private String orgaId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "Address")
	private String address;
	
	@Column(name = "nbSalaris")
	private int nbSalaris;
	
	@Column(name = "logo")
	private String logo;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private AppUser user;
	
	
	@OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	private Set<Customer> customers = new HashSet<>();
	
}
