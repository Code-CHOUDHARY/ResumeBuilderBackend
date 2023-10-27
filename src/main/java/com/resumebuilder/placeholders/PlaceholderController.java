package com.resumebuilder.placeholders;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/placeholder")
public class PlaceholderController {
	@Autowired
	private PlaceholderService service;
	
	@PostMapping("/addPlaceholders")
	public ResponseEntity<HttpStatus> addPlaceholdders(@RequestBody PlaceholderRequestBody placeholders){
		
		service.addPlaceholder(placeholders);
		
		return  ResponseEntity.ok(HttpStatus.OK);
	}
	@GetMapping("/getPlaceholders")
	public ResponseEntity<PlaceholderRequestBody> addPlaceholdders(){
		
		
		
		return  ResponseEntity.ok(this.service.getplaceholder());
	
	}
	
	
}
