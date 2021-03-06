/**
 * 
 */
package com.ynov.crm.service;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ynov.crm.enties.AppRole;
import com.ynov.crm.enties.AppUser;
import com.ynov.crm.enties.Log;
import com.ynov.crm.mapper.UserMapper;
import com.ynov.crm.repository.AppRoleRepository;
import com.ynov.crm.repository.AppUserRepository;
import com.ynov.crm.repository.LogRepository;
import com.ynov.crm.requestdto.AppUserRequestDto;
import com.ynov.crm.responsedto.AppUserResponseDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * @author algas
 *
 */
@Service
@Data
@NoArgsConstructor
@Transactional
@Slf4j

public class UserServiceImpl implements UserService {

	private AppUserRepository appUserRepo;
	private UserMapper userMapper;
	private AppRoleRepository  appRoleRepo;
	OrganizationService organizationService;
	private LogRepository logRepository;
	private String subject;
	private MailService mailService;
	private UserPrinciple currentUser;

	@Autowired
	private PasswordEncoder encoder;

	@Value("${crm.superAdmin}")
	private String superAdmin;
	
	
	/**
	 * @param appUserRepo
	 * @param userMapper
	 * @param appRoleRepo
	 */
	@Autowired
	public UserServiceImpl(AppUserRepository appUserRepo,
						   UserMapper userMapper,
						   AppRoleRepository appRoleRepo,
						   OrganizationService organizationService,
						   LogRepository logRepository,
						   MailService mailService
						   ) {
		super();
		this.appUserRepo = appUserRepo;
		this.userMapper = userMapper;
		this.appRoleRepo = appRoleRepo;
		this.organizationService =  organizationService;
		this.logRepository = logRepository;
		this.mailService = mailService;
	
	}
	/**
	 * init current user
	 */
	public void initCurrentUser(){
		 currentUser  =  (UserPrinciple) SecurityContextHolder.getContext().getAuthentication(). getPrincipal();
	}
	
	@Override
	public AppUserResponseDto findByEmail(String email) {
		return userMapper.appUserToAppUserResponseDto(appUserRepo.findByEmail(email));
	}

	@Override
	public AppUserResponseDto findByUsername(String username) {
		return userMapper.appUserToAppUserResponseDto(appUserRepo.findByUsername(username).get());
	}

	@Override
	public AppUserResponseDto getUser(String userId) {
	    return userMapper.appUserToAppUserResponseDto(appUserRepo.findById(userId).get());
	}

	@Override
	public List<AppUserResponseDto> getAllUsers() {
		this.initCurrentUser();
		log.debug(currentUser.toString());
		log.debug(String.valueOf(this.currentUser.getUsername().equals(getSuperAdmin())));
		if(this.currentUser.getUsername().equals(getSuperAdmin())) {
			return appUserRepo.findAll().stream().map(user->userMapper.appUserToAppUserResponseDto(user))
					.sorted(Comparator.comparing(AppUserResponseDto::getLastUpdate).reversed())
					.collect(Collectors.toList());
		}
		return appUserRepo.findAll().stream().map(user->userMapper.appUserToAppUserResponseDto(user))
				.sorted(Comparator.comparing(AppUserResponseDto::getLastUpdate).reversed())
				.filter(user->user.getUsername().equals(this.currentUser.getUsername()))
				.collect(Collectors.toList());
	}
	

	@Override
	public List<AppUserResponseDto> findAllUsersByPaging(Integer pageNo, Integer pageSize, String sortBy) {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		this.initCurrentUser();
		
		log.debug(String.valueOf(this.currentUser.getUsername().equals(getSuperAdmin())));
		 Page<AppUser> pagedResult = appUserRepo.findAll(paging);
		if(this.currentUser.getUsername().equals(getSuperAdmin())) {
			return pagedResult.stream().map(user->userMapper.appUserToAppUserResponseDto(user))
					.sorted(Comparator.comparing(AppUserResponseDto::getLastUpdate).reversed())
					.collect(Collectors.toList());
		}
		return pagedResult.stream().map(user->userMapper.appUserToAppUserResponseDto(user))
				.sorted(Comparator.comparing(AppUserResponseDto::getLastUpdate).reversed())
				.filter(user->user.getUsername().equals(this.currentUser.getUsername()))
				.collect(Collectors.toList());
      
	}


	@Override

