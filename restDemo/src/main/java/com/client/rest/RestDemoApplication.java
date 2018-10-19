package com.client.rest;
import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
public class RestDemoApplication implements CommandLineRunner {
	
	public static String dir = "c:\\venice";
	public static String namefile = "config.cfg";
	public static String pathfile = dir + "\\" + namefile;
	public static File file = null;
	public static String[] arrDatiConfig = new String[12];
	
	public static void main(String[] args) {
		SpringApplication.run(RestDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		String msg = "";
		String token = "";
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
				token = removeCh(token, token.length()-1);
				token = removeCh(token,0);
				
				// Si crea la seguente chiamata
				RestTemplate restTemplate1 = new RestTemplate();
				HttpHeaders headers1 = new HttpHeaders();
				headers1.setContentType(MediaType.APPLICATION_JSON);
				headers1.set("X-Auth", token);
				String resourceURL1 = arrDatiConfig[1];
				
				// Prima di fare la chiamata si deve aggiustare il UTC a -2 ore.
				this.GestioneUTC();
				
				String json1 = GestioneFileJSON.ConvertJSONDataTileFromAce(
						arrDatiConfig[4],arrDatiConfig[5],arrDatiConfig[6],
						arrDatiConfig[9],arrDatiConfig[10],arrDatiConfig[11]);
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

	
	public static String removeCh (String s , int index) {
		if ((index > s.length()-1) || (index < 0)) return null;
		String c = s.substring(0,index) + s.substring(index+1 , s.length());
		return c;
		}
	
	public void GestioneUTC() throws ParseException {
		
		// Data Ora Inzio
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmm");
		Date data_ora_Inizio = sdf.parse(arrDatiConfig[5] + arrDatiConfig[6]);
				
		Calendar cal = Calendar.getInstance();
	    cal.setTimeInMillis(data_ora_Inizio.getTime());

	    cal.add(Calendar.HOUR, -2);
	    Timestamp data_ora_Inizio_UTC = new Timestamp(cal.getTime().getTime());
	    System.out.println(data_ora_Inizio_UTC);
		
	    // Data Ora fine
		Date data_ora_Fine = sdf.parse(arrDatiConfig[7] + arrDatiConfig[8]);
		
		Calendar cal1 = Calendar.getInstance();
	    cal1.setTimeInMillis(data_ora_Fine.getTime());

	    cal1.add(Calendar.HOUR, -2);
	    Timestamp data_ora_Fine_UTC = new Timestamp(cal1.getTime().getTime());
	    System.out.println(data_ora_Fine_UTC);
	    
	    int granularita = Integer.parseInt(arrDatiConfig[9]);
	    int i = 1;
	    do {
	    	
	    	System.out.println("Chiamata: " + i);
	    	cal.add(Calendar.MINUTE, granularita);
			data_ora_Inizio_UTC = new Timestamp(cal.getTime().getTime());
			i++;
	    	
	    } while(data_ora_Inizio_UTC.before(data_ora_Fine_UTC));
		
		
		System.out.println(data_ora_Inizio_UTC);
		
		System.exit(0);
	
	}
}
