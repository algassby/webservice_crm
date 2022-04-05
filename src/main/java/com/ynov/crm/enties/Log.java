/**
 * 
 */
package com.ynov.crm.enties;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author algas
 *
 */
@Entity(name = "Log")
@Table(name = "Logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true )
public class Log {
	@Id
	@Column(name = "logId")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String logId;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "lastUpdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdate;
	
	@Column(name = "username")
	private String username;
	
	

}
