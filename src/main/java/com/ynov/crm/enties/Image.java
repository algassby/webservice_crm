package com.ynov.crm.enties;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 
 * @author gonza
 *
 */
@Entity(name = "Image")
@Table(name = "Image")

@Data
@AllArgsConstructor
@NoArgsConstructor

@Accessors(chain = true )
public class Image {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "imageId")
	private String imageId;
	
	@Column(name = "path")
	private String path;
	
	@Column(name = "postDate")
	private Date postDate;
	
	@Column(name = "customer")
	private Customer customer;

}
