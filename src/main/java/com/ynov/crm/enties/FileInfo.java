package com.ynov.crm.enties;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author algas
 *
 */
@Entity
@Table(name = "FileInfo")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FileInfo {
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "fileId")
	private String fileId;
	@Column(name = "fileName")
	private String fileName;
	@Column(name = "fileUrl")
	private String fileUrl;
	//@ManyToOne(fetch = FetchType.LAZY)
   // @JoinColumn(name = "customerId")
	//private Customer customer;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdate;
}