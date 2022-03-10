/**
 * 
 */
package com.ynov.crm.enties;



import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author algas
 *
 */
@Entity
@Table(name = "Customers")
//@NamedEntityGraph(name = "Customer.detail",
//attributeNodes = @NamedAttributeNode("fileInfos"))
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true )
@Getter
@Setter

public class Customer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -981807439411396303L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "customerId")
	private String  customerId;
	
	@Column(name = "firstName")
	private String  firstName;
	
	@Column(name = "lastName")
	private String  lastName;
	
	@Column(name = "phoneNumer",length = 12)
	private String  phoneNumer;
	
	@ManyToOne
	private Organization organization;
	//@JoinColumn(name = "fileId", referencedColumnName = "fileId")
	
	@OneToMany(cascade = CascadeType.ALL ,orphanRemoval = true, fetch = FetchType.EAGER)
	  @JoinTable(name="customers_images", 
      joinColumns=@JoinColumn(name=""), 
      inverseJoinColumns=@JoinColumn(name="file_id"))
	private Set<FileInfo> fileInfos =  new HashSet<>();
	
	
	
}
