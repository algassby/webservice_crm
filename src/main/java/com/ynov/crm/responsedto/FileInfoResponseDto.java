/**
 * 
 */
package com.ynov.crm.responsedto;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.ynov.crm.enties.Customer;
import com.ynov.crm.enties.Organization;

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
	@JsonProperty(access = Access.WRITE_ONLY)
	private Organization organization;
	private Date lastUpdate;
}
