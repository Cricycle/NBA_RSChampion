package com.cricycle.datamining;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class NBAMiner {
	
	public static String getHTMLOfWebsiteWithParam(String websiteURL, Map<String, String> dataParams) {
		Connection conn = Jsoup.connect(websiteURL)
		if (dataParams != null)
			conn = conn.data(dataParams);
		
		URL websiteURL = null;
		try {
			websiteURL = new URL(website);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e.toString());
		}
		
		StringBuilder html = new StringBuilder();
		try {
		    URLConnection conn = websiteURL.openConnection();
		    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String inputLine;
		    while ((inputLine = in.readLine()) != null) {
		        System.out.println(inputLine);
		    }
		    in.close();
		} catch (IOException e) {
			throw new RuntimeException(e.toString());
		}
		
		return html.toString();
	}
}
