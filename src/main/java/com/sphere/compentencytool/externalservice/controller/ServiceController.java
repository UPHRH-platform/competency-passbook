package com.sphere.compentencytool.externalservice.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sphere.compentencytool.external.service.ExternalService;

@RestController
@RequestMapping("/compentencytool/v1")
public class ServiceController {

	@Autowired
	ExternalService externalService;
	 @GetMapping("/user/read/{user_Id}")
		public ResponseEntity<String> UserRead(@RequestHeader Map<String,String> headers,@PathVariable (value = "user_Id") String UserID){
			return externalService.read_user(headers,UserID);
		}
	 
	 @GetMapping("/content/read/{Content_Id}")
		public ResponseEntity<String> ContentRead(@PathVariable(value = "Content_Id") String ContentID){
		 return externalService.read_content(ContentID);
		}
	 
	 @PostMapping(value = "/generate-token",consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
		public ResponseEntity<String> GenerateToken(String username,String password,String client_id,String grant_type){
		 System.out.println("generate token : "+username+password+client_id+grant_type);
		 
		 MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		 map.add("username",username);
		 map.add("password",password);
		 map.add("client_id",client_id);
		 map.add("grant_type",grant_type);
		 return externalService.Generate_token(map);
		
		}
	 
	 @GetMapping("/verify-token")
		public ResponseEntity<String> VerifyToken(@RequestHeader Map<String,String> headers){
		 System.out.println(headers);
		 return externalService.Verify_token(headers);
		}
	 
}
