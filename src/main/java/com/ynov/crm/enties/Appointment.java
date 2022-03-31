/**
 * 
 */
package com.ynov.crm.enties;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author gonza
 *
 */
@Entity(name = "Appointment")
@Table(name = "Appointment")

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
	
	@NotNull(message = "label do not null")
	@Column(name = "label")
	private String label;
	
	@NotNull(message = "date do not null")
	@DateTimeFormat
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm", timezone = "GMT+1")
	@Column(name = "date")	
	private Date date;
	

	@Column(name="place")
	private String place;
	
	@ManyToOne
	@NotNull(message = "customerId do not null")
	@JoinColumn(name = "customerId")
	private Customer customer;


}
