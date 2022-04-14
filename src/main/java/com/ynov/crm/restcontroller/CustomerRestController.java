/**
 * 
 */
package com.ynov.crm.restcontroller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ynov.crm.requestdto.CustomerRequestDto;
import com.ynov.crm.requestdto.JsonObjectDto;
import com.ynov.crm.responsedto.CustomerResponseDto;
import com.ynov.crm.responsedto.ResponseMessage;
import com.ynov.crm.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author algas
 *
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
@RequestMapping("/api/customers")
@Tag(name = "Customer management", description = "Management of customer")
public class CustomerRestController {

	private CustomerService customerService;

	/**
	 * @param customerService
	 */
	@Autowired
	public CustomerRestController(CustomerService customerService) {
		super();
		this.customerService = customerService;
	}
	
	@Operation(summary = "get All Customers and paging")
    @ApiResponse(responseCode = "200", description = "Get All Customer, if has not contains any customer return empty list", 
    content = @Content(array = @ArraySchema( schema = @Schema(implementation = CustomerResponseDto.class))))
    @ApiResponse(responseCode = "404", description = "customer not found")
	@GetMapping
	public ResponseEntity<?> getAllCustomer(
			@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10")Integer pageSize,
			@RequestParam(defaultValue = "lastName")  String sortBy) {
		return new ResponseEntity<>(customerService.getAllCustomer(pageNo, pageSize, sortBy), HttpStatus.OK);
		
	}
	
	@Operation(summary = "get All Customers by organiazation")
    @ApiResponse(responseCode = "200", description = "Get All Customer inside orgization else nothing", 
    content = @Content(array = @ArraySchema( schema = @Schema(implementation = CustomerResponseDto.class))))
    @ApiResponse(responseCode = "404", description = "orgnizationId not found")
	
	@GetMapping("/orgnization/{orgnizationId}")
	public ResponseEntity<?> findAllByOrganization (
			@Parameter(description = "orgnizationId cannot be null or empty", required = true)
			@PathVariable String orgnizationId){
		return ResponseEntity.ok().body(customerService.findByOrganization(orgnizationId));
	}
	
	@Operation(summary = "get a single Customer")
    @ApiResponse(responseCode = "200", description = "Get a Customer if exist else message exce", 
    content = @Content(schema = @Schema(implementation = CustomerResponseDto.class)))
	@ApiResponse(responseCode = "400", description = "Invalid customerId")
    @ApiResponse(responseCode = "404", description = "customer not found")
	@GetMapping("/{customerId}")
	public ResponseEntity<?> getCustomer(
			@Parameter(description = "customerId can be null or empty", required = false)
			@PathVariable String customerId) {
		if(customerId ==null) {
			return new ResponseEntity<>(new ResponseMessage("User not found"), HttpStatus. NOT_FOUND); 
		}
		if(customerService.existsById(customerId)) {
			return new ResponseEntity<>(customerService.getCustomer(customerId), HttpStatus.OK); 
		}

		return new ResponseEntity<>(new ResponseMessage("User not found"), HttpStatus. NOT_FOUND); 

	}
	@Operation(summary = "Create a customer", description = "Create a new customer and add him in a orgnization", tags = { "Customer" })
	  @ApiResponses(value = {
		        @ApiResponse(responseCode = "200", description = "successful operation, Json message  or an error",
		        content = @Content(schema = @Schema(implementation = CustomerResponseDto.class))),
		        @ApiResponse(responseCode = "405", description = "Validation exception", content = @Content(schema = 
		        @Schema(implementation = MethodArgumentNotValidException.class)) ) })
	@PostMapping("/save")
	public ResponseEntity<?> save(
			@Parameter(description = "customerRequestDto cannot be null or empty", required = true,
			content = @Content(schema = @Schema(implementation = CustomerRequestDto.class)))
			@Valid @RequestBody CustomerRequestDto customerRequestDto) {
		if(customerRequestDto.getOrgaId()==null) {
			return new ResponseEntity<>(new ResponseMessage("Organization inexistante"), HttpStatus.OK); 
		}
		CustomerResponseDto customerResponseDto = customerService.save(customerRequestDto);
		return customerResponseDto.getCustomerId()!=null ? new ResponseEntity<>(customerResponseDto, HttpStatus.OK)
				:  new ResponseEntity<>(new ResponseMessage("Cannot save the customer, because the organization is not found"), HttpStatus.OK);  
		
	}
	
