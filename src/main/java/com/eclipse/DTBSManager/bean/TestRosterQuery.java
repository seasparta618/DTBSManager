package com.eclipse.DTBSManager.bean;
/**
 * 
 * @author Leon Wang	
 * @email seasparta618@gmail.com || dev.leon618@gmail.com
 */
public class TestRosterQuery {
	
	private int rosterSlotId; 
	private int intHour;
	private int intMinute;
	private String stringTesterStatusCode;
	private long longResourceId;
	private long longLocationId;
	private int intDay;
	private int intWeek;
	

	public TestRosterQuery() {
		super();
	}

	public TestRosterQuery(int intHour, int intMinute, String stringTesterStatusCode, long longResourceId,
			long longLocationId, int intDay, int intWeek) {
		super();
		this.intHour = intHour;
		this.intMinute = intMinute;
		this.stringTesterStatusCode = stringTesterStatusCode;
		this.longResourceId = longResourceId;
		this.longLocationId = longLocationId;
		this.intDay = intDay;
		this.intWeek = intWeek;
	}

	
	public int getRosterSlotId() {
		return rosterSlotId;
	}

	public void setRosterSlotId(int rosterSlotId) {
		this.rosterSlotId = rosterSlotId;
	}

	public int getIntHour() {
		return intHour;
	}

	public void setIntHour(int intHour) {
		this.intHour = intHour;
	}

	public int getIntMinute() {
		return intMinute;
	}

	public void setIntMinute(int intMinute) {
		this.intMinute = intMinute;
	}

	public String getStringTesterStatusCode() {
		return stringTesterStatusCode;
	}

	public void setStringTesterStatusCode(String stringTesterStatusCode) {
		this.stringTesterStatusCode = stringTesterStatusCode;
	}

	public long getLongResourceId() {
		return longResourceId;
	}

	public void setLongResourceId(long longResourceId) {
		this.longResourceId = longResourceId;
	}

	public long getLongLocationId() {
		return longLocationId;
	}

	public void setLongLocationId(long longLocationId) {
		this.longLocationId = longLocationId;
	}

	public int getIntDay() {
		return intDay;
	}

	public void setIntDay(int intDay) {
		this.intDay = intDay;
	}

	public int getIntWeek() {
		return intWeek;
	}

	public void setIntWeek(int intWeek) {
		this.intWeek = intWeek;
	}

}
