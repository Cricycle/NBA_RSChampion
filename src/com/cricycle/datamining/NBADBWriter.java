package com.cricycle.datamining;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

public class NBADBWriter {
	
	private static String dbConnString = "jdbc:postgresql://localhost:5432/NBA";
	private static String dbPropertiesFile = "database.properties";
	private static String nbaGameDataTable = "nba_game_data";
	private static String[] nbaGameDataCols = {"home_team", "away_team", "home_score", "away_score", "game_date"};
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
			String format = "(?, ?, ?, ?, ?)";
			sqlParts.append(String.format("%s%c\n", format, (i == data.size()-1 ? ';' : ',')));
		}
		PreparedStatement st = conn.prepareStatement(sqlParts.toString(), nbaGameDataCols);
		for (int i = 0; i < data.size(); ++i) {
			NBAGameData gameData = data.get(i);
			java.util.Calendar cal = java.util.Calendar.getInstance(); // machinery to convert java.util.Date to java.sql.Date
			java.util.Date gameDate = gameData.getGameDate();
			cal.setTime(gameDate);
			st.setString(i*5 + 1, gameData.getHomeTeamName());
			st.setString(i*5 + 2, gameData.getAwayTeamName());
			st.setInt(i*5 + 3, gameData.getHomeTeamScore());
			st.setInt(i*5 + 4, gameData.getAwayTeamScore());
			st.setDate(i*5 + 5, new java.sql.Date(cal.getTime().getTime()));
		}
		
		st.execute();
		closeConnection(conn);
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
