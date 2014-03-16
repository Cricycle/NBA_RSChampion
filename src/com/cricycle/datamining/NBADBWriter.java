package com.cricycle.datamining;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class NBADBWriter {
	
	private static String dbConnString = "jdbc:postgresql://localhost:5432/NBA";
	private static String dbPropertiesFile = "database.properties";
	private static String nbaChampionTable = "nba_champions";
	private static String[] nbaChampionCols = {"team_name", "year"};
	private static String nbaGameDataTable = "nba_game_data";
	private static String[] nbaGameDataCols = {"winning_team", "losing_team", "winning_score", "losing_score", "game_date", "game_loc"};
	private Properties props;
	
	public NBADBWriter() {
		props = new Properties();
		try {
			InputStream in = new FileInputStream(dbPropertiesFile);
			props.loadFromXML(in);
		} catch (IOException e) {
			throw new RuntimeException(e.toString());
		}
	}
	
	public void writeGameData(ArrayList<NBAGameData> data) throws SQLException {
		if (data.isEmpty())
			return;
		Connection conn = getConnection();
		StringBuilder sqlParts = new StringBuilder("INSERT INTO " + nbaGameDataTable + " VALUES ");
		for (int i = 0; i < data.size(); ++i) {
			String format = "(?, ?, ?, ?, ?, ?)";
			sqlParts.append(String.format("%s%c\n", format, (i == data.size()-1 ? ';' : ',')));
		}
		PreparedStatement st = conn.prepareStatement(sqlParts.toString(), nbaGameDataCols);
		for (int i = 0; i < data.size(); ++i) {
			NBAGameData gameData = data.get(i);
			java.util.Calendar cal = java.util.Calendar.getInstance(); // machinery to convert java.util.Date to java.sql.Date
			java.util.Date gameDate = gameData.getGameDate();
			cal.setTime(gameDate);
			st.setString(i*6 + 1, sub63(gameData.getWinningTeamName()));
			st.setString(i*6 + 2, sub63(gameData.getLosingTeamName()));
			st.setInt(i*6 + 3, gameData.getWinningTeamScore());
			st.setInt(i*6 + 4, gameData.getLosingTeamScore());
			st.setDate(i*6 + 5, new java.sql.Date(cal.getTime().getTime()));
			st.setString(i*6 + 6, sub63(gameData.getGameLocation()));
		}
		
		st.execute();
		closeConnection(conn);
	}
	
	public void writeChampionData(ArrayList<NBAChampion> data) throws SQLException {
		if (data.isEmpty())
			return;
		Connection conn = getConnection();
		StringBuilder sqlParts = new StringBuilder("INSERT INTO " + nbaChampionTable + " VALUES ");
		for (int i = 0; i < data.size(); ++i) {
			String format = "(?, ?)";
			sqlParts.append(String.format("%s%c\n", format, (i == data.size()-1 ? ';' : ',')));
		}
		PreparedStatement st = conn.prepareStatement(sqlParts.toString(), nbaChampionCols);
		for (int i = 0; i < data.size(); ++i) {
			NBAChampion champ = data.get(i);
			st.setString(i*2 + 1, champ.getTeamName());
			st.setInt(i*2 + 2, champ.getYear());
		}
		
		st.execute();
		closeConnection(conn);
	}
	
	private String sub63(String input) {
		if (input.length() <= 63)
			return input;
		return input.substring(0, 63);
	}
	
	private void closeConnection(Connection conn) throws SQLException {
		conn.close();
	}
	
	private Connection getConnection() throws SQLException {
		loadDriverClass();
		int failCount = 0;
		boolean successful = false;
		Connection conn = null;
		SQLException excep = null;
		while (failCount < 5 && !successful) {
			try {
				conn = DriverManager.getConnection(dbConnString, props);
				successful = true;
			} catch (SQLException e) {
				excep = e;
				++failCount;
				try {
					// sleep for short time to give time to recover from transient error
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		if (!successful) {
			throw excep;
		}
		return conn;
	}
	
	private void loadDriverClass() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e.toString());
		}
	}
	
}
