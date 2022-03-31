/**
 * 
 */
package com.ynov.crm.responsedto;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ynov.crm.enties.Customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author algas
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FileInfoResponseDto {

	private String fileId;
	private String fileName;
	private String fileUrl;
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdate;
}
