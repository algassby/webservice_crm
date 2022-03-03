package com.ynov.crm.restcontroller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ynov.crm.requestdto.OrganizationRequestDto;
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
	public ResponseEntity<?> getAllOrganization() {
		return new ResponseEntity<>(organizationService.findAll(), HttpStatus.OK);
	}
	
	@GetMapping("/find/{orgaName}")
	public ResponseEntity<?> findOrganizationByName(@Valid @PathVariable String orgaName) {
		if(orgaName.isBlank()||orgaName.isEmpty()||orgaName.equals("")) {
			return new ResponseEntity<>("This name of organization is empty or null.",HttpStatus.BAD_REQUEST);

		if (!organizationRequestDto.verifObligatoryField()) {
			return new ResponseEntity<>("required field not found (name and/or address)",
					HttpStatus.BAD_REQUEST);
		}
		if (!organizationRequestDto.verifOptionalField()) {
			return new ResponseEntity<>("illegal argument for nbSalaris ",
					HttpStatus.BAD_REQUEST);
		}
		
		if (organizationService.existsByName(organizationRequestDto.getName())) {
			return new ResponseEntity<>("Organization " + organizationRequestDto.getName() + " already exist.",
					HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(organizationService.save(organizationRequestDto), HttpStatus.CREATED);
		}
	}

	@GetMapping("/find/{orgaId}")
	public ResponseEntity<?> findOrganizationByName(@Valid @PathVariable String orgaId) {

		if(organizationService.getOrganization(orgaId)!=null) {
			return new ResponseEntity<>(organizationService.getOrganization(orgaId),HttpStatus.FOUND);
		}else {
			return new ResponseEntity<>("This organization is unknow.",HttpStatus.NOT_FOUND);
		}

	}

	@DeleteMapping(value = "/delete/{orgaName}")
	public ResponseEntity<?> deleteOrganization(@Valid @PathVariable String orgaName) {
		if (orgaName.isBlank() || orgaName.isEmpty() || orgaName.equals("")) {
			return new ResponseEntity<>("This name of organization is empty or null.", HttpStatus.BAD_REQUEST);

		} else if (organizationService.existsByName(orgaName)) {
			return new ResponseEntity<>(
					organizationService.remove(organizationService.findByName(orgaName).getOrgaId()), HttpStatus.OK);

		} else {
			return new ResponseEntity<>("Organization " + orgaName + " is unknow", HttpStatus.NOT_FOUND);
		}

	}

	@PutMapping(value = "/update/{orgaName}")
	public ResponseEntity<?> updateOrganization(@Valid @PathVariable String orgaName,
			@Valid @RequestBody OrganizationRequestDto organizationRequestDto) {
		if (!organizationRequestDto.verifObligatoryField()) {
			return new ResponseEntity<>("required field not found (name and/or address)",
					HttpStatus.BAD_REQUEST);
		}

		if (orgaName.isBlank() || orgaName.isEmpty() || orgaName.equals("")) {
			return new ResponseEntity<>("This name of organization is empty or null.", HttpStatus.BAD_REQUEST);

		} else if (organizationService.existsByName(orgaName)) {
			return new ResponseEntity<>(organizationService.update(organizationRequestDto, orgaName), HttpStatus.OK);

		} else {
			return new ResponseEntity<>("This organization is unknow.", HttpStatus.NOT_FOUND);
		}

	}

}
