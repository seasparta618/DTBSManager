package com.eclipse.DTBSManager.bean;
/**
 * 
 * @author Leon Wang	
 * @email seasparta618@gmail.com || dev.leon618@gmail.com
 */
public class RosterVisualJson {

	private String day;
	private String hour;
	private int value;
	private int total;
	private int used;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getUsed() {
		return used;
	}

	public void setUsed(int used) {
		this.used = used;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
