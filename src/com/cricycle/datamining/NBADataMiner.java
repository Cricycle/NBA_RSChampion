package com.cricycle.datamining;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NBADataMiner {
	
	private static final String landOfBasketballURL = "http://www.landofbasketball.com/results/%d_%d_scores_full.htm";
	private static final String landOfBasketballChampionsURL = "http://www.landofbasketball.com/championships/year_by_year.htm";
	
	public static ArrayList<NBAGameData> getNBAGameData(int startYear) {
		String finalURL = String.format(landOfBasketballURL, startYear, startYear+1);
		
		Connection conn = Jsoup.connect(finalURL)
				.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
		
		ArrayList<NBAGameData> list = new ArrayList<>();
		try {
			Document doc = conn.get();
			Elements tables = doc.select("table");
			Element dataTable = tables.get(10);
			Elements gameDataAsRow = dataTable.getElementsByAttributeValue("valign", "top");
			Iterator<Element> it = gameDataAsRow.iterator();
			while (it.hasNext()) {
				Element gameDataRow = it.next();
				Elements columns = gameDataRow.select("td");
				Date gameDate = new SimpleDateFormat("MMM dd, yyyy").parse(columns.get(0).text().trim());
				String winning_team = columns.get(1).text().trim();
				String temp = columns.get(2).text().trim();
				int winning_score = -1;
				try {
					winning_score = Integer.parseInt(temp.substring(0, temp.length()-1));
				} catch (NumberFormatException e) {
					continue;
				}
				String losing_team = columns.get(3).text().trim();
				int losing_score = Integer.parseInt(columns.get(4).text().trim());
				String location = columns.get(6).text().trim().substring(3);
				list.add(new NBAGameData(winning_team, losing_team, winning_score, losing_score, location, gameDate));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static ArrayList<NBAChampion> getNBAChampions() {
		Connection conn = Jsoup.connect(landOfBasketballChampionsURL);
		ArrayList<NBAChampion> list = new ArrayList<>();
		
		try {
			Document doc = conn.get();
			Elements tables = doc.select("table");
			Element dataTable = tables.get(6);
			Elements champRows = dataTable.select("tr");
			for (int i = 1; i < champRows.size(); ++i) {
				Element champData = champRows.get(i);
				Elements rowData = champData.select("td");
				String textYear = rowData.get(0).text();
				int year = Integer.parseInt(textYear.substring(0, 4)) + 1;
				String winningTeam = rowData.get(1).text().trim();
				list.add(new NBAChampion(winningTeam, year));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return list;
	}
}
