/**
 * 
 */
package com.ynov.crm.mapper;

import org.mapstruct.Mapper;

import com.ynov.crm.enties.AppUser;
import com.ynov.crm.requestdto.AppUserRequestDto;
import com.ynov.crm.responsedto.AppUserResponseDto;

/**
 * @author algas
 *
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
	
	public AppUser appUserRequestDtoToAppUser(AppUserRequestDto appUserRequestDto);
	public AppUserResponseDto appUserToAppUserResponseDto(AppUser appUser);
}
