package com.client.rest;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.client.file.GestioneFile;
import com.client.file.GestioneFileJSON;

public class UTC {

	public static void GestioneUTC() throws ParseException, IOException {

		// Data Ora Inzio
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmm");
		Date data_ora_Inizio = sdf.parse(RestDemoApplication.arrDatiConfig[5] + RestDemoApplication.arrDatiConfig[6]);

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(data_ora_Inizio.getTime());

		cal.add(Calendar.HOUR, -2);
		Timestamp data_ora_Inizio_UTC = new Timestamp(cal.getTime().getTime());
		System.out.println(data_ora_Inizio_UTC);

		// Data Ora fine
		Date data_ora_Fine = sdf.parse(RestDemoApplication.arrDatiConfig[7] + RestDemoApplication.arrDatiConfig[8]);

		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(data_ora_Fine.getTime());

		cal1.add(Calendar.HOUR, -2);
		Timestamp data_ora_Fine_UTC = new Timestamp(cal1.getTime().getTime());
		System.out.println(data_ora_Fine_UTC);

		int granularita = Integer.parseInt(RestDemoApplication.arrDatiConfig[9]);
		int i = 1;
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-Auth", RestDemoApplication.token);
		String URL = RestDemoApplication.arrDatiConfig[1];
		
		
		
		GestioneFile gf = new GestioneFile();
		String ace = RestDemoApplication.arrDatiConfig[4];
		ace = Funzioni.removeCh(ace, 6);
		ace = Funzioni.removeCh(ace, 2);
		String data = RestDemoApplication.arrDatiConfig[5];
		String ora = RestDemoApplication.arrDatiConfig[6];
		String datafine = RestDemoApplication.arrDatiConfig[7];
		String orafine = RestDemoApplication.arrDatiConfig[8];
		String pathDirChiamata = RestDemoApplication.DIR + "\\Chiamata " + ace + "_" + data + ora + "_" + datafine + orafine;
		gf.createDir(pathDirChiamata); 
		long totChiamate = ((cal1.getTimeInMillis() - cal.getTimeInMillis()))/(granularita*60*1000);
		
		do {

			System.out.println("Chiamata: " + i + " di " + totChiamate + " " + data_ora_Inizio_UTC);
			//Qui si preparano i dati per il corpo della chiamata
			String Body = GestioneFileJSON.ConvertJSONDataTileFromAce(
					RestDemoApplication.arrDatiConfig[4],RestDemoApplication.arrDatiConfig[5],RestDemoApplication.arrDatiConfig[6],
					RestDemoApplication.arrDatiConfig[9],RestDemoApplication.arrDatiConfig[10],RestDemoApplication.arrDatiConfig[11]);
			
            ResponseEntity<String> response = Chiamate.POST(URL, Body, headers);
			if (response.getStatusCode() == HttpStatus.OK) {

				//Creare e Inserire i dati nel file
				String pathFileChiamata = pathDirChiamata + "\\Chiamata " + ace + "_" + data + ora + ".txt";
				gf.createFile(pathFileChiamata,"chiamata");
				gf.fileWriter(pathFileChiamata, response.getBody());
			}
			
			cal.add(Calendar.MINUTE, granularita);
			data_ora_Inizio_UTC = new Timestamp(cal.getTime().getTime());
			SimpleDateFormat df = new SimpleDateFormat ("yyMMdd");
			RestDemoApplication.arrDatiConfig[5] = df.format(data_ora_Inizio_UTC);
			df = new SimpleDateFormat ("HHmm");
			RestDemoApplication.arrDatiConfig[6] = df.format(data_ora_Inizio_UTC);
			data = RestDemoApplication.arrDatiConfig[5];
			ora = RestDemoApplication.arrDatiConfig[6];
			
			i++;

		} while (data_ora_Inizio_UTC.before(data_ora_Fine_UTC));
		
		SimpleDateFormat sdfOra = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		RestDemoApplication.DateTimeFineProcesso.setTime(new Date());
		final long end  = System.currentTimeMillis();
		System.out.println("[Inizio]          " + sdfDateTime.format(RestDemoApplication.start));
		System.out.println("[Fine]            " + sdfDateTime.format(end));
		long tempofinale = end-RestDemoApplication.start;
		tempofinale = tempofinale - 3600000;
		System.out.println("[Tempo trascorso] " + sdfOra.format(tempofinale));

	}

}
