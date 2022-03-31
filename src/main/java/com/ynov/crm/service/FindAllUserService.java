/**
 * 
 */
package com.ynov.crm.service;

import java.util.List;

import com.ynov.crm.responsedto.AppUserResponseDto;

/**
 * @author algas
 *
 */
public interface FindAllUserService {
	List<AppUserResponseDto> findAllUsersByPaging(Integer pageNo, Integer pageSize, String sortBy);
	
}
