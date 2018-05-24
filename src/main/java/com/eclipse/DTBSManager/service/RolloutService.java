package com.eclipse.DTBSManager.service;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import com.eclipse.DTBSManager.bean.RolloutResult;
import com.eclipse.DTBSManager.bean.RosterVisual;
import com.eclipse.DTBSManager.dao.DTBSRolloutAsisDao;
import com.eclipse.DTBSManager.dao.DTBSRolloutDao;
import com.eclipse.DTBSManager.dao.OracleDTBSRolloutAsisDaoImpl;
import com.eclipse.DTBSManager.dao.OracleDTBSRolloutDaoImpl;
import com.eclipse.DTBSManager.util.RosterUtil;
/**
 * 
 * @author Leon Wang	
 * @email seasparta618@gmail.com || dev.leon618@gmail.com
 */
public class RolloutService {

	private DTBSRolloutDao dao = new OracleDTBSRolloutDaoImpl();
	private DTBSRolloutAsisDao AsisDao = new OracleDTBSRolloutAsisDaoImpl();
	private Connection conn = RosterUtil.getConnection();

	public void createRollout() {

	}

	public void createRollout(int day) {

	}

	public RolloutResult createRollout(String region) throws Exception {
		RolloutResult result = null;
		java.util.Date rolloutDate = this.getNextRolloutDate(region);
		if (conn == null) {
			conn = RosterUtil.getConnection();
		} else if (conn.isClosed()) {
			conn = RosterUtil.getConnection();
		}
		result = dao.populateRosterByRegion(conn, region, RosterUtil.fromUtilDate2SqlDate(rolloutDate), 28, "PUBLISHED", "John Chen Testing", true);
		return result;
	}

	public RolloutResult createRollout(int day, String region, java.sql.Date rolloutStartDate, String rolloutStatus,
			String currUser) throws Exception {
		RolloutResult result = null;

		if (conn == null) {
			conn = RosterUtil.getConnection();
		} else if (conn.isClosed()) {
			conn = RosterUtil.getConnection();
		}
		result = dao.populateRosterByRegion(conn, region, rolloutStartDate, day, rolloutStatus, currUser, true);

		return result;
	}

	public Date getLastRolloutDate(String regionCode) throws Exception {
		if (conn == null) {
			conn = RosterUtil.getConnection();
		} else if (conn.isClosed()) {
			conn = RosterUtil.getConnection();
		}
		return RosterUtil.fromSqlDate2UtilDate(AsisDao.getLastRolloutStartDateFromRegion(conn, regionCode));
	}

	public Date getNextRolloutDate(String regionCode) throws Exception {
		if (conn == null) {
			conn = RosterUtil.getConnection();
		} else if (conn.isClosed()) {
			conn = RosterUtil.getConnection();
		}
		return RosterUtil.fromSqlDate2UtilDate(AsisDao.getNextRolloutStartDateFromRegion(conn, regionCode));
	}

	public Date getStartRolloutDate(String regionCode) throws Exception {
		if (conn == null) {
			conn = RosterUtil.getConnection();
		} else if (conn.isClosed()) {
			conn = RosterUtil.getConnection();
		}
		return RosterUtil.fromSqlDate2UtilDate(AsisDao.getStartRolloutStartDateFromRegion(conn, regionCode));
	}

	public int cancelMonthBooking(String regionCode) throws Exception {
		if (conn == null) {
			conn = RosterUtil.getConnection();
		} else if (conn.isClosed()) {
			conn = RosterUtil.getConnection();
		}
		return AsisDao.clearMonthBooking(conn, regionCode);
	}

	public int checkAvailableSlotsNum(String regionCode) throws Exception {
		if (conn == null) {
			conn = RosterUtil.getConnection();
		} else if (conn.isClosed()) {
			conn = RosterUtil.getConnection();
		}
		return AsisDao.checkAvailableSlotsNum(conn, regionCode);
	}

	public int checkTotalSlotsNum(String regionCode) throws Exception {
		if (conn == null) {
			conn = RosterUtil.getConnection();
		} else if (conn.isClosed()) {
			conn = RosterUtil.getConnection();
		}
		return AsisDao.checkTotalSlotsNum(conn, regionCode);
	}
	public String checkAvailableSlotsPercent(String regionCode) throws Exception {
		if (conn == null) {
			conn = RosterUtil.getConnection();
		} else if (conn.isClosed()) {
			conn = RosterUtil.getConnection();
		}
		return AsisDao.checkAvailableSlotsPercent(conn, regionCode);
	}
	
	public List<RosterVisual> getRosterVisuals(String regionCode,String locationId,int rosterId)throws Exception{
		List<RosterVisual> rosters = null;
		if (conn == null) {
			conn = RosterUtil.getConnection();
		} else if (conn.isClosed()) {
			conn = RosterUtil.getConnection();
		}
		rosters=AsisDao.getRosterVisuals(conn, regionCode, locationId,rosterId);
		return rosters;
	}
	
	public String[] getLocations(String regionCode)throws Exception{
		if (conn == null) {
			conn = RosterUtil.getConnection();
		} else if (conn.isClosed()) {
			conn = RosterUtil.getConnection();
		}
		
		return AsisDao.getLocations(conn, regionCode);
	}

}