	@Operation(summary = "Update an existing customer", description = "Update a customer if exists else send and message exception", tags = { "Customer" })
	  @ApiResponses(value = {
		        @ApiResponse(responseCode = "200", description = "successful operation, Json message  or an error",
		        content = @Content(schema = @Schema(implementation = CustomerResponseDto.class))),
		        @ApiResponse(responseCode = "404", description = "customer not found"),
		        @ApiResponse(responseCode = "405", description = "Validation exception", content = @Content(schema = 
		        @Schema(implementation = MethodArgumentNotValidException.class)) ) })
	@PutMapping("/{customerId}/update")
	public ResponseEntity<?> update(
			@Parameter(description = "customerRequestDto cannot be null or empty", required = true,
			content = @Content(schema = @Schema(implementation = CustomerRequestDto.class)))
			@Valid @RequestBody CustomerRequestDto customerRequestDto,
			@Parameter(description = "customerId cannot be null or empty", required = true)
			@PathVariable String customerId) {
		if(customerId ==null) {
			return new ResponseEntity<>(new ResponseMessage("User not found"), HttpStatus. NOT_FOUND); 
		}
		if(customerService.existsById(customerId)) {
			
			return new ResponseEntity<>(customerService.update(customerRequestDto, customerId), HttpStatus.OK); 
		}
		return new ResponseEntity<>(new ResponseMessage("Cannot  Updated User, because doest not exists!"), HttpStatus.BAD_REQUEST); 
		
	}
	
	@Operation(summary = "Delete a customer by id", description = "", tags = { "Customer" })
	@ApiResponses(value = { 
	@ApiResponse(responseCode = "200", description = "successful operation",
			content = @Content(schema = @Schema(implementation = ResponseMessage.class))),
	@ApiResponse(responseCode = "404", description = "customer not found") })
	@DeleteMapping("/{customerId}/delete")
	public ResponseEntity<?> update(
			@Parameter(description = "customerId cannot be null or empty", required = true)
			@PathVariable String customerId) {
		return new ResponseEntity<>(new ResponseMessage(customerService.delete(customerId)), HttpStatus.OK); 
		
	}
	
	@Operation(summary = "Add image to customer ", description = "", tags = { "Customer" })
	@ApiResponses(value = { 
	@ApiResponse(responseCode = "200", description = "successful operation",
			content = @Content(schema = @Schema(implementation = ResponseMessage.class))),
	@ApiResponse(responseCode = "404", description = "customer not found") })
	@PutMapping("/update/{customerId}/images/save")
	public ResponseEntity<?> addManyImage(
			@Parameter(description = "customerId cannot be null or empty", required = true)
			@PathVariable  String customerId,
			@Parameter(description = "File is optional", required = false,
			content = @Content(array= @ArraySchema( schema = @Schema(implementation = MultipartFile.class))))
			@RequestParam(value = "files") MultipartFile files []) {
		if(files.length==0) {
			return new ResponseEntity<>(new ResponseMessage("Cannot  Upload images to customer, cause file is empty!"), HttpStatus.BAD_REQUEST); 
		}
		return new ResponseEntity<>(new ResponseMessage(customerService.addAllImageToCustomer(customerId, files)), HttpStatus.OK); 
		
	}
	
	@Operation(summary = "remove image to customer ", description = "", tags = { "Customer" })
	@ApiResponses(value = { 
	@ApiResponse(responseCode = "200", description = "successful operation",
			content = @Content(schema = @Schema(implementation = ResponseMessage.class))),
	@ApiResponse(responseCode = "404", description = "customer not found") })
	@PutMapping("/update/{customerId}/images/delete")
	public ResponseEntity<?> removeManyImageToCustomer(
			@Parameter(description = "customerId cannot be null or empty", required = true)
			@PathVariable  String customerId,
			@Parameter(description = "JsonObjectDto cannot be null or empty", required = true,
			content = @Content(schema = @Schema(implementation = JsonObjectDto.class)))
			@RequestBody JsonObjectDto jsonRequestDto) {
		if(jsonRequestDto.getImages().isEmpty()) {
			return new ResponseEntity<>(new ResponseMessage("Cannot  delete images from customer, cause files list is empty!"), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(new ResponseMessage(customerService.removeManyImageToCustomer(customerId, jsonRequestDto)), HttpStatus.OK); 
		
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

	
}
