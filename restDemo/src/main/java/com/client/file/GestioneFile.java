package com.client.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.client.rest.ClientRest;

public class GestioneFile {
	
	/*====================================================*/
	public File createFile(String path, String key) throws IOException {
		File file = new File(path);
		
		switch (key) {
		case "config":
			if(!file.exists()) {
				file.createNewFile();
			}
			break;
		case "chiamata":
			if(!file.exists()) {
				file.createNewFile();
			}else {
				file.delete();
				file.createNewFile();
			}
			break;
		}
		return file;
	}
	
	public void createDir(String path) throws IOException {
		File dir = new File(path);
		if(!dir.exists()) {
			dir.mkdirs();
		}
	}
	
	/*Scrivere file=======================================*/
	public void fileWriter(String filePath, String text) throws IOException {
		
		File file = new File(filePath);

		FileWriter fw = new FileWriter(file, true);
		fw.write(text + "\n");
		fw.close();
	}
	
	public void fileWriteMultiline(File file, String[] text) throws IOException {
		
		BufferedWriter bw = null;		
		bw = new BufferedWriter(new FileWriter(file));
		
		for (String string : text) {
			bw.write(string);
			bw.newLine();
		}
		bw.close();
	}
	
	public void fileWriteMultilinePrintWriter(File file, String[] text) throws IOException {
		PrintWriter pw = null;
		
		pw = new PrintWriter(new FileWriter(file));
		
		for (String string : text) {
			pw.println(string);
		}
		pw.close();
	}

	/*LeggereFile e crea array arrDatiConfig*/
	public void FileReader(File file) throws IOException {
		
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String linea; int i = 0;
		
		while ((linea = reader.readLine()) != null){
			ClientRest.arrDatiConfig[i]=linea.substring(linea.indexOf(":")+1);
			i++;
		}
	}
	
	public String[] cloneArray(String[] array) {
        String[] newArray = new String[array.length];
        for (int i = 0; i < array.length; i++)
            newArray[i] = array[i];
        return newArray;
    }
	
	public void creareFileConfig() throws IOException {
			
		String[] config = new String[] {
				"urlLogin:https://api.wexplore.olivetti.com/dmes/login",
				"urlGetTile:https://api.wexplore.olivetti.com/dmes/getDataTileFromAce",
				"username:perugia",
				"password:HZe[A$fT7u22cy_v",
				"ace:10|054|039",
				"dataInizio:171022",
				"orainizio:0000",
				"dataFine:171023",
				"orafine:0000",
				"granularita:15",
				"adjusment:true",
				"colori:[\"P\",\"Ni\",\"Ns\",\"Tb\",\"Tc\",\"Gm\",\"Gf\",\"F1\",\"F2\",\"F3\",\"F4\",\"F5\",\"F6\",\"Vi\",\"Ve\",\"Vp\",\"Vr\"]"};
		
		
		this.createDir(ClientRest.DIR);
		ClientRest.file = this.createFile(ClientRest.PATHFILECONFIG,"config");
		if(ClientRest.file.length()==0) {
			this.fileWriteMultilinePrintWriter(ClientRest.file, config);
			System.out.println("Congratulazioni!!!\n"+
								"La configurlazione del Venice\n"+
								"ed andata a buon fine nella seguente direttori\n"+
								"Direttori: " + ClientRest.PATHFILECONFIG );
			System.exit(0);
		}
	}

}
