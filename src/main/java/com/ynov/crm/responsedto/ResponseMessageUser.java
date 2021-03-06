/**
 * 
 */
package com.ynov.crm.responsedto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author algas
 *
 */

@Getter
@Setter
public class ResponseMessageUser extends ResponseMessage {

	private AppUserResponseDto appUserResponseDto;
	public ResponseMessageUser(String message) {
		super(message);
	}
	

	/**
	 * @param message
	 * @param appUserResponseDto
	 */
	public ResponseMessageUser(String message, AppUserResponseDto appUserResponseDto) {
		super(message);
		this.appUserResponseDto = appUserResponseDto;
	}



	
	

}
