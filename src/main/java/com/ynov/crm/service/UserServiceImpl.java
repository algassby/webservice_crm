/**
 * 
 */
package com.ynov.crm.service;



import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
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
import com.ynov.crm.mapper.UserMapper;
import com.ynov.crm.repository.AppRoleRepository;
import com.ynov.crm.repository.AppUserRepository;
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
	private FileInfoService fileService;
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
	public UserServiceImpl(AppUserRepository appUserRepo, UserMapper userMapper, AppRoleRepository appRoleRepo, FileInfoService fileService) {
		super();
		this.appUserRepo = appUserRepo;
		this.userMapper = userMapper;
		this.appRoleRepo = appRoleRepo;
		this.fileService = fileService;
	
	
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
	public List<AppUserResponseDto> findAllUsersByPaging(Integer pageNo, Integer pageSize,
			String sortBy) {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		 
        Page<AppUser> pagedResult = appUserRepo.findAll(paging);
        if(pagedResult.hasContent()) {
            return pagedResult.getContent().stream()
            		.map(user->userMapper.appUserToAppUserResponseDto(user))
            		.collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
	}


	@Override

	public AppUserResponseDto save(AppUserRequestDto userDto) {


		UserPrinciple currentUser  =  (UserPrinciple) SecurityContextHolder.getContext().getAuthentication(). getPrincipal();
		log.info(currentUser.toString());
		AppUser appUser =  userMapper.appUserRequestDtoToAppUser(userDto).setLastUpdate(new Date()).setAdminId(currentUser.getUserId()).setPassword(encoder.encode(userDto.getPassword()));

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
		//appUser.setPassword(this.encoder.encode(appUser.getPassword()));
		return userMapper.appUserToAppUserResponseDto(appUserRepo.save(appUser));
	}

	@Override
	public AppUserResponseDto update(AppUserRequestDto userDto, String userId) {
		
		UserPrinciple currentUser  =  (UserPrinciple) SecurityContextHolder. getContext(). getAuthentication(). getPrincipal();
		AppUser appUser =  appUserRepo.findById(userId).get();
		
		if(userDto.getRoleName()!=null) {
			if(!userDto.getRoleName().isEmpty()) {
				appUser.addRole(appRoleRepo.findByRoleName(userDto.getRoleName()));
				log.info(appRoleRepo.findByRoleName(userDto.getRoleName()).toString());
			}
			else {
				AppRole role = appRoleRepo.findByRoleName("ROLE_USER");
				appUser.addRole(role);
				log.info(role.toString());
			}
		}
		else {
			AppRole role = appRoleRepo.findByRoleName("ROLE_USER");
			appUser.addRole(role);
			log.info(role.toString());
		}
		log.info(appUser.getRoles().toString());
		appUser.setUsername(userDto.getUsername()).setFirstName(userDto.getFirstName()).setLastName(userDto.getLastName()).setEmail(userDto.getEmail())
		.setLastUpdate(new Date()).setAdminId(currentUser.getUserId());
		return userMapper.appUserToAppUserResponseDto(appUserRepo.save(appUser));
	}

	@Override
	public String removeUser(String userId) {
		appUserRepo.deleteById(userId);
		return "User Delete successfully";
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
		if(roleName!=null) {
			AppUser appUser = appUserRepo.findByUsername(username).get();
			AppRole  appRole = appRoleRepo.findByRoleName(roleName);
			appUser.getRoles().add(appRole);
			appUserRepo.save(appUser);
			return String.join(" ", "Role ", roleName , "added successffuly", "to", username);
		}
		return String.join(" ", "role doest not added to user");
	}

	@Override
	public String removeRoleToUser(String username, String roleName) {
		if(roleName!=null) {
			AppUser appUser = appUserRepo.findByUsername(username).get();
			appUser.getRoles().removeIf(role->role.getRoleName().equals(roleName));
			appUserRepo.save(appUser);
			return String.join(" ", "Role", roleName, "deleted successffuly", "to", username);
		}
		return String.join(" ", "role doest not removed to user");
		
		
	}

	

	
	

	
	

}
