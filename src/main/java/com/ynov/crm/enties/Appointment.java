/**
 * 
 */
package com.ynov.crm.enties;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author gonza
 *
 */
@Entity(name = "Apointment")
@Table(name = "Apointment")

@Data
@AllArgsConstructor
@NoArgsConstructor

@Accessors(chain = true )
public class Appointment {
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "ApointId", length = 60)
	private String ApointId;
	
	@Column(name = "label")
	private String label;
	
	@Column(name = "date")
	private Date date;


}
