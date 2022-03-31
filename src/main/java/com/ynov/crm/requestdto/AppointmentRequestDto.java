package com.ynov.crm.requestdto;



import java.sql.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class AppointmentRequestDto {
	
	@NotNull(message="label cannot null")
	@NotBlank(message="label cannot null")
	private String label;
	
	@NotNull(message="date cannot null")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
	private Date date;
	
	@Nullable
	private String place; 
	
	@NotNull(message="customerId cannot null")
	@NotBlank(message="customerId cannot null")
	private String customerId;


}
