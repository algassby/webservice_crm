/**
 * 
 */
package com.ynov.crm.requestdto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author algas
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true )
public class CustomerRequestDto {

	private String  firstName;
	private String  lastName;
	private String  phoneNumer;
	private String orgaId;
}
