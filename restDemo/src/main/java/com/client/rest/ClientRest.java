package com.client.rest;
import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
import com.client.file.GestioneFileJSON;

@SpringBootApplication
public class ClientRest implements CommandLineRunner {
	
	public static final String DIR = "c:\\venice";
	public static final String NAMEFILECONFIG = "config.cfg";
	public static final String PATHFILECONFIG = DIR + "\\" + NAMEFILECONFIG;
	public static File file = null;
	public static String[] arrDatiConfig = new String[12];
	public static String token = "";
	public static Calendar DateTimeInizioProcesso = Calendar.getInstance();
	public static Calendar DateTimeFineProcesso = Calendar.getInstance();
	public static final long start  = System.currentTimeMillis();
	
	
	public static void main(String[] args) {
		SpringApplication.run(ClientRest.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Rome"));
		
		DateTimeInizioProcesso.setTime(new Date());
		

		String msg = "";
		GestioneFile gf = new GestioneFile();
		
		// Creo il file di configurazione
		gf.creareFileConfig();
		
		// Leggere il file config.cfg per prendere tutti i parametri
		// ed inserirgli nell arrDatiConfig
		gf.FileReader(file);

		// fare la chiamata di autenticazione
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		try {
			// Faccio login
			// Url Login
			String resourceURL = arrDatiConfig[0];
			
			// Creo json con username ed password
			String json = GestioneFileJSON.ConvertJSONLogin(arrDatiConfig[2], arrDatiConfig[3]);
			
			// creo l'entità con il body ed headers
			HttpEntity<String> entity = new HttpEntity<String>(json, headers);
			
			// faccio la chiamata login
			ResponseEntity<String> response = restTemplate.exchange(resourceURL, HttpMethod.POST, entity, String.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				msg = "Utente autorizato";
				System.out.println(msg);
				
				// Si tutto OK si prende il token per la seguente chiamata
				token = response.getHeaders().get("X-Auth").toString();
				
				// il token si pulisce dei due caratteri [ ] de inizio e fine
				token = Funzioni.removeCh(token, token.length()-1);
				token = Funzioni.removeCh(token,0);
							
				// Aggiustare il UTC e fare le chiamate succesive
				UTC.GestioneUTC();
				
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
}
