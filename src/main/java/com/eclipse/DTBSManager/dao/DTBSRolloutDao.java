package com.eclipse.DTBSManager.dao;

import java.sql.Connection;

import com.eclipse.DTBSManager.bean.RolloutResult;
/**
 * 
 * @author Leon Wang	
 * @email seasparta618@gmail.com || dev.leon618@gmail.com
 */
public interface DTBSRolloutDao {

	public boolean populateRoster(Connection conn) throws Exception;

	public RolloutResult populateRosterByRegion(Connection conn, String rolloutRegionCode, java.sql.Date rosterStartDate,int days,
			String rolloutStatus, String currUser, boolean doUpdates) throws Exception;
	
	public long createRosterRollout(Connection dbConn, String rolloutRegionCode, java.sql.Date rolloutStartDate,
			String rolloutStatus, String currUser)throws Exception;

}
