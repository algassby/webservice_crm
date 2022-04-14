package com.ynov.crm.restcontroller;



import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ynov.crm.requestdto.OrganizationRequestDto;
import com.ynov.crm.responsedto.OrganizationResponsDto;
import com.ynov.crm.responsedto.ResponseMessage;
import com.ynov.crm.service.OrganizationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
@RestController
@RequestMapping("/api/organizations")
@Tag(name = "Organization management", description = "Organization management")
public class OrganizationRestController {

	@Autowired
	private OrganizationService organizationService;

	@Operation(summary = "add a new organization", description = "add orga by dto", tags = { "Orgnization" })
	@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "successful operation, or error",
    content = @Content(schema = @Schema(implementation = OrganizationResponsDto.class))),
    @ApiResponse(responseCode = "405", description = "Validation exception", content = @Content(schema = 
    @Schema(implementation = MethodArgumentNotValidException.class)) ) })
	@PostMapping("/save")
	public ResponseEntity<?> saveOrganization(
			 @Parameter(description="OrganizationDto cannot null or empty.", 
	          required=true, schema=@Schema(implementation = OrganizationRequestDto.class))
			@Valid @RequestParam(name = "organization") String organizationRequestDto,
			@Parameter(description = "File is optional", required = false,
			content = @Content(array= @ArraySchema( schema = @Schema(implementation = MultipartFile.class))))
			@RequestParam(name = "file", required = false) MultipartFile file) throws JsonMappingException, JsonProcessingException {
	 ObjectMapper orgMapper = new ObjectMapper();
	 OrganizationRequestDto organization = orgMapper.readValue(organizationRequestDto, OrganizationRequestDto.class);
		return new ResponseEntity<>(organizationService.save(organization, file),HttpStatus.CREATED);
	}
	
	@Operation(summary = "Get all organizations", description = "Get all organization", tags = { "Organization" })
	@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "successful operation, or empty list",
    content = @Content(array = @ArraySchema(schema =  @Schema(implementation = OrganizationResponsDto.class)))),
    @ApiResponse(responseCode = "404", description = "Organization Not found", content = @Content(schema = 
    @Schema(implementation = RuntimeException.class)) ),
    @ApiResponse(responseCode = "405", description = "Validation exception", content = @Content(schema = 
    @Schema(implementation = MethodArgumentNotValidException.class)) ) })
	@GetMapping
	public ResponseEntity<?> getAllOrganization(@RequestParam(required = false, defaultValue = "0") Integer pageNo, @RequestParam( defaultValue = "10", required = false) Integer pageSize, @RequestParam(defaultValue = "name", required = false) String sortBy) {
		return new ResponseEntity<>(organizationService.findAll(pageNo, pageSize, sortBy), HttpStatus.OK);
	}
	
	@Operation(summary = "Get all organizations by a user", description = "Get all organization", tags = { "Organization" })
	@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "successful operation, or empty list",
    content = @Content(array = @ArraySchema(schema =  @Schema(implementation = OrganizationResponsDto.class)))),
    @ApiResponse(responseCode = "404", description = "Organization Not found", content = @Content(schema = 
    @Schema(implementation = RuntimeException.class)) ),
    @ApiResponse(responseCode = "405", description = "Validation exception", content = @Content(schema = 
    @Schema(implementation = MethodArgumentNotValidException.class)) ) })
	@GetMapping("/users/{userId}")
	public ResponseEntity<?>findAllByAdminId(
			@Parameter(description = "organizationId is required", required = true,
			content = @Content( schema = @Schema(implementation = String.class)))
			@PathVariable String userId, 
			@RequestParam(defaultValue = "0", required = false) Integer pageNo, 
			@RequestParam(defaultValue = "10", required = false) Integer pageSize,
			@RequestParam(defaultValue = "name", required = false) String sortBy) {
		try {
			return new ResponseEntity<>(organizationService.findAllByAdminId(userId, pageNo, pageSize, sortBy), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseMessage("empty list or amin not found"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	@Operation(summary = "Get a single organization", description = "get organization by his id", tags = { "Organization" })
	@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "successful operation, or error",
    content = @Content(schema = @Schema(implementation = OrganizationResponsDto.class))),
    @ApiResponse(responseCode = "404", description = "Organization Not found", content = @Content(schema = 
    @Schema(implementation = RuntimeException.class)) ),
    @ApiResponse(responseCode = "405", description = "Validation exception", content = @Content(schema = 
    @Schema(implementation = MethodArgumentNotValidException.class)) ) })
	@GetMapping("/{orgaId}")
	public ResponseEntity<?> findOrganizationById(
			@Parameter(description = "organizationId is required", required = true,
			content = @Content( schema = @Schema(implementation = String.class)))
			@Valid @PathVariable String orgaId) {
		if (orgaId == null) {
			return new ResponseEntity<>(new ResponseMessage("This name of organization is empty or null."), HttpStatus.BAD_REQUEST);

		} 
		if (organizationService.existsById(orgaId)) {
			return new ResponseEntity<>(
					organizationService.getOrganization(orgaId), HttpStatus.OK);
		} 

		return new ResponseEntity<>(new ResponseMessage("This organization is unknow or not Found."),HttpStatus.NOT_FOUND);
	

	}
	@Operation(summary = "Delete an existing organization", description = "delete by by his id", tags = { "Organization" })
	@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "successful operation, or error",
    content = @Content(schema = @Schema(implementation = ResponseMessage.class))),
    @ApiResponse(responseCode = "404", description = "Organization Not found", content = @Content(schema = 
    @Schema(implementation = RuntimeException.class)) ),
    @ApiResponse(responseCode = "405", description = "Validation exception", content = @Content(schema = 
    @Schema(implementation = MethodArgumentNotValidException.class)) ) })
	@DeleteMapping(value = "/{orgaId}/delete")
	public ResponseEntity<?> deleteOrganization(
			@Parameter(description = "organizationId is required", required = true,
			content = @Content( schema = @Schema(implementation = String.class)))
			@Valid @PathVariable String orgaId) {
		if (orgaId == null) {
			return new ResponseEntity<>(new ResponseMessage("This name of organization is empty or null."), HttpStatus.BAD_REQUEST);

		} 
		if (organizationService.existsById(orgaId)) {
			return new ResponseEntity<>(
					new ResponseMessage(organizationService.remove(orgaId)), HttpStatus.OK);

		} 
			
		return new ResponseEntity<>(new ResponseMessage("This name of organization is empty not found"), HttpStatus.NOT_FOUND);
		

	}

	@Operation(summary = "Update an existing organization", description = "Update a user by his id", tags = { "Organization" })
	@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "successful operation, or error",
    content = @Content(schema = @Schema(implementation = OrganizationResponsDto.class))),
    @ApiResponse(responseCode = "404", description = "Organization Not found", content = @Content(schema = 
    @Schema(implementation = RuntimeException.class)) ),
    @ApiResponse(responseCode = "405", description = "Validation exception", content = @Content(schema = 
    @Schema(implementation = MethodArgumentNotValidException.class)) ) })
	@PutMapping(value = "/{orgaId}/update")
	public ResponseEntity<?> updateOrganization(
			@Parameter(description = "organizationId is required", required = true,
			content = @Content(schema = @Schema(implementation = String.class)))
			@Valid @PathVariable String orgaId,
			@Parameter(description = "File is optional", required = false,
			content = @Content(array= @ArraySchema( schema = @Schema(implementation = OrganizationRequestDto.class))))
			@Valid @RequestParam(name = "organization")  String organizationRequestDto,
			@Parameter(description = "File is optional", required = false,
			content = @Content(array= @ArraySchema( schema = @Schema(implementation = MultipartFile.class))))
			@RequestParam(name = "file", required = false) MultipartFile file) throws JsonMappingException, JsonProcessingException {
		
		if (orgaId == null) {
			return new ResponseEntity<>(new ResponseMessage("This name of organization is empty or null."), HttpStatus.BAD_REQUEST);

		} 
		if (organizationService.existsById(orgaId)) {
			ObjectMapper orgMapper = new ObjectMapper();
			OrganizationRequestDto organization = orgMapper.readValue(organizationRequestDto, OrganizationRequestDto.class);
			OrganizationResponsDto organizationResponsDto = organizationService.update(organization, orgaId, file);
			return (organizationResponsDto.getOrgaId()!=null ? new ResponseEntity<>(organizationResponsDto, HttpStatus.OK):
						new ResponseEntity<>(new ResponseMessage("This admin cannot update the organization!"), HttpStatus.OK));

		} 
		return new ResponseEntity<>(new ResponseMessage("This organization is unknow and not found."), HttpStatus.NOT_FOUND);
		

	}

}
