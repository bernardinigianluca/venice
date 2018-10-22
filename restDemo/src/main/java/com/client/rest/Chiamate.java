package com.client.rest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Chiamate {
	
	static RestTemplate restTemplate = new RestTemplate();

	public static ResponseEntity<String> POST(String URL, String Body, HttpHeaders headers) {
		
		HttpEntity<String> entity1 = new HttpEntity<String>(Body, headers);
		
		//Questa istruzione non funziona
		ResponseEntity<String> response1 = restTemplate.exchange(URL, HttpMethod.POST, entity1, String.class);

		return response1;

		
	}

}
