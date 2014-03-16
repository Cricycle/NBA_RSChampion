package com.cricycle.datamining;

public class NBAChampion {
	
	private String teamName;
	private int year;
	
	public NBAChampion(String teamName, int year) {
		this.teamName = teamName;
		this.year = year;
	}
	
	public int getYear() {
		return year;
	}
	
	public String getTeamName() {
		return teamName;
	}
}
