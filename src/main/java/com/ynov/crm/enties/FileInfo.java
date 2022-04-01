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

import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author algas
 *
 */
@Entity(name = "FileInfo")
@Table(name = "FileInfo")
@Accessors(chain = true)
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
	@Column(name = "type")
	private String type;
	@Column(name = "size")
	private Long size;
	@ManyToOne(cascade = CascadeType.PERSIST , fetch = FetchType.LAZY)
	@JoinColumn(name = "customerId", nullable = true)
	private Customer customer;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orgaId", nullable = true)
	@JsonProperty(access = Access.WRITE_ONLY)
	private Organization organization;
	    
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdate;
	
}