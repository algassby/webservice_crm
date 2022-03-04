/**
 * 
 */
package com.ynov.crm.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ynov.crm.requestdto.AppUserRequestDto;
import com.ynov.crm.responsedto.AppUserResponseDto;

/**
 * @author algas
 *
 */
public interface UserService {

	AppUserResponseDto findByEmail(String email);
	AppUserResponseDto findByUsername(String username);
	AppUserResponseDto getUser(String userId);
	List<AppUserResponseDto> getAllUsers();
	List<AppUserResponseDto> findAllUsersByPaging(Integer pageNo, Integer pageSize, String sortBy);
	AppUserResponseDto save(AppUserRequestDto userDto);
	AppUserResponseDto update(AppUserRequestDto userDto, String userId);
	String removeUser(String userId);
	Boolean existsByEmail(String email);
	Boolean existsByUsername(String email);
	Boolean existsById(String userId);
	String addRoleToUser(String username, String roleName);
	String removeRoleToUser(String username, String roleName);
	
}
