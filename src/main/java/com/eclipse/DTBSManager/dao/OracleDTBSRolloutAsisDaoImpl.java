package com.eclipse.DTBSManager.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.eclipse.DTBSManager.bean.RosterVisual;
import com.eclipse.DTBSManager.util.RosterUtil;
/**
 * 
 * @author Leon Wang	
 * @email seasparta618@gmail.com || dev.leon618@gmail.com
 */
public class OracleDTBSRolloutAsisDaoImpl implements DTBSRolloutAsisDao {

	@Override
	public Date getNextRolloutStartDateFromRegion(Connection conn, String rolloutRegionCode) throws Exception {
		java.sql.Date lastRolloutDate = null;
		java.sql.Date nextRolloutDate = null;
		Calendar cal = GregorianCalendar.getInstance();
		cal.setFirstDayOfWeek(1);

		lastRolloutDate = this.getLastRolloutStartDateFromRegion(conn, rolloutRegionCode);

		if (lastRolloutDate != null) {
			cal.setTime(lastRolloutDate);

			while (cal.get(7) != 2) {
				cal.add(5, 1);
			}
			nextRolloutDate = new java.sql.Date(cal.getTimeInMillis());
		}
		return nextRolloutDate;
	}

	@Override
	public Date getLastRolloutStartDateFromRegion(Connection conn, String rolloutRegionCode) throws Exception {
		java.sql.Date lastRolloutDate = null;
		Calendar cal = GregorianCalendar.getInstance();
		cal.setFirstDayOfWeek(1);

		Connection dbConn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String sqlCmd = "select max(lrsbr.latest_slot_date) as latest_slot_date ";
		sqlCmd += "from ";
		sqlCmd += "(select rstr.rollout_region_id, max(rstr.roster_rollout_id) as roster_rollout_id from dtbs.dtbs_roster_rollout rstr group by rstr.rollout_region_id) lr ";
		sqlCmd += "join dtbs.dtbs_roster_rollout rstr on lr.roster_rollout_id = rstr.roster_rollout_id  ";
		sqlCmd += "join dtbs.dtbs_rollout_region rr on rr.rollout_region_id = rstr.rollout_region_id  ";
		sqlCmd += "join  ";
		sqlCmd += "(select max(rs.slot) as latest_slot_date, rs.roster_rollout_id from dtbs.roster_slots rs group by rs.roster_rollout_id) lrsbr on lr.roster_rollout_id = lrsbr.roster_rollout_id  ";
		sqlCmd += "where rr.rollout_region_code = ? or rr.rollout_region_code = 'ALL' ";
		RosterUtil.report(sqlCmd);
		try {
			dbConn = conn;// RosterUtil.getConnection();
			stmt = dbConn.prepareStatement(sqlCmd);
			stmt.setString(1, rolloutRegionCode);
			rs = stmt.executeQuery();

			if (rs.next()) {
				lastRolloutDate = rs.getDate(1);
			}
			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (lastRolloutDate == null) {
			System.out.println(
					"RosterRollout.getNextRolloutStartDateFromRegion: lastRolloutDate was null. Throwing IllegalArgumentException.");
			throw new IllegalArgumentException("The rollout region code " + rolloutRegionCode
					+ " was not valid or at least a latest rollout date could not be found.");
		}
		return lastRolloutDate;
	}

	@Override
	public int clearMonthBooking(Connection conn, String rolloutRegionCode) throws Exception {
		String sql = "update DTBS_TEST_ROSTER set ROSTER_SLOT_ID = null where ROSTER_SLOT_ID in ( ";
		sql += " select ROSTER_SLOT_ID from ROSTER_SLOTS rs where rs.ROSTER_ROLLOUT_ID = ( ";
		sql += " select max(rstr.roster_rollout_id) as roster_rollout_id ";
		sql += " from dtbs.dtbs_roster_rollout rstr, dtbs.dtbs_rollout_region drr where rstr.rollout_region_id=drr.rollout_region_id AND drr.ROLLOUT_REGION_CODE =? group by rstr.rollout_region_id))";

		PreparedStatement ps = null;
		int result = 0;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, rolloutRegionCode);
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int checkAvailableSlotsNum(Connection conn, String rolloutRegionCode) throws Exception {
		String sql = "select count(ROSTER_SLOT_ID) from ROSTER_SLOTS rs where rs.ROSTER_ROLLOUT_ID = ( ";
		sql += "select max(rstr.roster_rollout_id) as roster_rollout_id ";
		sql += "from dtbs.dtbs_roster_rollout rstr, dtbs.dtbs_rollout_region drr where rstr.rollout_region_id=drr.rollout_region_id AND drr.ROLLOUT_REGION_CODE =? group by rstr.rollout_region_id)  ";
		sql += "AND rs.ROSTER_SLOT_ID NOT IN ( SELECT ROSTER_SLOT_ID FROM DTBS_TEST_ROSTER WHERE ROSTER_SLOT_ID is not null )  ";

		PreparedStatement ps = null;
		int result = 0;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, rolloutRegionCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int checkTotalSlotsNum(Connection conn, String rolloutRegionCode) throws Exception {
		String sql = "select count(ROSTER_SLOT_ID) from ROSTER_SLOTS rs where rs.ROSTER_ROLLOUT_ID = ( ";
		sql += "select max(rstr.roster_rollout_id) as roster_rollout_id ";
		sql += "from dtbs.dtbs_roster_rollout rstr, dtbs.dtbs_rollout_region drr where rstr.rollout_region_id=drr.rollout_region_id AND drr.ROLLOUT_REGION_CODE =? group by rstr.rollout_region_id)  ";

		PreparedStatement ps = null;
		int result = 0;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, rolloutRegionCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Date getStartRolloutStartDateFromRegion(Connection conn, String rolloutRegionCode) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		java.sql.Date lastRolloutDate = null;

		String sqlCmd = "select min(lrsbr.latest_slot_date) as latest_slot_date ";
		sqlCmd += "from ";
		sqlCmd += "(select rstr.rollout_region_id, max(rstr.roster_rollout_id) as roster_rollout_id from dtbs.dtbs_roster_rollout rstr group by rstr.rollout_region_id) lr ";
		sqlCmd += "join dtbs.dtbs_roster_rollout rstr on lr.roster_rollout_id = rstr.roster_rollout_id  ";
		sqlCmd += "join dtbs.dtbs_rollout_region rr on rr.rollout_region_id = rstr.rollout_region_id  ";
		sqlCmd += "join  ";
		sqlCmd += "(select min(rs.slot) as latest_slot_date, rs.roster_rollout_id from dtbs.roster_slots rs group by rs.roster_rollout_id) lrsbr on lr.roster_rollout_id = lrsbr.roster_rollout_id  ";
		sqlCmd += "where rr.rollout_region_code = ? or rr.rollout_region_code = 'ALL' ";
		try {
			stmt = conn.prepareStatement(sqlCmd);
			stmt.setString(1, rolloutRegionCode);
			rs = stmt.executeQuery();
			if (rs.next()) {
				lastRolloutDate = rs.getDate(1);
			}
			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lastRolloutDate;
	}

	@Override
	public String checkAvailableSlotsPercent(Connection conn, String rolloutRegionCode) throws Exception {
		String result = "";
		int totalSlots = 0;
		int availableSlots = 0;
		totalSlots = this.checkTotalSlotsNum(conn, rolloutRegionCode);
		availableSlots = this.checkAvailableSlotsNum(conn, rolloutRegionCode);
		result = RosterUtil.getPercentage(totalSlots, availableSlots);
		return result;
	}

	@Override
	public List<RosterVisual> getRosterVisuals(Connection conn, String rolloutRegionCode) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<RosterVisual> rosters = new ArrayList<RosterVisual>();
		RosterVisual roster = null;
		int locationId = 0;
		String sqlCmd = "select rs.ROSTER_SLOT_ID, rs.SLOT, rs.LOCATION_ID, dtr.DTBS_TEST_ROSTER_ID from ROSTER_SLOTS rs ";
		sqlCmd += "Left Join DTBS_TEST_ROSTER dtr On rs.ROSTER_SLOT_ID = dtr.ROSTER_SLOT_ID ";
		sqlCmd += "where rs.ROSTER_ROLLOUT_ID = (  ";
		sqlCmd += "select max(rstr.roster_rollout_id) as roster_rollout_id  ";
		sqlCmd += "from dtbs.dtbs_roster_rollout rstr, dtbs.dtbs_rollout_region drr where rstr.rollout_region_id=drr.rollout_region_id AND drr.ROLLOUT_REGION_CODE =? group by rstr.rollout_region_id)  ";
		sqlCmd += "order by Location_ID, SLOT ";
		try {
			stmt = conn.prepareStatement(sqlCmd);
			stmt.setString(1, rolloutRegionCode);
			rs = stmt.executeQuery();
			while (rs.next()) {
				roster = new RosterVisual();
				roster.setRosterSlotId(rs.getInt(1));
				roster.setSlot(rs.getString(2));
				roster.setLocationId(rs.getInt(3));
				roster.setTestRosterId(rs.getInt(4));
				if (locationId == 0) {
					locationId = roster.getLocationId();
				} else if (locationId != roster.getLocationId()) {
					// Going to process another LocationId
					locationId = roster.getLocationId();
					// To save this location to file and clear up the rosters
					// container.
					RosterUtil.processSaveTsvFile(rosters, locationId);
					rosters.clear();
				}
				rosters.add(roster);
			}
			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<RosterVisual> getRosterVisuals(Connection conn, String rolloutRegionCode, String locationId,
			int rosterId) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<RosterVisual> rosters = null;
		int rolloutId = this.getLatestRolloutIdByRegion(conn, rolloutRegionCode);
		int index = 0;
		if (rolloutId != 0) {
			rosters = new ArrayList<RosterVisual>();
			RosterVisual roster = null;
			String sqlCmd = "SELECT drsm.DTBS_ROSTER_SLOTS_MAP_ID, count(drm.DTBS_ROSTER_MAP_ID) ";
			sqlCmd += "FROM DTBS_ROSTER_SLOTS_MAP drsm ";
			sqlCmd += "LEFT JOIN DTBS_ROSTER_MAP drm ";
			sqlCmd += "ON drsm.DTBS_ROSTER_SLOTS_MAP_ID = drm.DTBS_ROSTER_SLOT_MAP_ID ";
			sqlCmd += "WHERE drsm.LOCATION_ID = ? ";
			sqlCmd += "AND drsm.DTBS_ROSTER_ID = ? ";
			sqlCmd += "GROUP BY drsm.DTBS_ROSTER_SLOTS_MAP_ID ";
			sqlCmd += "ORDER BY drsm.DTBS_ROSTER_SLOTS_MAP_ID ";
			stmt = conn.prepareStatement(sqlCmd);
			stmt.setString(1, locationId);
			stmt.setInt(2, rosterId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				roster = new RosterVisual();
				roster.setSlotMapId(rs.getInt(1));
				roster.setTotal(rs.getInt(2));
				rosters.add(roster);
			}

			sqlCmd = "SELECT rs.ROSTER_SLOT_ID, count(dtr.DTBS_TEST_ROSTER_ID) ";
			sqlCmd += "FROM ROSTER_SLOTS rs ";
			sqlCmd += "LEFT JOIN DTBS_TEST_ROSTER dtr ";
			sqlCmd += "ON rs.ROSTER_SLOT_ID = dtr.ROSTER_SLOT_ID ";
			sqlCmd += "WHERE rs.LOCATION_ID = ? ";
			sqlCmd += "AND rs.DTBS_ROSTER_ID = ? ";
			sqlCmd += "AND rs.ROSTER_ROLLOUT_ID=? ";
			sqlCmd += "GROUP BY (rs.ROSTER_SLOT_ID) ";
			sqlCmd += "ORDER BY rs.ROSTER_SLOT_ID ";

			stmt = conn.prepareStatement(sqlCmd);
			stmt.setString(1, locationId);
			stmt.setInt(2, rosterId);
			stmt.setInt(3, rolloutId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				roster = rosters.get(index);
				roster.setRosterSlotId(rs.getInt(1));
				roster.setUsed(rs.getInt(2));
				index++;
			}
			rs.close();
			stmt.close();

		}
		return rosters;
	}

	public List<RosterVisual> getRosterVisuals_bak(Connection conn, String rolloutRegionCode, String locationId)
			throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<RosterVisual> rosters = new ArrayList<RosterVisual>();
		RosterVisual roster = null;
		String sqlCmd = "select rs.ROSTER_SLOT_ID, rs.SLOT, rs.LOCATION_ID, dtr.DTBS_TEST_ROSTER_ID from ROSTER_SLOTS rs ";
		sqlCmd += "Left Join DTBS_TEST_ROSTER dtr On rs.ROSTER_SLOT_ID = dtr.ROSTER_SLOT_ID ";
		sqlCmd += "where  rs.LOCATION_ID=? AND rs.ROSTER_ROLLOUT_ID = (  ";
		sqlCmd += "select max(rstr.roster_rollout_id) as roster_rollout_id  ";
		sqlCmd += "from dtbs.dtbs_roster_rollout rstr, dtbs.dtbs_rollout_region drr where rstr.rollout_region_id=drr.rollout_region_id AND drr.ROLLOUT_REGION_CODE =? group by rstr.rollout_region_id)  ";
		sqlCmd += "order by Location_ID, SLOT ";
		try {
			stmt = conn.prepareStatement(sqlCmd);
			stmt.setString(1, locationId);
			stmt.setString(2, rolloutRegionCode);
			rs = stmt.executeQuery();
			while (rs.next()) {
				roster = new RosterVisual();
				roster.setRosterSlotId(rs.getInt(1));
				roster.setSlot(rs.getString(2));
				roster.setLocationId(rs.getInt(3));
				roster.setTestRosterId(rs.getInt(4));
				rosters.add(roster);
			}
			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rosters;
	}

	public String[] getLocations(Connection conn, String rolloutRegionCode) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> ids = new ArrayList<String>();
		String sqlCmd = "SELECT loc.LOCATION_ID ";
		sqlCmd += " FROM  DTBS_ROLLOUT_REGION drr, DTBS_ROLLOUT_REGION_AREA drra, LOCATION loc  ";
		sqlCmd += " where drr.ROLLOUT_REGION_ID = drra.ROLLOUT_REGION_ID   ";
		sqlCmd += " AND drr.ROLLOUT_REGION_CODE = ?   ";
		sqlCmd += " AND drra.AREA_ID = loc.AREA_ID   ";
		sqlCmd += " ORDER BY loc.LOCATION_ID ";
		try {
			stmt = conn.prepareStatement(sqlCmd);
			stmt.setString(1, rolloutRegionCode);
			rs = stmt.executeQuery();
			while (rs.next()) {
				ids.add(rs.getString(1));
			}
			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		String[] result = new String[ids.size()];
		ids.toArray(result);
		return result;
	}

	public int getLatestRolloutIdByRegion(Connection conn, String rolloutRegionCode) throws Exception {
		int result = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sqlCmd = "select max(rstr.roster_rollout_id) as roster_rollout_id ";
		sqlCmd += "from dtbs.dtbs_roster_rollout rstr, dtbs.dtbs_rollout_region drr ";
		sqlCmd += "where rstr.rollout_region_id=drr.rollout_region_id  ";
		sqlCmd += "AND drr.ROLLOUT_REGION_CODE =? group by rstr.rollout_region_id ";
		try {
			stmt = conn.prepareStatement(sqlCmd);
			stmt.setString(1, rolloutRegionCode);
			rs = stmt.executeQuery();
			while (rs.next()) {
				result = rs.getInt(1);
			}
			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

}
