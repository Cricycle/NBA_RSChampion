package com.cricycle.datamining;

import java.util.ArrayList;

public class NBADBWriter {
	
	private String dbConnString;
	
	public NBADBWriter() { 
		this.dbConnString = "jdbc:postgresql://localhost:5432/NBA";
	}
	
	public void writeGameData(ArrayList<NBAGameData> data) {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e.toString());
		}
		
		jdbc:postgresql://localhost:5432/NBA
		
	}
	
}
