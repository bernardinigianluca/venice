package com.client.rest;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UTC {

	public void GestioneUTC() throws ParseException {

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
		do {

			System.out.println("Chiamata: " + i);
			cal.add(Calendar.MINUTE, granularita);
			data_ora_Inizio_UTC = new Timestamp(cal.getTime().getTime());
			i++;

		} while (data_ora_Inizio_UTC.before(data_ora_Fine_UTC));

		System.out.println(data_ora_Inizio_UTC);

		System.exit(0);

	}

}
