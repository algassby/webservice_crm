/**
 * 
 */
package com.ynov.crm.enties;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;

import lombok.experimental.Accessors;

/**
 * @author algas
 *
 */
@Entity(name = "Users")
@Table(name = "Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true )

public class AppUser {
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "userId", length = 60)
	private  String userId;
	
	@Column(name = "firstName", length = 60)
	private  String firstName;
	
	@Column(name = "lastName", length = 60)
	private  String lastName;
	
	@Column(name = "email",length = 60)
	@Email
	private  String email;
	
	@Column(name = "username", length = 60)
	@JsonProperty(access = Access.WRITE_ONLY)
	private  String username;
	
	@JsonProperty(access = Access.WRITE_ONLY)
    private String userKey;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinTable(name = "user_roles", 
    		joinColumns = {
            @JoinColumn(name = "user_id") }, 
    		inverseJoinColumns = {
            @JoinColumn(name = "role_id") })
    private Set<AppRole> roles = new HashSet<>();
	
	@Column(name = "admin_id", length = 60)
	private String adminId;
	@Column(name = "lastUpdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdate;

	@OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "appUser")
	private Set<Organization> organizations = new HashSet<>();
	
	
	public void addRole(AppRole appRole) {
		this.getRoles().add(appRole);
	}
	
}
