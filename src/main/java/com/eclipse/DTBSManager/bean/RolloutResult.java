package com.eclipse.DTBSManager.bean;

/**
 * 
 * @author Leon Wang	
 * @email seasparta618@gmail.com || dev.leon618@gmail.com
 */

public class RolloutResult {

	private  int intDay;
	private String startDate;
	private int rosterSlotsCount;
	private int testRosterCount;
	private int roterRolloutCount;
	private String result;

	public RolloutResult() {
		startDate = "01/01/2020";
		result = "test";
	}

	public int getIntDay() {
		return intDay;
	}

	public void setIntDay(int intDay) {
		this.intDay = intDay;
	}


	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public int getRosterSlotsCount() {
		return rosterSlotsCount;
	}

	public void setRosterSlotsCount(int rosterSlotsCount) {
		this.rosterSlotsCount = rosterSlotsCount;
	}

	public int getTestRosterCount() {
		return testRosterCount;
	}

	public void setTestRosterCount(int testRosterCount) {
		this.testRosterCount = testRosterCount;
	}

	public int getRoterRolloutCount() {
		return roterRolloutCount;
	}

	public void setRoterRolloutCount(int roterRolloutCount) {
		this.roterRolloutCount = roterRolloutCount;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
