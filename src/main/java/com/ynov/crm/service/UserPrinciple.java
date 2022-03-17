package com.ynov.crm.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import java.util.stream.Collectors;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.ynov.crm.enties.AppUser;

import lombok.Data;

@Data

public class UserPrinciple implements UserDetails {
	private static final long serialVersionUID = 1L;

	private String userId;
    private String firstName;
    private String lastName;
    private String username;
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;
    private String email;
    private String adminId;
   	@Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
 
    
    

    private Collection<? extends GrantedAuthority> authorities;



    /**
	 * @param userId
	 * @param firstName
	 * @param lastName
	 * @param username
	 * @param email
	 * @param password
	 * @param authorities
	 */
	public UserPrinciple(String userId, String firstName, String lastName, String username, String email,
			String password, String adminId , Date lastUpdate,Collection<? extends GrantedAuthority> authorities) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
	}

	public static UserPrinciple build(AppUser user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getRoleName())
        ).collect(Collectors.toList());

        return new UserPrinciple( user.getUserId(), user.getFirstName(),user.getLastName(), user.getUsername(), user.getEmail(),
        		user.getPassword(),user.getAdminId(), user.getLastUpdate(), authorities
               
               
        );
    }

    

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

	

    
    
}