package com.ynov.crm.restcontroller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ynov.crm.requestdto.AppointmentRequestDto;
import com.ynov.crm.responsedto.AppointmentResponseDto;
import com.ynov.crm.responsedto.ResponseMessage;
import com.ynov.crm.service.AppointmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.info.Contact;

@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
@RestController
@RequestMapping("/api/appointment")

@Tag(name = "Appointment", description = "Manage the customer appointment")
public class ApointmentRestController {

	@Autowired
	private AppointmentService appointmentService;

	 @Operation(summary = "Add a new Appoitment to For customer", description = "", tags = { "Appoitment" })
	    @ApiResponses(value = { 
	        @ApiResponse(responseCode = "201", description = "Appoitment created",
	                content = @Content(schema = @Schema(implementation = AppointmentResponseDto.class))), 
	        @ApiResponse(responseCode = "400", description = "Invalid input"), 
	        @ApiResponse(responseCode = "409", description = "Appointment  already exists") })	
	@PostMapping("/save")
	public ResponseEntity<?> saveAppointment( 
			@Parameter(description="Appoitment to add. Cannot null or empty.", 
            required=true, schema=@Schema(implementation = Contact.class))
			@RequestBody AppointmentRequestDto appointmentRequestDto) {
		return appointmentService.save(appointmentRequestDto);
	}

	 @Operation(summary = "Update an existing Appoitment", description = "", tags = { "Appoitment" })
	 @ApiResponses(value = {
	      @ApiResponse(responseCode = "200", description = "successful operation"),
	      @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
	      @ApiResponse(responseCode = "404", description = "Appointment not found"),
	      @ApiResponse(responseCode = "405", description = "Validation exception") })
	@PutMapping("/{appointmentId}/update")
	public ResponseEntity<?> update( @Parameter(description="Appoitment to update. Cannot null or empty.", 
            required=true, schema=@Schema(implementation = AppointmentRequestDto.class))
			@RequestBody AppointmentRequestDto appointmentRequestDto,
			@Parameter(description="Id of the Appointment to be update. Cannot be empty.", 
              required=true)	@PathVariable String appointmentId) {
		return appointmentService.update(appointmentRequestDto,  appointmentId);
	}

	@Operation(summary = "Delete a appointment by id", description = "", tags = { "Appointment" })
	@ApiResponses(value = { 
	@ApiResponse(responseCode = "200", description = "successful operation", 
			content = @Content(schema = @Schema(implementation = ResponseMessage.class))),
	@ApiResponse(responseCode = "404", description = "appoitment not found") })
	@DeleteMapping("/{appointmentId}/delete")
	public ResponseEntity<?> update(
			 @Parameter(description="Id of the contact to be delete. Cannot be empty.",
             required=true)
			@Valid @PathVariable String appointmentId) {
		return appointmentService.remove(appointmentId);

	}

	  @Operation(summary = "Find Appoitment by ID", description = "Returns a single Appointment", tags = { "Appointment" })
	    @ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "successful operation",
	                content = @Content(schema = @Schema(implementation = AppointmentResponseDto.class))),
	    @ApiResponse(responseCode = "404", description = "Contact not found") })
	@GetMapping("/{appointmentId}")
	public ResponseEntity<?> findAppointmentById( 
			@Parameter(description="appoitment cannot be empty or null", required = true)
			@Valid @PathVariable String appointmentId) {
		return appointmentService.getAppointment(appointmentId);
	}
	  
	@Operation(summary = "Find Appointment by Customer Id", description = "Search by CustomerId", tags = { "Appoitement" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation, return appointment if exists else not found", 
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = AppointmentResponseDto.class)))) })	
	@GetMapping("/customer/{customerId}")
	public ResponseEntity<?> findAppointmentByCustomer(
			@Parameter(description="customerId cannot be empty or null", required = true)
			@Valid @PathVariable String customerId,
			@Parameter(description="Page number, default is 1") 
			@Valid @RequestParam int page) {
		return appointmentService.getAppointmentByCustommer(customerId, page);
				
	}

}
