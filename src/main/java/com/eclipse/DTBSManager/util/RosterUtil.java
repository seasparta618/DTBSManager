package com.eclipse.DTBSManager.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import com.eclipse.DTBSManager.bean.RosterVisual;
import com.eclipse.DTBSManager.bean.RosterVisualJson;
/**
 * 
 * @author Leon Wang	
 * @email seasparta618@gmail.com || dev.leon618@gmail.com
 */
public class RosterUtil {

	private static int totalRecord = 0;
	private static int processedRecord = 0;

	public static Connection getConnection() {
		Connection con = null;
		try {
			String url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=192.168.56.101)(PORT=1521)))(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=xe)))";
			// String url =
			// "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=tmrd-db)(PORT=1535)))(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=TMRD)))";
			Class.forName("oracle.jdbc.OracleDriver");

			con = DriverManager.getConnection(url, "dtbs", "dtbs");
			// con = DriverManager.getConnection(url, "dtbs", "vinegarhorse5");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	public static String WARN_ON_SLOT_TIME(String loc, String thehour, String theminute) {
		String result = "OK";
		String time = thehour + theminute;

		switch (loc) {
		case "Hobart":
		case "Launceston":
		case "Burnie":
		case "Devonport":
		case "Ulverstone":
		case "Sheffield":
		case "Queenstown":
		case "Longford":
		case "Wynyard": {
			report("Hobart");
			if ("1555".equals(time) || "1615".equals(time)) {
				result = "LATE";
			} else if ("0815".equals(time) || "0910".equals(time)) {
				result = "EARLY";
			}
			break;
		}
		case "George Town": {
			if ("1555".equals(time)) {
				result = "LATE";
			} else if ("0815".equals(time) | "0910".equals(time)) {
				result = "EARLY";
			}
			break;
		}
		case "Beaconsfield": {
			if ("1555".equals(time)) {
				result = "LATE";
			} else if ("0815".equals(time)) {
				result = "EARLY";
			}
			break;
		}
		case "Oatlands":
		case "Campbell Town": {
			if ("0910".equals(time) || "1020".equals(time)) {
				result = "EARLY";
			}
			break;
		}
		case "Scottsdale": {
			if ("0910".equals(time)) {
				result = "EARLY";
			}
			break;
		}

		}
		return result;
	}

	public static String getPercentage(int totalSlots, int availableSlots) {
		double result = 0;
		try {
			result = availableSlots * 100 / totalSlots;
		} catch (java.lang.ArithmeticException e) {

		}
		java.text.NumberFormat fm = java.text.NumberFormat.getNumberInstance();
		fm.setMaximumFractionDigits(2);
		fm.setMinimumFractionDigits(0);
		return fm.format(result) + "%";
	}

	public static boolean processSaveTsvFile(List<RosterVisual> rosters, int locationId) {
		String str1 = "08:00";
		String str2 = "08:15";
		String str3 = "09:10";
		String str4 = "10:05";
		String str5 = "10:20";
		String str6 = "11:15";
		String str7 = "12:10";
		String str8 = "12:55";
		String str9 = "13:50";
		String str10 = "14:45";
		String str11 = "15:00";
		String str12 = "15:55";
		String str13 = "16:50";
		String[] slots = new String[13];
		slots[0] = str1;
		slots[1] = str2;
		slots[2] = str3;
		slots[3] = str4;
		slots[4] = str5;
		slots[5] = str6;
		slots[6] = str7;
		slots[7] = str8;
		slots[8] = str9;
		slots[9] = str10;
		slots[10] = str11;
		slots[11] = str12;
		slots[12] = str13;
		boolean result = false;
		int days = 27;
		RosterVisual roster = null;
		try {

			String fileStr = "C:\\Users\\jchen\\Desktop\\DTBSManager\\LocationId-" + locationId + ".tsv";
			File file = new File(fileStr);
			file.createNewFile();

			FileWriter fos = new FileWriter(fileStr);
			// PrintWriter dos = new PrintWriter(System.out);
			PrintWriter dos = new PrintWriter(fos, true);
			dos.println("day\thour\tvalue\t");
			// loop through all your data and print it to the file
			for (int i = 0; i < days; i++) {
				for (int j = 0; j < 12; j++) {
					try {
						roster = rosters.get(i * 12 + j);
					} catch (Exception e) {
					}
					dos.print((i + 1) + "\t");
					dos.print((j + 1) + "\t");
					if (roster == null) {
						dos.print("0\t");
					} else {
						if (roster.getTestRosterId() == 0) {
							dos.print("1\t");
						} else {
							dos.print("2\t");
						}
					}
					dos.println();
					fos.flush();
				}
			}
			dos.flush();
			dos.close();
			fos.close();
		} catch (IOException e) {
			System.out.println("Error Printing Tab Delimited File");
		}
		return result;
	}

	public static List<RosterVisualJson> convertRosterVisualFormat4Json(List<RosterVisual> rosters,
			java.util.Date date) {
		List<RosterVisualJson> jsons = new ArrayList<RosterVisualJson>();
		GregorianCalendar gcalStartDate = new GregorianCalendar();
		gcalStartDate.setTime(date);
		gcalStartDate.add(Calendar.DATE, -25);
		String str1 = "08:00";
		String str2 = "08:15";
		String str3 = "09:10";
		String str4 = "10:05";
		String str5 = "10:20";
		String str6 = "11:15";
		String str7 = "12:10";
		String str8 = "12:55";
		String str9 = "13:50";
		String str10 = "14:45";
		String str11 = "15:00";
		String str12 = "15:55";
		String str13 = "16:50";
		String[] slots = new String[13];
		slots[0] = str1;
		slots[1] = str2;
		slots[2] = str3;
		slots[3] = str4;
		slots[4] = str5;
		slots[5] = str6;
		slots[6] = str7;
		slots[7] = str8;
		slots[8] = str9;
		slots[9] = str10;
		slots[10] = str11;
		slots[11] = str12;
		slots[12] = str13;
		int days = 27;
		RosterVisual roster = null;
		RosterVisualJson json = null;
		// Random random = new Random();
		int weekDays = 0;
		for (int i = 0; i < days; i++) {
			// From Monday to Friday.
			if (gcalStartDate.get(Calendar.DAY_OF_WEEK) - 1 > 0 && gcalStartDate.get(Calendar.DAY_OF_WEEK) - 1 < 6) {
				for (int j = 0; j < 13; j++) {
					roster = rosters.get(weekDays * 13 + j);
					json = constructRosterVisualJson((i + 1) + "", (j + 1) + "", roster.getTotal(), roster.getUsed());
					jsons.add(json);
				}
				weekDays++;
			} else {
				for (int j = 0; j < 13; j++) {
					json = constructRosterVisualJson((i + 1) + "", (j + 1) + "", 0, 0);
					jsons.add(json);
				}
			}
			gcalStartDate.add(Calendar.DATE, 1);
		}
		return jsons;
	}

	private static RosterVisualJson constructRosterVisualJson(String day, String hour, int total, int used) {
		RosterVisualJson json = new RosterVisualJson();
		json.setTotal(total);
		json.setUsed(used);
		json.setDay(day);
		json.setHour(hour);
		Random random = new Random();
		json.setValue(random.nextInt(10));
		/*
		if(total>0 && used<total){
			json.setValue((int)Math.floor((used/(float)total)*9));
		}else if(total>0 && used == total){
			json.setValue(9);
		}else{
			json.setValue(0);
		}
		*/
		
		return json;
	}

	public static String fromUtilDate2FormatString(java.util.Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static char PASSES_L2_PRE_9AM_CHECK(char p_is_L2_test_type, String loc, String slottime) {
		char result = 'Y';
		SimpleDateFormat parser = new SimpleDateFormat("HHmm");
		try {
			Date checkTime = parser.parse("0900");
			Date checkSlotTime = parser.parse(slottime);
			String[] placeSet = { "Hobart", "Launceston", "St Helens", "Burnie", "Devonport", "Ulverstone",
					"Queenstown" };
			if ('Y' == p_is_L2_test_type && checkSlotTime.before(checkTime)) {
				for (String place : placeSet) {
					if (place.equals(loc)) {
						result = 'N';
						break;
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static final void closeObject(Statement o) {
		try {
			o.close();
			o = null;
		} catch (Exception clEx) {
			clEx = null;
		}
	}

	public static final void closeObject(Connection o) {
		try {
			o.close();
			o = null;
		} catch (Exception clEx) {
			clEx = null;
		}
	}

	public static final void closeObject(ResultSet o) {
		try {
			o.close();
			o = null;
		} catch (Exception clEx) {
			clEx = null;
		}
	}

	public static int toTheDay4Oracle(Date startDate) {
		int the_day = 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		the_day = cal.get(Calendar.DAY_OF_WEEK);
		// System.out.println(the_day);
		return the_day;
	}

	public static java.sql.Date fromUtilDate2SqlDate(java.util.Date date) {
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		return sqlDate;
	}

	public static java.util.Date fromSqlDate2UtilDate(java.sql.Date date) {
		java.util.Date utlDate = new java.util.Date(date.getTime());
		return utlDate;
	}

	public static java.util.Date fromSqlString2UtilDate(String stringDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		// java.util.Date utlDate = new
		// java.util.Date(sdf.parse(stringDate).getTime());
		return sdf.parse(stringDate);
	}

	public static java.sql.Date fromSqlString2SqlDate(String stringDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		java.sql.Date sqlDate = new java.sql.Date(sdf.parse(stringDate).getTime());
		return sqlDate;
	}

	public static String fromSqlDate2FormatString(java.sql.Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(date);
	}

	public static String fromSqlDate2FormatString(java.sql.Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static String fromSqlDate2FormatString(java.util.Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static String fromSqlDate2AddDays(java.sql.Date date, int days, int hour, int minute) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		return fromSqlDate2FormatString(cal.getTime(), "dd/MM/YYYY HH:mm");
	}

	public static double getProcessingPercentage() {
		double result = 0;
		try {
			result = RosterUtil.processedRecord * 100 / RosterUtil.totalRecord;
			RosterUtil.report("Total:" + RosterUtil.totalRecord + "; processed: " + RosterUtil.processedRecord);
		} catch (java.lang.ArithmeticException e) {
			// e.printStackTrace();
			result = 0;
		}
		java.text.NumberFormat fm = java.text.NumberFormat.getNumberInstance();
		fm.setMaximumFractionDigits(2);
		fm.setMinimumFractionDigits(0);
		return Double.parseDouble(fm.format(result));
	}

	public static void report(Object obj) {
		System.out.println(obj);
	}
}
