package com.client.rest;

public class Funzioni {
	
	public static String removeCh (String s , int index) {
		if ((index > s.length()-1) || (index < 0)) return null;
		String c = s.substring(0,index) + s.substring(index+1 , s.length());
		return c;
		}

}
