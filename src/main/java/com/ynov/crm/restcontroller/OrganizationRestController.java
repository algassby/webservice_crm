package com.ynov.crm.restcontroller;



import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
@RestController
@RequestMapping("/api/organizations")
@Tag(name = "Organization management", description = "Organization management")
public class OrganizationRestController {

	@Autowired
	private OrganizationService organizationService;

	@PostMapping("/save")
	public ResponseEntity<?> saveOrganization(@Valid @RequestParam(name = "organization") String organizationRequestDto, @RequestParam(name = "file", required = false) MultipartFile file) throws JsonMappingException, JsonProcessingException {
	 ObjectMapper orgMapper = new ObjectMapper();
	 OrganizationRequestDto organization = orgMapper.readValue(organizationRequestDto, OrganizationRequestDto.class);
		return new ResponseEntity<>(organizationService.save(organization, file),HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<?> getAllOrganization(@RequestParam(required = false, defaultValue = "0") Integer pageNo, @RequestParam( defaultValue = "10", required = false) Integer pageSize, @RequestParam(defaultValue = "name", required = false) String sortBy) {
		return new ResponseEntity<>(organizationService.findAll(pageNo, pageSize, sortBy), HttpStatus.OK);
	}
	@GetMapping("/users/{userId}")
	public ResponseEntity<?>findAllByAdminId(@PathVariable String userId, @RequestParam(defaultValue = "0", required = false) Integer pageNo, @RequestParam(defaultValue = "10", required = false) Integer pageSize,@RequestParam(defaultValue = "name", required = false) String sortBy) {
		try {
			return new ResponseEntity<>(organizationService.findAllByAdminId(userId, pageNo, pageSize, sortBy), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	

	@GetMapping("/{orgaId}")
	public ResponseEntity<?> findOrganizationById(@Valid @PathVariable String orgaId) {
		if (orgaId == null) {
			return new ResponseEntity<>(new ResponseMessage("This name of organization is empty or null."), HttpStatus.BAD_REQUEST);

		} 
		if (organizationService.existsById(orgaId)) {
			return new ResponseEntity<>(
					organizationService.getOrganization(orgaId), HttpStatus.OK);
		} 

		return new ResponseEntity<>(new ResponseMessage("This organization is unknow or not Found."),HttpStatus.NOT_FOUND);
	

	}

	@DeleteMapping(value = "/{orgaId}/delete")
	public ResponseEntity<?> deleteOrganization(@Valid @PathVariable String orgaId) {
		if (orgaId == null) {
			return new ResponseEntity<>(new ResponseMessage("This name of organization is empty or null."), HttpStatus.BAD_REQUEST);

		} 
		if (organizationService.existsById(orgaId)) {
			return new ResponseEntity<>(
					new ResponseMessage(organizationService.remove(orgaId)), HttpStatus.OK);

		} 
			
		return new ResponseEntity<>(new ResponseMessage("This name of organization is empty not found"), HttpStatus.NOT_FOUND);
		

	}

	@PutMapping(value = "/{orgaId}/update")
	public ResponseEntity<?> updateOrganization(@Valid @PathVariable String orgaId,
			@Valid @RequestParam(name = "organization")  String organizationRequestDto, @RequestParam(name = "file", required = false) MultipartFile file) throws JsonMappingException, JsonProcessingException {
		
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
