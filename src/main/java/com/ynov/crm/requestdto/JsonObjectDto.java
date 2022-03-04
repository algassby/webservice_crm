/**
 * 
 */
package com.ynov.crm.requestdto;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author algas
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class JsonObjectDto {
 

	private Set<String> images = new HashSet<>();
}
