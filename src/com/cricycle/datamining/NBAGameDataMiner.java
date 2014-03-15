package com.cricycle.datamining;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NBAGameDataMiner {
	
	public static final String espnURL= "http://scores.espn.go.com/nba/scoreboard";
	
	public static ArrayList<NBAGameData> getNBAGameData(Date gameDate) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String espnDate = df.format(gameDate);
		
		Connection conn = Jsoup.connect(espnURL)
			.data("date", espnDate)
			.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");

		System.out.println(espnDate);
		//if (espnDate != null)
		//	return null;
		ArrayList<NBAGameData> list = new ArrayList<>();
		try {
			Document doc = conn.get();

			Elements gameResults = doc.select("div.mod-content");
			Iterator<Element> it = gameResults.iterator();
			while (it.hasNext()) {
				Element result = it.next();
				Elements awayDivGroup = result.getElementsByAttributeValue("class", "team away");
				Elements homeDivGroup = result.getElementsByAttributeValue("class", "team home");
				
				if (awayDivGroup.isEmpty()) {
					continue;
				}
				Element awayTeam = awayDivGroup.get(0);
				Element homeTeam = homeDivGroup.get(0);
				
				Element awayTeamP = awayTeam.getElementsByAttributeValue("class", "team-name").get(0);
				Element homeTeamP = homeTeam.getElementsByAttributeValue("class", "team-name").get(0);
				String awayTeamName = awayTeamP.text();
				String homeTeamName = homeTeamP.text();
				int awayTeamScore = -1;
				int homeTeamScore = -1;
				
				try {
					awayTeamScore = Integer.parseInt(awayTeam.getElementsByAttributeValue("class", "finalScore").get(0).text());
					homeTeamScore = Integer.parseInt(homeTeam.getElementsByAttributeValue("class", "finalScore").get(0).text());
				} catch (Exception e) {
					continue;
				}
				
				NBAGameData game = new NBAGameData(homeTeamName, awayTeamName, homeTeamScore, awayTeamScore, gameDate);
				list.add(game);
			}
			
		} catch (IOException e) {
			System.err.println(e.toString());
		}
		
		return list;
	}
}
