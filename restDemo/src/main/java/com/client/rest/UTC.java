package com.client.rest;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
		Date data_ora_Inizio = sdf.parse(ClientRest.arrDatiConfig[5] + ClientRest.arrDatiConfig[6]);

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(data_ora_Inizio.getTime());

		cal.add(Calendar.HOUR, -2);
		Timestamp data_ora_Inizio_UTC = new Timestamp(cal.getTime().getTime());
		System.out.println(data_ora_Inizio_UTC);

		// Data Ora fine
		Date data_ora_Fine = sdf.parse(ClientRest.arrDatiConfig[7] + ClientRest.arrDatiConfig[8]);

		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(data_ora_Fine.getTime());

		cal1.add(Calendar.HOUR, -2);
		Timestamp data_ora_Fine_UTC = new Timestamp(cal1.getTime().getTime());
		System.out.println(data_ora_Fine_UTC);

		int granularita = Integer.parseInt(ClientRest.arrDatiConfig[9]);
		int i = 1;
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-Auth", ClientRest.token);
		String URL = ClientRest.arrDatiConfig[1];
		
		GestioneFile gf = new GestioneFile();
		String ace = ClientRest.arrDatiConfig[4];
		ace = Funzioni.removeCh(ace, 6);
		ace = Funzioni.removeCh(ace, 2);
		String data = ClientRest.arrDatiConfig[5];
		String ora = ClientRest.arrDatiConfig[6];
		String pathDirChiamata = ClientRest.DIR + "\\Chiamata " + ace + "_" + data + ora;
		String pathFileChiamata = pathDirChiamata + "\\Chiamata " + ace + "_" + data + ora + ".txt";
		gf.createDir(pathDirChiamata); 
		gf.createFile(pathFileChiamata,"chiamata");
		
		do {

			System.out.println("Chiamata: " + i + " " + data_ora_Inizio_UTC);
			//Qui si preparano i dati per il corpo della chiamata
			String Body = GestioneFileJSON.ConvertJSONDataTileFromAce(
					ClientRest.arrDatiConfig[4],ClientRest.arrDatiConfig[5],ClientRest.arrDatiConfig[6],
					ClientRest.arrDatiConfig[9],ClientRest.arrDatiConfig[10],ClientRest.arrDatiConfig[11]);
			
			ResponseEntity<String> response = Chiamate.POST(URL, Body, headers);
			if (response.getStatusCode() == HttpStatus.OK) {
//				System.out.println(response.getStatusCode());
				//Inserire i dati nel file
				gf.fileWriter(pathFileChiamata, response.getBody());
//				System.out.println(response.getBody());
			}
			
			cal.add(Calendar.MINUTE, granularita);
			data_ora_Inizio_UTC = new Timestamp(cal.getTime().getTime());
			SimpleDateFormat df = new SimpleDateFormat ("YYMMDD");
			ClientRest.arrDatiConfig[5] = df.format(data_ora_Inizio_UTC);
			df = new SimpleDateFormat ("HHmm");
			ClientRest.arrDatiConfig[6] = df.format(data_ora_Inizio_UTC);
			
			i++;

		} while (data_ora_Inizio_UTC.before(data_ora_Fine_UTC));
		
		SimpleDateFormat sdfOra = new SimpleDateFormat("HH:mm:ss.SSS");
		SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		ClientRest.DateTimeFineProcesso.setTime(new Date());
		final long end  = System.currentTimeMillis();
		System.out.println("[Inizio]          " + sdfDateTime.format(ClientRest.start));
		System.out.println("[Fine]            " + sdfDateTime.format(end));
		long tempofinale = end-ClientRest.start;
		tempofinale = tempofinale - 3600000;
		System.out.println("[Tempo trascorso] " + sdfOra.format(tempofinale));

		System.exit(0);

	}

}
