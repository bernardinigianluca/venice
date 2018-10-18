package com.client.rest;

import java.io.File;

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

import com.client.file.GestioneFile;

@SpringBootApplication
public class RestDemoApplication implements CommandLineRunner {
	
	public static void main(String[] args) {
		SpringApplication.run(RestDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		String msg = "";
		String token = "";
		String[] config = new String[] {"urlLogin:https://api.wexplore.olivetti.com/dmes/login",
										"urlGetTile:https://api.wexplore.olivetti.com/dmes/getDataTileFromAce",
										"utente:perugia",
										"password:HZe[A$fT7u22cy_v",
										"ace:10|054|039",
										"dataInizio:171022",
										"orainizio:0000",
										"dataFine:171022",
										"orafine:0015",
										"granularita:15",
										"adjusment:true",
										"colori:[\"P\",\"Ni\",\"Ns\",\"Tb\",\"Tc\",\"Gm\",\"Gf\",\"F1\",\"F2\",\"F3\",\"F4\",\"F5\",\"F6\",\"Vi\",\"Ve\",\"Vp\",\"Vr\"]"};
	

		// Creazione Direttorio e File configurazione
		String dir = "c:\\venice";
		String namefile = "config.cfg";
		String pathfile = dir + "\\" + namefile;
		
		GestioneFile gf = new GestioneFile();
		gf.createDir(dir);
		File file = gf.createFile(pathfile);
		
		if(file.length()==0) {
			//Scrive multiline sul file con PrintWriter
			gf.fileWriteMultilinePrintWriter(file, config);
		}
		
		// Leggere il file config.cfg per prendere tutti i parametri
		try {
			String[] copia1 = gf.cloneArray(gf.FileReader(file));
		//String array[] = new gf.FileReader(file);
		System.out.println(copia1[0]);
		} catch (Exception e) {
			System.out.println(e);
			System.exit(0);
		}
		
		

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
				
				// Si tutto OK si prende il token per la seguente chiamata
				token = response.getHeaders().get("X-Auth").toString();
				System.out.println(token);
				token = removeCh(token, token.length()-1);
				token = removeCh(token,0);
				System.out.println("token: " + token);
				
				// Si crea la seguente chiamata
//				headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
				RestTemplate restTemplate1 = new RestTemplate();
				HttpHeaders headers1 = new HttpHeaders();
				headers1.setContentType(MediaType.APPLICATION_JSON);
				headers1.set("X-Auth", token);
				String resourceURL1 = "https://api.wexplore.olivetti.com/dmes/getDataTileFromAce";
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
				ResponseEntity<String> response1 = restTemplate1.exchange(resourceURL1, HttpMethod.POST, entity1, String.class);
				if (response1.getStatusCode() == HttpStatus.OK) {
					System.out.println(response1.getBody());
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

	public static String ConvertJSONLogin(String username, String password) {
		String json = "";
		json = "{\n" + "\"username\":\"" + username + "\",\n" + "\"password\":\"" + password + "\"\n" + "}";
		return json;
	}
	
	public static String ConvertJSONDataTileFromAce() {
		String json = "";
		
		return json;
	}
	
	public static String removeCh (String s , int index) {
		if ((index > s.length()-1) || (index < 0)) return null;
		String c = s.substring(0,index) + s.substring(index+1 , s.length());
		return c;
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
