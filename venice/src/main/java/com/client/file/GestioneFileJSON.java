package com.client.file;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GestioneFileJSON {
	
	public class Login {
		@JsonProperty("username")
		public String username;
		@JsonProperty("password")
		public String password;
	}
	
	public class DataTile {
		@JsonProperty("ace")
		public String ace;
		@JsonProperty("data")
		public String data;
		@JsonProperty("ora")
		public String ora;
		@JsonProperty("granularita")
		public String granularita;
		@JsonProperty("adjustment")
		public String adjustment;
		@JsonProperty("colors")
		public String colors;
	}

	public static String ConvertJSONLogin(String username, String password) {
		String json = "";
		json = "{\n" + "\"username\":\"" + username + "\",\n" + "\"password\":\"" + password + "\"\n" + "}";
		return json;
	}
	
	public static String ConvertJSONDataTileFromAce(String ace, String data, String ora, 
													String granularita, String adjustment, 
													String colors) {
		String json = "";
		
		// proceso a colors

		json = "{\n" + 
				"\"ace\": \"" + ace + "\",\n" + 
				"\"data\": \"" + data + "\",\n" + 
				"\"ora\":   \"" + ora + "\" ,\n" + 
				"\"granularita\": \"" + granularita + "\",\n" + 
				"\"adjustment\": " + adjustment + ",\n" +
				"\"colors\": " + colors + "\n" + 
//				"\"colors\": [\"P\",\"Ni\",\"Ns\",\"Tb\",\"Tc\",\"Gm\",\"Gf\",\"F1\",\"F2\",\"F3\",\"F4\",\"F5\",\"F6\",\"Vi\",\"Ve\",\"Vp\",\"Vr\"]\n" + 
				"}";
		return json;
	}
}
