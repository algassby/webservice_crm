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

import com.ynov.crm.responsedto.CustomerResponseDto;
import com.ynov.crm.responsedto.FileInfoResponseDto;
import com.ynov.crm.responsedto.ResponseMessage;
import com.ynov.crm.service.FileInfoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author algas
 *
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping("/api/files")
@Tag(name = "File management", description = "API file to get files and load file content")
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

	@Operation(summary = "get All files of customer")
    @ApiResponse(responseCode = "200", description = "Get All file if exists or empty list", 
    content = @Content(array = @ArraySchema( schema = @Schema(implementation = FileInfoResponseDto.class))))
  
	@GetMapping
	public ResponseEntity<?> findAllFile(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10")
	Integer pageSize, @RequestParam(defaultValue = "lastUpdate")  String sortBy) {
		return new ResponseEntity<>(fileInfoServiceImpl.findAllFile(pageNo, pageSize, sortBy), HttpStatus.OK);
	}
	

	@Operation(summary = "get All Customer files")
    @ApiResponse(responseCode = "200", description = "Get All file if exists or empty list", 
    content = @Content(array = @ArraySchema( schema = @Schema(implementation = FileInfoResponseDto.class))))
    @ApiResponse(responseCode = "404", description = "customer not found")
	@GetMapping("/customer/{customerId}")
	public ResponseEntity<?> findAllFileByCustomerId(
			@Parameter(description = "customerId cannot be null or empty", required = true)
			@PathVariable String customerId) {
		return new ResponseEntity<>(fileInfoServiceImpl.findAllFileByCustomer(customerId), HttpStatus.OK);
	}
	
	@Operation(summary = "load cutsomer file by his name")
    @ApiResponse(responseCode = "200", description = "Get file if exist or an exception", 
    content = @Content( schema = @Schema(implementation = Resource.class)))
    @ApiResponse(responseCode = "404", description = "customer not found")
	@GetMapping("/customer/download/{fileName:.+}")
	public ResponseEntity<?> downloadFileFromLocal(
			@Parameter(description = "fileName cannot be null or empty", required = true)
			@PathVariable String fileName, 
			HttpServletRequest httpRequest) {
		
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
	
	
	@Operation(summary = "load cutsomer file by his name")
    @ApiResponse(responseCode = "200", description = "Get file if exist or an exception", 
    content = @Content( schema = @Schema(implementation = Resource.class)))
    @ApiResponse(responseCode = "404", description = "customer not found")
	@GetMapping("/organization/download/{fileName:.+}")
	public ResponseEntity<?> downloadFileFromLocalOrganization(
			@Parameter(description = "fileName cannot be null or empty", required = true)
			@PathVariable String fileName, 
			HttpServletRequest httpRequest) {
		
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
