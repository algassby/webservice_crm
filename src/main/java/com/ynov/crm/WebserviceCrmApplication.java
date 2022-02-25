package com.ynov.crm;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ynov.crm.enties.AppRole;
import com.ynov.crm.enties.AppUser;
import com.ynov.crm.repository.AppRoleRepository;
import com.ynov.crm.repository.AppUserRepository;

@SpringBootApplication
public class WebserviceCrmApplication {
	@Autowired
	PasswordEncoder encoder;
	public static void main(String[] args) {
		SpringApplication.run(WebserviceCrmApplication.class, args);
	}
	
	@Bean
	CommandLineRunner start(AppUserRepository dao, AppRoleRepository roleDao) {
		return args->{
			AppUser user = new AppUser();
			AppRole roleUser = new AppRole();
			
			roleUser.setRoleName("ROLE_USER");
			if(!roleDao.existsByRoleName("ROLE_USER")) {
					roleDao.save(roleUser);
			}
			
			
			AppRole roleAdmin = new AppRole();
			roleAdmin.setRoleName("ROLE_ADMIN");
			if(!roleDao.existsByRoleName("ROLE_ADMIN")) {
					roleDao.save(roleAdmin);
			}
			if(!dao.existsByEmail("admin@admin.com") && !dao.existsByUsername("admin")) {
				Set<AppRole> roles = new HashSet<>();
				roles.add(roleDao.findByRoleName("ROLE_ADMIN"));
				
				//user.setUserId(UUID.randomUUID().toString());
				user.setEmail("admin@admin.com").setFirstName("admin").setLastName("admin").setPassword(encoder.encode("123456789")).setUsername("admin")
				.setRoles(roles).setLastUpdate(new Date()).setAdminId(user.getUserId());
			
				dao.save(user);
			}
			
			
		};
	}
	

}
