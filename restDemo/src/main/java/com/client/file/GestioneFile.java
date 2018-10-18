package com.client.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GestioneFile {
	
	/*====================================================*/
	public File createFile(String path) throws IOException {
		File file = new File(path);
		if(!file.exists()) {
			file.createNewFile();
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
		
		FileWriter fw = new FileWriter(file);
		fw.write(text);
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

	
	/*LeggereFile================================================*/
	@SuppressWarnings("resource")
	public String[] FileReader(File file) throws IOException {
		
		String[] array = new String[12];
		
//		FileReader fr = null;
//		
//		fr = new FileReader(file);
//		
//		char[] text = new char[1024];
//		
//		int size = fr.read(text);
//		
//		for(int i = 0; i < size; i++) {
//			array[i]=text[i];
//		}
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String linea; int i = 0;
		
		while ((linea = reader.readLine()) != null){
			array[i]=linea.substring(linea.indexOf(":")+1);
			i++;
		}
		
		return array;
	}
	
	public String[] cloneArray(String[] array) {
        String[] newArray = new String[array.length];
        for (int i = 0; i < array.length; i++)
            newArray[i] = array[i];
        return newArray;
    }

}
