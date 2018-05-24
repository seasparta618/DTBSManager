package com.eclipse.DTBSManager.bean;
/**
 * 
 * @author Leon Wang	
 * @email seasparta618@gmail.com || dev.leon618@gmail.com
 */
public class RolloutQuery {

	private int days;
	private String region;
	private String startDate;
	private String rolloutStatus;
	private String user;

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getRolloutStatus() {
		return rolloutStatus;
	}

	public void setRolloutStatus(String rolloutStatus) {
		this.rolloutStatus = rolloutStatus;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
