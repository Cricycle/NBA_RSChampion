package com.cricycle.datamining;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class NBAMain {
	public static void main(String[] args) {
		if (args.length > 0 && args[0].equals("load")) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.set(2013, Calendar.OCTOBER, 29);
			loadFrom(cal);
			return;
		}
		
	}
	
	public static void loadFrom(GregorianCalendar start) {
		GregorianCalendar today = new GregorianCalendar();
		NBADBWriter writer = new NBADBWriter();
		
		while (start.before(today)) {
			Date date = start.getTime();
			
			ArrayList<NBAGameData> data = NBAGameDataMiner.getNBAGameData(date);
			
			try {
				writer.writeGameData(data);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			start.add(Calendar.DAY_OF_MONTH, 1);
			
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
