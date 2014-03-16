package com.cricycle.datamining;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class NBAMain {
	public static void main(String[] args) {
		if (args.length == 0)
			return;
		if (args[0].equals("loadRegular")) {
			int year = Integer.parseInt(args[1]);
			loadFromSeason(year);
		} else if (args[0].equals("loadChamps")) {
			loadChampionData();
		}
	}
	
	public static void loadFromSeason(int year) {
		GregorianCalendar today = new GregorianCalendar();
		NBADBWriter writer = new NBADBWriter();
		
		while (year < today.get(Calendar.YEAR)) {
			System.out.println("mining: " + year);
			ArrayList<NBAGameData> data = NBADataMiner.getNBAGameData(year);
			
			try {
				if (data != null)
				writer.writeGameData(data);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			++year;
			
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void loadChampionData() {
		ArrayList<NBAChampion> data = NBADataMiner.getNBAChampions();
		NBADBWriter writer = new NBADBWriter();
		try {
			writer.writeChampionData(data);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
