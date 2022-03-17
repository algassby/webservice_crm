/**
 * 
 */
package com.ynov.crm.utils;




import org.springframework.stereotype.Service;

import com.ynov.crm.service.UserPrinciple;

import lombok.Getter;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author algas
 *
 */

@Getter
@Setter
@Slf4j
@Service
public class CheckAccessAdmin {


	public Boolean checkAccess(String adminId, UserPrinciple principle) {
		
		return (principle.getUserId().equals(adminId) || principle.getUsername().equals("admin"));
	}
}
