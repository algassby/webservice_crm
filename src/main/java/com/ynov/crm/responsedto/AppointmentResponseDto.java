package com.ynov.crm.responsedto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ynov.crm.enties.Customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true )
public class AppointmentResponseDto {
	
	private String  apointId;
	private String  label;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm", timezone = "GMT+1")	
	private Date date;
	private String place;
	private String customerId;
}
