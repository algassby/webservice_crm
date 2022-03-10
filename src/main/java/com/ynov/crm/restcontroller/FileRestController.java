/**
 * 
 */
package com.ynov.crm.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ynov.crm.service.FileInfoService;

/**
 * @author algas
 *
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/files")
public class FileRestController {

	private FileInfoService fileInfoService;

	/**
	 * @param fileInfoService
	 */
	@Autowired
	public FileRestController(FileInfoService fileInfoService) {
		super();
		this.fileInfoService = fileInfoService;
	}
	@GetMapping
	public ResponseEntity<?> findAllFile() {
		return new ResponseEntity<>(fileInfoService.findAllFile(), HttpStatus.OK);
	}
	@GetMapping("/customer")
	public ResponseEntity<?> findAllFileByCustomerId(@RequestParam(value = "customerId", required = false) String customerId) {
		return new ResponseEntity<>(fileInfoService.findAllFileByCustomer(customerId), HttpStatus.OK);
	}
	
	
}
