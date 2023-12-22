package com.sphere.compentencytool.external.service.impl;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.sphere.compentencytool.common.utils.AppProperties;
import com.sphere.compentencytool.external.service.ExternalService;

@Service
public class ExternalServiceImpl implements ExternalService {

	RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	AppProperties props;
	
	@Override
	public ResponseEntity<String> read_user(Map<String,String> headers,String UserID) {
		// TODO Auto-generated method stub
		 System.out.println(headers.get("authorization"));
		 System.out.println(headers.get("x-authenticated-user-token"));
		System.out.println(UserID);
		
		HttpHeaders header=new HttpHeaders();
		header.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//		header.add("Authorization", "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJYUkZWVDBidDlBNGdsWm5uSUF5d1BJYWFzdjRReGFHWSJ9.APB-Ma_1l_R5l0xRddDhhlYkxBxxwZzcQofyhoif2bE" );
//		header.add("x-authenticated-user-token","eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJkelFFNjdiRmxRN0V2eUF3Tktndmk1X2ZQR0dsVUVKOGEyMnFlZ1R0TFU0In0.eyJqdGkiOiJkN2RhNzJjZC0wZDI0LTRlYzItOGYxYi1iODk2MjAwM2VmNDIiLCJleHAiOjE2NjY4NDg0NjgsIm5iZiI6MCwiaWF0IjoxNjY2NzYyMDY4LCJpc3MiOiJodHRwczovL2Fhc3RyaWthLXN0YWdlLnRhcmVudG8uY29tL2F1dGgvcmVhbG1zL3N1bmJpcmQiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiZjo5MDdiNWM2NC0xZDc5LTQ0ZGItYjNiNS1lYzEyOWQ1N2Y0MjE6NjY3OGU4YWMtN2U3Ni00YWYzLTgzNTQtYzA5NzBkZDc4NDE5IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoicG9ydGFsIiwiYXV0aF90aW1lIjowLCJzZXNzaW9uX3N0YXRlIjoiYTA0NGQ3MGMtODdlOC00ZGYxLWJjMTctNjZhZjQzYzFiZDNmIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL2FkbWluLWFhc3RyaWthLXN0YWdlLnRhcmVudG8uY29tIiwiaHR0cHM6Ly9hYXN0cmlrYS1zdGFnZS50YXJlbnRvLmNvbS8qIiwiaHR0cHM6L2NicC1hYXN0cmlrYS1zdGFnZS50YXJlbnRvLmNvbSIsImh0dHBzOi8vb3JnLWFhc3RyaWthLXN0YWdlLnRhcmVudG8uY29tIiwiaHR0cDovL2xvY2FsaG9zdDozMDAwIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiIiLCJuYW1lIjoiQW5raXRhIEthdXNoaWsiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbmtpdGFrYXVzaGlrX3M5dTMiLCJnaXZlbl9uYW1lIjoiQW5raXRhIiwiZmFtaWx5X25hbWUiOiJLYXVzaGlrIiwiZW1haWwiOiJzcCoqKioqKioqKioqKioqKkB5b3BtYWlsLmNvbSJ9.qyI1VcSoA-YsUA4Gq2FvpO1O1AghyL4izbybMOdqPr0W8mqVM_pX_2_JzLxGJE7FyavSEDOrtMhvjpppmk70u48Uvt1nvW7jthPE_lpI9sIKwuQGx8byokG86ok9Pk0XXoDeG3vFw8BzlHJ8NSYAynXzxrCoCuis7yEUUoxGIqlJfDbdRiQrSvtlWywwtgsWdIA3UQx2UqslooNKMHL0UhWr-lArplzGtKupuLNHcmVFVvDKFUMMWw9YE6X9wFraBeYt-hPkqM70fREafucs_umruiVtimvgklCMotM6UfPWXWvTAUeFLtXc6GFFphkmnU7kXEWFDGeApWsfs1rwvQ" );
		header.add("Authorization",headers.get("authorization"));
		header.add("x-authenticated-user-token",headers.get("x-authenticated-user-token"));
		HttpEntity<String> entity=new HttpEntity<String>("parameters",header);
		
		String url=props.getUserReadApi()+UserID;
		System.out.println(url);
		
		ResponseEntity<String> response=restTemplate.exchange(url,HttpMethod.GET,entity,String.class);
	
		return response;
	}

	@Override
	public ResponseEntity<String> read_content(String contentID) {
		// TODO Auto-generated method stub
		System.out.println("impl : "+contentID);
		HttpHeaders header=new HttpHeaders();
		header.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		
//		header.add("Cookie",headers.get("cookie") );
		HttpEntity<String> entity=new HttpEntity<String>("parameters",header);
		System.out.println(props.getContentReadAapi()+contentID);
		
		ResponseEntity<String> response=restTemplate.exchange(props.getContentReadAapi()+contentID,HttpMethod.GET,entity,String.class);
	
		return response;
		
	}

	@Override
	public ResponseEntity<String> Generate_token(MultiValueMap<String, String> map) {
		// TODO Auto-generated method stub
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> body_request = new HttpEntity<>(map, headers);

		ResponseEntity<String> response=restTemplate.exchange(props.getGenerateToken(),HttpMethod.POST,body_request,String.class);
		
		return response;
	}
	
	@Override
	public ResponseEntity<String> Verify_token(Map<String, String> headers) {
		// TODO Auto-generated method stub
		HttpHeaders header=new HttpHeaders();
		header.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		header.add("Authorization",headers.get("authorization") );
		HttpEntity<String> entity=new HttpEntity<String>("parameters",header);
		System.out.println(props.getVerifyToken());
		System.out.println(headers.get("authorization") );
		ResponseEntity<String> response=restTemplate.exchange(props.getVerifyToken(),HttpMethod.GET,entity,String.class);
	
		return response;
	}

}
