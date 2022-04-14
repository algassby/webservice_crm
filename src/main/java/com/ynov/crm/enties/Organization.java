package com.ynov.crm.enties;

import java.util.HashSet;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
/**
 * 
 * @author gonza
 *
 */

@Entity(name = "Organization")
@Table(name = "Organization")


@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true )
@Getter
@Setter
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
	
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name="user_id")
	//@JsonProperty(access = Access.WRITE_ONLY)
	private AppUser appUser;
	
	@Column(name = "adminId")
	private String adminId;
	
	@OneToMany(cascade = CascadeType.ALL ,orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "organization", targetEntity=Customer.class)
	@Fetch(FetchMode.JOIN)
	private Set<Customer> customers = new HashSet<>();
	
	@OneToOne(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private FileInfo fileInfo;
	
	
	 public void setFileInfo(FileInfo fileInfo) {
         if (fileInfo == null) {
             if (this.fileInfo != null) {
                 this.fileInfo.setOrganization(null);
             }
         }
         else {
        	 fileInfo.setOrganization(this);
         }
         this.fileInfo = fileInfo;
     }
 
	
}
