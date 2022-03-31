package com.ynov.crm.restcontroller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ynov.crm.requestdto.OrganizationRequestDto;
import com.ynov.crm.responsedto.OrganizationResponsDto;
import com.ynov.crm.responsedto.ResponseMessage;
import com.ynov.crm.service.OrganizationService;

@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*")
@RestController
@RequestMapping("/api/organizations")
public class OrganizationRestController {

	@Autowired
	private OrganizationService organizationService;

	@PostMapping("/save")
	public ResponseEntity<?> saveOrganization(@Valid @RequestBody OrganizationRequestDto organizationRequestDto) {
	
		return new ResponseEntity<>(organizationService.save(organizationRequestDto),HttpStatus.CREATED);
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
					organizationService.remove(orgaId), HttpStatus.OK);

		} 
			
		return new ResponseEntity<>(new ResponseMessage("This name of organization is empty not found"), HttpStatus.NOT_FOUND);
		

	}

	@PutMapping(value = "/{orgaId}/update")
	public ResponseEntity<?> updateOrganization(@Valid @PathVariable String orgaId,
			@Valid @RequestBody OrganizationRequestDto organizationRequestDto) {
		
		if (orgaId == null) {
			return new ResponseEntity<>(new ResponseMessage("This name of organization is empty or null."), HttpStatus.BAD_REQUEST);

		} 
		if (organizationService.existsById(orgaId)) {
			OrganizationResponsDto organizationResponsDto = organizationService.update(organizationRequestDto, orgaId);
			return (organizationResponsDto.getOrgaId()!=null ? new ResponseEntity<>(organizationResponsDto, HttpStatus.OK):
						new ResponseEntity<>(new ResponseMessage("This admin cannot update the organization!"), HttpStatus.OK));

		} 
		return new ResponseEntity<>(new ResponseMessage("This organization is unknow and not found."), HttpStatus.NOT_FOUND);
		

	}

}
