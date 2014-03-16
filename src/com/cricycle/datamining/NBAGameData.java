package com.cricycle.datamining;

import java.util.Date;

public class NBAGameData {
	private String winningTeamName;
	private String losingTeamName;
	private int winningTeamScore;
	private int losingTeamScore;
	private String gameLocation;
	private Date gameDate;
	
	public NBAGameData(String winningTeamName,
			String losingTeamName,
			int winningTeamScore,
			int losingTeamScore,
			String gameLocation,
			Date gameDate) {
		this.winningTeamName = winningTeamName;
		this.losingTeamName = losingTeamName;
		this.winningTeamScore = winningTeamScore;
		this.losingTeamScore = losingTeamScore;
		this.gameLocation = gameLocation;
		this.gameDate = (Date)gameDate.clone();
	}
	
	public String getWinningTeamName() {
		return winningTeamName;
	}

	public String getLosingTeamName() {
		return losingTeamName;
	}

	public int getWinningTeamScore() {
		return winningTeamScore;
	}

	public int getLosingTeamScore() {
		return losingTeamScore;
	}

	public String getGameLocation() {
		return gameLocation;
	}

	public Date getGameDate() {
		return gameDate;
	}

	public String toString() {
		return String.format("H:%s,A:%s  %d:%d", winningTeamName, losingTeamName, winningTeamScore, losingTeamScore);
	}
}
