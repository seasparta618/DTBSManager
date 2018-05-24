package com.eclipse.DTBSManager.dao;

import java.sql.Connection;
import java.util.List;

import com.eclipse.DTBSManager.bean.RosterVisual;
/**
 * 
 * @author Leon Wang	
 * @email seasparta618@gmail.com || dev.leon618@gmail.com
 */
public interface DTBSRolloutAsisDao {
	public java.sql.Date getNextRolloutStartDateFromRegion(Connection conn, String rolloutRegionCode) throws Exception;
	
	public java.sql.Date getLastRolloutStartDateFromRegion(Connection conn, String rolloutRegionCode) throws Exception;
	
	public java.sql.Date getStartRolloutStartDateFromRegion(Connection conn, String rolloutRegionCode) throws Exception;
	
	public int clearMonthBooking(Connection conn,String rolloutRegionCode)throws Exception;
	
	public int checkAvailableSlotsNum(Connection conn,String rolloutRegionCode)throws Exception;
	
	public int checkTotalSlotsNum(Connection conn,String rolloutRegionCode)throws Exception;
	
	public String checkAvailableSlotsPercent(Connection conn,String rolloutRegionCode)throws Exception;
	
	public List<RosterVisual> getRosterVisuals(Connection conn,String rolloutRegionCode)throws Exception;
	
	public List<RosterVisual> getRosterVisuals(Connection conn,String rolloutRegionCode,String locationId,int rosterId)throws Exception;
	
	public String[] getLocations(Connection conn,String rolloutRegionCode)throws Exception;
	
	public int getLatestRolloutIdByRegion(Connection conn,String rolloutRegionCode)throws Exception;

}
