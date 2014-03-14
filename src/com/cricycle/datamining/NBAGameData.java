package com.cricycle.datamining;

import java.util.Date;

public class NBAGameData {
	private String homeTeamName;
	private String awayTeamName;
	private int homeTeamScore;
	private int awayTeamScore;
	private Date gameDate;
	
	public NBAGameData() {}
	
	public NBAGameData(String homeTeamName, String awayTeamName, int homeTeamScore, int awayTeamScore, Date gameDate) {
		this.homeTeamName = homeTeamName;
		this.awayTeamName = awayTeamName;
		this.homeTeamScore = homeTeamScore;
		this.awayTeamScore = awayTeamScore;
		this.gameDate = (Date)gameDate.clone();
	}
	
	public void setHomeTeamName(String homeTeamName) {
		this.homeTeamName = homeTeamName;
	}
	
	public void setAwayTeamName(String awayTeamName) {
		this.awayTeamName = awayTeamName;
	}
	
	public void setGameDate(Date gameDate) {
		this.gameDate = (Date)gameDate.clone();
	}
	
	public void setHomeTeamScore(int homeTeamScore) {
		this.homeTeamScore = homeTeamScore;
	}
	
	public void setAwayTeamScore(int awayTeamScore) {
		this.awayTeamScore = awayTeamScore;
	}
	
	public String getHomeTeamName() {
		return homeTeamName;
	}
	
	public String getAwayTeamName() {
		return awayTeamName;
	}
	
	public int getHomeTeamScore() {
		return homeTeamScore;
	}
	
	public int getAwayTeamScore() {
		return awayTeamScore;
	}
	
	public Date getGameDate() {
		return (Date)gameDate.clone();
	}
	
	public String toString() {
		return String.format("H:%s,A:%s  %d:%d", homeTeamName, awayTeamName, homeTeamScore, awayTeamScore);
	}
}
