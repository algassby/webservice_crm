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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ynov.crm.requestdto.OrganizationRequestDto;
import com.ynov.crm.service.OrganizationService;

@CrossOrigin(origins = "*", maxAge = 3600,allowedHeaders = "*")
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
		}
//		else if(organizationService.existsByName(orgaName)) {
//			return new ResponseEntity<>(organizationService.findByName(orgaName),HttpStatus.FOUND);
//		}
	else {
			return new ResponseEntity<>("This organization is unknow.",HttpStatus.NOT_FOUND);
		}

	}
	
	@DeleteMapping(value = "/delete/{orgaName}")
    public ResponseEntity<?> deleteOrganization(@Valid @PathVariable String orgaName){
		if(orgaName.isBlank()||orgaName.isEmpty()||orgaName.equals("")) {
			return new ResponseEntity<>("This name of organization is empty or null.",HttpStatus.BAD_REQUEST);
			
		}else if(organizationService.existsByName(orgaName)) {
			return new ResponseEntity<>(organizationService
					.remove(organizationService
					.findByName(orgaName)
					.getOrgaId()),HttpStatus.OK);
			
		}else {
			return new ResponseEntity<>(organizationService
					.remove(organizationService
					.findByName(orgaName)
					.getOrgaId()),HttpStatus.NOT_FOUND);
		}
       
     
    }

    @PutMapping(value = "/update/{name}")
    public ResponseEntity<?> updateOrganization(@Valid @PathVariable String orgaName,
    		@Valid @RequestBody OrganizationRequestDto organizationRequestDto) {

    	if(orgaName.isBlank()||orgaName.isEmpty()||orgaName.equals("")) {
			return new ResponseEntity<>("This name of organization is empty or null.",HttpStatus.BAD_REQUEST);
			
		}else if(organizationService.existsByName(orgaName)) {		
			return new ResponseEntity<>(organizationService.update(organizationRequestDto,orgaName),HttpStatus.OK);
				
		}else {
			return new ResponseEntity<>("This organization is unknow.",HttpStatus.NOT_FOUND);
		}
    	

       
    }

//    @PostMapping(value = "{orgaName}/customer/add")
//    public ResponseEntity<?> saveCustomerToOrganization(@Valid @PathVariable String orgaName
//    		,@Valid @PathVariable String UserNameCustomer) {
//    	//test organization exist
//    	if(orgaName.isBlank()||orgaName.isEmpty()||orgaName.equals("")) {
//			return new ResponseEntity<>("This name of organization is empty or null.",HttpStatus.BAD_REQUEST);
//			
//		}else if(!organizationService.existsByName(orgaName)) {
//			return new ResponseEntity<>("This organization is unknow.",HttpStatus.NOT_FOUND);
//		}
//    	//test user existe
//    	if(orgaName.isBlank()||orgaName.isEmpty()||orgaName.equals("")) {
//			return new ResponseEntity<>("This surname of customer is empty or null.",HttpStatus.BAD_REQUEST);
//			
//		}else if(organizationService.existsByName(orgaName)) {
//			return new ResponseEntity<>("This organization is unknow.",HttpStatus.NOT_FOUND);
//		}
//	}
//
//    @DeleteMapping(value = "{orgaName}/customer/delete/{surnameCustomer}")
//	public String removeCustomerToOrganization(String orgaName,String userName);

}
