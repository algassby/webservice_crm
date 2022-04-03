/**
 * 
 */
package com.ynov.crm.restcontroller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ynov.crm.responsedto.ResponseMessage;
import com.ynov.crm.service.FileInfoService;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author algas
 *
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping("/api/files")
@Tag(name = "contact", description = "the Contact API")
public class FileRestController {

	
	private FileInfoService fileInfoServiceImpl;
	private FileInfoService fileInfoServiceOrga;

	/**
	 * @param fileInfoService
	 */
	@Autowired
	public FileRestController(@Qualifier(value = "FileInfoServiceImpl") FileInfoService fileInfoServiceImpl,
			@Qualifier(value = "FileInforServiceOrganization") FileInfoService fileInfoServiceOrga) {
		super();
		this.fileInfoServiceImpl = fileInfoServiceImpl;
		this.fileInfoServiceOrga = fileInfoServiceOrga;
	}
	@GetMapping
	public ResponseEntity<?> findAllFile(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10")
	Integer pageSize, @RequestParam(defaultValue = "lastUpdate")  String sortBy) {
		return new ResponseEntity<>(fileInfoServiceImpl.findAllFile(pageNo, pageSize, sortBy), HttpStatus.OK);
	}
	
	@GetMapping("/customer/{customerId}")
	public ResponseEntity<?> findAllFileByCustomerId(@PathVariable String customerId) {
		return new ResponseEntity<>(fileInfoServiceImpl.findAllFileByCustomer(customerId), HttpStatus.OK);
	}
	@GetMapping("/customer/download/{fileName:.+}")
	public ResponseEntity<?> downloadFileFromLocal(@PathVariable String fileName, HttpServletRequest httpRequest) {
		
		if(fileName==null) {
			return ResponseEntity.ok().body(new ResponseMessage("Invalid FileName"));
		}
		if(!fileInfoServiceImpl.existsByFileName(fileName)) {
			return ResponseEntity.ok().body(new ResponseMessage("The file doest not exits!"));
		}
		Resource  resource =  fileInfoServiceImpl.load(fileName);
		String contentType = null;
		try {
			contentType = httpRequest.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
 
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
	@GetMapping("/organization/download/{fileName:.+}")
	public ResponseEntity<?> downloadFileFromLocalOrganization(@PathVariable String fileName, HttpServletRequest httpRequest) {
		
		if(fileName==null) {
			return ResponseEntity.ok().body(new ResponseMessage("Invalid FileName"));
		}
		if(!fileInfoServiceOrga.existsByFileName(fileName)) {
			return ResponseEntity.ok().body(new ResponseMessage("The file doest not exits!"));
		}
		Resource  resource =  fileInfoServiceOrga.load(fileName);
		String contentType = null;
		try {
			contentType = httpRequest.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
 
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
	
	
}
