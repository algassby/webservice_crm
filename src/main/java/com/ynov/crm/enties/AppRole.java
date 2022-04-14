/**
 * 
 */
package com.ynov.crm.enties;


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
 * @author algas
 *
 */
@Entity(name = "roles")
@Table(name = "roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true )
public class AppRole {

	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "roleId")
    private long roleId;
    @Column(name = "roleName", length = 20)
    private String roleName;

}
