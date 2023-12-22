package com.sphere.compentencytool.external.service;


import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public interface ExternalService {

	public ResponseEntity<String> read_user(Map<String,String> headers,String UserID);
	public ResponseEntity<String> read_content(String ContentID);
	public ResponseEntity<String> Verify_token(Map<String, String> headers);
	public ResponseEntity<String> Generate_token(MultiValueMap<String, String> map);
	
}