	public AppUserResponseDto save(AppUserRequestDto userDto) {
		String text = String.join(" ",
				"Bonjour ",
				userDto.getFirstName(),
				userDto.getLastName().toUpperCase(),
				"votre compte a bien ??t?? cr????"
		);

		this.mailService.sendEmail("Ajout d'un customer", text, userDto.getEmail());

		UserPrinciple currentUser  =  (UserPrinciple) SecurityContextHolder. getContext(). getAuthentication(). getPrincipal();

		log.info(currentUser.toString());
		AppUser appUser =  userMapper.appUserRequestDtoToAppUser(userDto).setLastUpdate(new Date()).setAdminId(currentUser.getUserId()).setUserKey(encoder.encode(UUID.randomUUID().toString()));

		Set<AppRole> roles = appUser.getRoles();

		if(userDto.getRoleName()!=null) {
			if(userDto.getRoleName().isEmpty()) {
				AppRole role = appRoleRepo.findByRoleName("ROLE_USER");
				roles.add(role);
			}

			else {
				roles.add(appRoleRepo.findByRoleName(userDto.getRoleName()));
			}
		}
		else {
			roles.add(appRoleRepo.findByRoleName("ROLE_USER"));
		}

		appUser.setRoles(roles);
		return userMapper.appUserToAppUserResponseDto(appUserRepo.save(appUser));
	}

	@Override
	public AppUserResponseDto update(AppUserRequestDto userDto, String userId) {
		this.initCurrentUser();
		AppUser appUser =  appUserRepo.findById(userId).get();
		if(this.currentUser.getUserId().equals(appUser.getUserId())) {
			if(userDto.getRoleName()!=null) {
				appUser.addRole(appRoleRepo.findByRoleName(userDto.getRoleName()));
				AppRole role = appRoleRepo.findByRoleName("ROLE_USER");
				appUser.addRole(role);
				log.debug(role.toString());
				log.debug(appUser.getRoles().toString());
				
			  }
			appUser.setUsername(userDto.getUsername()).setFirstName(userDto.getFirstName()).setLastName(userDto.getLastName()).setEmail(userDto.getEmail())
			.setLastUpdate(new Date()).setAdminId(currentUser.getUserId());
			return userMapper.appUserToAppUserResponseDto(appUserRepo.save(appUser));

		}
		return new AppUserResponseDto();
		
	}

	@Override
	public String removeUser(String userId) {
		this.initCurrentUser();
		AppUser appUser =  appUserRepo.findById(userId).get();
		if(this.currentUser.getUserId().equals(appUser.getAdminId())) {
			if(!appUser.getOrganizations().isEmpty()) {
				appUser.getOrganizations().stream().forEach(organization->{
					organizationService.remove(organization.getOrgaId());
				});
			}
			appUserRepo.deleteById(userId);
			if(!appUserRepo.existsById(userId)) {
				logRepository.save(new Log().setUsername(currentUser.getUsername())
						.setDescription(new StringBuffer().append("L'admin").append(currentUser.getUsername()).append(" a supprimer l'admin ")
								.append(appUser.getUsername())
								.append(" ?? ").append(new Date()).append(".").toString()).setLastUpdate(new Date()));
			}
			return "User Delete successfully";
		}
		return new StringBuilder("The current admin ").append(appUser.getFirstName()).append("cannot remove the target admin").toString();
		
	}

	@Override
	public Boolean existsByEmail(String email) {
		return appUserRepo.existsByEmail(email);
	}

	@Override
	public Boolean existsByUsername(String email) {
		return appUserRepo.existsByUsername(email);
	}
	
	@Override
	public Boolean existsById(String userId) {
		
		return appUserRepo.existsById(userId);
	}

	@Override
	public String addRoleToUser(String username, String roleName) {
		this.initCurrentUser();
		AppUser appUser = appUserRepo.findByUsername(username).get();
		if(this.currentUser.getUserId().equals(appUser.getUserId())) {
			if(roleName!=null) {
				
				AppRole  appRole = appRoleRepo.findByRoleName(roleName);
				appUser.getRoles().add(appRole);
				appUserRepo.save(appUser);
				return String.join(" ", "Role ", roleName , "added successffuly", "to", username);
			}
			else {
				return String.join(" ", "role doest not added to user");
			}
		}
		return String.join(" ", " The current admin" , appUser.getFirstName(), "cannot add change the role");
		
	}

	@Override
	public String removeRoleToUser(String username, String roleName) {
		this.initCurrentUser();
		AppUser appUser = appUserRepo.findByUsername(username).get();
		if(this.currentUser.getUserId().equals(appUser.getUserId())) {
			if(roleName!=null) {
				
				appUser.getRoles().removeIf(role->role.getRoleName().equals(roleName));
				appUserRepo.save(appUser);
				return String.join(" ", "Role", roleName, "deleted successffuly", "to", username);
			}
			else {
				return String.join(" ", "role doest not change");
			}
			
		}
		
		return String.join(" ", " The current admin" , appUser.getFirstName(), "cannot add change the role");
		
		
	}

	

	
	

	
	

}
