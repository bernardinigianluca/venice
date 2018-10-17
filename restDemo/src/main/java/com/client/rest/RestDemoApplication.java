package com.client.rest;

import java.util.Collections;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class RestDemoApplication implements CommandLineRunner {
	
//	public String msg = "";
//	public String token;

	public static void main(String[] args) {
		SpringApplication.run(RestDemoApplication.class, args);
	}

//	@SuppressWarnings("static-access")
	@Override
	public void run(String... args) throws Exception {

		String msg = "";
		String token = "";
		// Leggere il file di testo per prendere username e password

		// fare la chiamata di autenticazione
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		try {
			String resourceURL = "https://api.wexplore.olivetti.com/dmes/login";
			String json = ConvertJSONLogin("perugia", "HZe[A$fT7u22cy_v");
			HttpEntity<String> entity = new HttpEntity<String>(json, headers);
			ResponseEntity<String> response = restTemplate.exchange(resourceURL, HttpMethod.POST, entity, String.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				msg = "Utente autorizato";
				System.out.println(msg);
//				System.out.println("{\n" + "    \"message\": \"Authorized\"\n" + "}\n" + "");
				
				// Si tutto OK si prende il token per la seguente chiamata
				token = response.getHeaders().get("X-Auth").toString();
				System.out.println("token: " + token);

				// Si crea la seguente chiamata
//				headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
				RestTemplate restTemplate1 = new RestTemplate();
				HttpHeaders headers1 = new HttpHeaders();
				headers1.setContentType(MediaType.APPLICATION_JSON);
				headers1.set("X-Auth", token);
				String resourceURL1 = "https://api.wexplore.olivetti.com/dmes/getDataTileFromAce";
	////		String jsonGetData = ConvertJSONDataTileFromAce("perugia", "HZe[A$fT7u22cy_v");
				String json1 = "{\n" + 
								"\"ace\": \"10|054|039\",\n" + 
								"\"data\": \"171022\",\n" + 
								"\"ora\":   \"1030\" ,\n" + 
								"\"granularita\": \"15\",\n" + 
								"\"adjustment\": true,\n" + 
								"\"colors\": [\"P\",\"Ni\",\"Ns\",\"Tb\",\"Tc\",\"Gm\",\"Gf\",\"F1\",\"F2\",\"F3\",\"F4\",\"F5\",\"F6\",\"Vi\",\"Ve\",\"Vp\",\"Vr\"]\n" + 
								"}";
				HttpEntity<String> entity1 = new HttpEntity<String>(json1, headers1);
				
				//Questa istruzione non funziona
				System.out.println("hola");
				ResponseEntity<String> response1 = restTemplate1.exchange(resourceURL1, HttpMethod.POST, entity1, String.class);
				System.out.println(response1);
				if (response1.getStatusCode() == HttpStatus.OK) {
					System.out.println(response1);
					System.out.println("hola2");
				}
			}
			
		} catch (Exception e) {
			switch (e.getMessage()) {
			case "401 Unauthorized":
				msg = "Errore: 401\n" + "Utente non autorizato";
				System.out.println(msg);
				break;

			default:
				msg = "Errore: " + e.getMessage();
				System.out.println(msg);
				break;
			}
		}
	}

	public String ConvertJSONLogin(String username, String password) {
		String json = "";
		json = "{\n" + "\"username\":\"" + username + "\",\n" + "\"password\":\"" + password + "\"\n" + "}";
		return json;
	}
	
	public String ConvertJSONDataTileFromAce() {
		String json = "";
		
		return json;
	}
	
//	public String Response() {
//		OkHttpClient client = new OkHttpClient();
//
//		MediaType mediaType = MediaType.parse("application/json");
//		RequestBody body = RequestBody.create(mediaType, "{\r\n    \"username\": \"perugia\",\r\n    \"password\": \"HZe[A$fT7u22cy_v\"\r\n}");
//		Request request = new Request.Builder()
//		  .url("https://api.wexplore.olivetti.com/dmes/login")
//		  .post(body)
//		  .addHeader("Content-Type", "application/json")
//		  .addHeader("cache-control", "no-cache")
//		  .addHeader("Postman-Token", "45493d61-acba-427c-a8b4-0be772d914c0")
//		  .build();
//
//		Response response = client.newCall(request).execute();
//	}
}
