package com.eclipse.DTBSManager.bean;
/**
 * 
 * @author Leon Wang	
 * @email seasparta618@gmail.com || dev.leon618@gmail.com
 */
public class RosterVisual {

	private int id;
	private int rosterSlotId;
	private String Slot;
	private String region;
	private int locationId;
	private int testRosterId;
	private int slotMapId;
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

	public int getSlotMapId() {
		return slotMapId;
	}

	public void setSlotMapId(int slotMapId) {
		this.slotMapId = slotMapId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRosterSlotId() {
		return rosterSlotId;
	}

	public void setRosterSlotId(int rosterSlotId) {
		this.rosterSlotId = rosterSlotId;
	}

	public String getSlot() {
		return Slot;
	}

	public void setSlot(String slot) {
		Slot = slot;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	public int getTestRosterId() {
		return testRosterId;
	}

	public void setTestRosterId(int testRosterId) {
		this.testRosterId = testRosterId;
	}

	@Override
	public String toString() {
		return "RosterVisual [id=" + id + ", rosterSlotId=" + rosterSlotId + ", Slot=" + Slot + ", region=" + region
				+ ", locationId=" + locationId + ", testRosterId=" + testRosterId + ", slotMapId=" + slotMapId
				+ ", total=" + total + ", used=" + used + "]";
	}

}
