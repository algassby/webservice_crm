package com.ynov.crm;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.ynov.crm.enties.AppRole;
import com.ynov.crm.enties.AppUser;
import com.ynov.crm.repository.AppRoleRepository;
import com.ynov.crm.repository.AppUserRepository;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@EnableAsync
@OpenAPIDefinition(
        info = @Info(
                title = "CRM API",
                version = "1.0",
                description = "CRM API",
                license = @License(name = "Apache 2.0", url = "http://localhost:8084"),
                contact = @Contact(url = "http://localhost:8084", name = "MAS", email = "graphaltere@gmail.com")
        )
)
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
				user.setEmail("admin@admin.com").setFirstName("admin").setLastName("admin").setUserKey(encoder.encode("123456789")).setUsername("admin")
				.setRoles(roles).setLastUpdate(new Date()).setAdminId(user.getUserId());
			
				dao.save(user);
			}
			
			
		};
	}
	
	  @Bean("threadPoolTaskExecutor")
	    public TaskExecutor getAsyncExecutor() {
	        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	        executor.setCorePoolSize(20);
	        executor.setMaxPoolSize(1000);
	        executor.setWaitForTasksToCompleteOnShutdown(true);
	        executor.setThreadNamePrefix("Async-");
	        return executor;
	    }
	
	
	

}
