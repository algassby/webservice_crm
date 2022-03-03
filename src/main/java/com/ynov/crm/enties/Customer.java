/**
 * 
 */
package com.ynov.crm.enties;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author algas
 *
 */
@Entity(name = "Customers")
@Table(name = "Customers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true )

public class Customer {

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
}
