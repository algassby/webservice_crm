/**
 * 
 */
package com.ynov.crm.service;

import com.ynov.crm.requestdto.AppUserRequestDto;
import com.ynov.crm.responsedto.AppUserResponseDto;

/**
 * @author algas
 *
 */
public interface ServiceCreateUser {
	AppUserResponseDto save(AppUserRequestDto userDto);
}
