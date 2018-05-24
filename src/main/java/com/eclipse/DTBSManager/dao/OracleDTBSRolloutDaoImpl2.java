package com.eclipse.DTBSManager.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.eclipse.DTBSManager.bean.RolloutResult;
import com.eclipse.DTBSManager.bean.TestRosterQuery;
import com.eclipse.DTBSManager.util.RosterUtil;
/**
 * 
 * @author Leon Wang	
 * @email seasparta618@gmail.com || dev.leon618@gmail.com
 */
public class OracleDTBSRolloutDaoImpl2 implements DTBSRolloutDao {

	@Override
	public boolean populateRoster(Connection conn) throws Exception {
		int intWeek = 1;
		int intDay = 0;
		GregorianCalendar gcalStartDate = new GregorianCalendar();
		GregorianCalendar gcalMapBaseDate = new GregorianCalendar();
		gcalMapBaseDate = this.initialCalen(gcalStartDate);
		gcalMapBaseDate.setFirstDayOfWeek(Calendar.SUNDAY);

		conn.setAutoCommit(false);
		Statement stmt2 = conn.createStatement();

		// ActiveRosterIds
		ArrayList<Integer> rosterIDs = this.getActiveRosterIds(conn);

		ResultSet rs = null;

		for (int rosterID : rosterIDs) {
			intWeek = 1;
			for (int intDayInc = 0; intDayInc <= 5; intDayInc++) {
				intDay = gcalStartDate.get(Calendar.DAY_OF_WEEK);
				if ((intDay >= 2) && (intDay <= 6)) {
					this.createRosterSlot(rosterID, intDay, intWeek, gcalStartDate, conn);
				} else {
					if (gcalStartDate.get(Calendar.DAY_OF_WEEK) == 7) {
						intWeek++;
					}
				}
				gcalStartDate.add(Calendar.DATE, 1);
			}
			gcalMapBaseDate.add(Calendar.DATE, -6);

			rs = this.obtainRosterSlot(rosterID, conn);

			int counter = 1;
			stmt2 = conn.createStatement();
			while (rs.next()) {
				this.createTestRoster(Integer.parseInt(rs.getString(1)), Integer.parseInt(rs.getString(2)),
						rs.getString(3), rs.getLong(4), rosterID, rs.getLong(5), rs.getInt(6), rs.getInt(7),
						gcalMapBaseDate, null);
				if (counter % 100 == 0) {
					stmt2.executeBatch();
				}
				counter++;
			}
			stmt2.executeBatch();
			stmt2.close();
		}
		conn.commit();
		conn.setAutoCommit(true);

		return false;
	}

	@Override
	public RolloutResult populateRosterByRegion(Connection conn, String rolloutRegionCode, Date rosterStartDate,
			int days, String rolloutStatus, String currUser, boolean doUpdates) throws Exception {
		RolloutResult result = new RolloutResult();
		result.setResult("start to populate roster by region");

		int intWeek = 1;
		int intDay = 0;
		GregorianCalendar gcalStartDate = new GregorianCalendar();
		gcalStartDate.setTime(rosterStartDate);
		GregorianCalendar gcalMapBaseDate = new GregorianCalendar();
		RosterUtil.report(gcalStartDate.get(Calendar.DATE) + "/" + (gcalStartDate.get(Calendar.MONTH) + 1) + "/"
				+ gcalStartDate.get(Calendar.YEAR));
		gcalMapBaseDate = this.initialCalen(gcalStartDate);
		gcalMapBaseDate.setFirstDayOfWeek(Calendar.SUNDAY);

		ResultSet rosterSlotsResults = null;

		this.createRosterRollout(conn, rolloutRegionCode, rosterStartDate, rolloutStatus, currUser);
		result.setRoterRolloutCount(1);

		// ActiveRosterIds
		ArrayList<Integer> rosterIDs = this.getActiveRosterIds(conn);
		for (int rosterID : rosterIDs) {
			intWeek = 1;
			long start = System.currentTimeMillis();
			result.setIntDay(days);
			for (int intDayInc = 0; intDayInc <= days; intDayInc++) {
				intDay = gcalStartDate.get(Calendar.DAY_OF_WEEK);
				RosterUtil.report("begain to create roster slot " + intDay);
				RosterUtil.report(gcalStartDate.get(Calendar.DATE) + "/" + (gcalStartDate.get(Calendar.MONTH) + 1) + "/"
						+ gcalStartDate.get(Calendar.YEAR));
				if ((intDay >= 2) && (intDay <= 6)) {
					result.setRosterSlotsCount(result.getRosterSlotsCount() + this.createRosterSlot(rosterID, intDay,
							intWeek, gcalStartDate, rolloutRegionCode, true, conn));

				} else {
					if (gcalStartDate.get(Calendar.DAY_OF_WEEK) == 7) {
						intWeek++;
					}
				}
				gcalStartDate.add(Calendar.DATE, 1);
			}
			RosterUtil.report("Done for creating the roster slot and costing: " + (System.currentTimeMillis() - start));
			// ==================================================================//
			RosterUtil.report("began to obtain roster slot===================id:" + rosterID + " regioncode:"
					+ rolloutRegionCode);
			rosterSlotsResults = this.obtainRosterSlot(rosterID, rolloutRegionCode, true, conn);
			// gcalMapBaseDate.add(Calendar.DATE, -2);
			int counter = 0;
		//	conn.setAutoCommit(false);
			
		//	PreparedStatement ps = this.createTestRosterPreStmt(conn);
			PreparedStatement ps = this.queryRosterPreStmt(conn);
			long time4InsertTestRoster = System.currentTimeMillis();

			long record4Roster = System.currentTimeMillis();

			TestRosterQuery[] rosterQuerys = new TestRosterQuery[500];
			TestRosterQuery rosterQuery = null;
			while (rosterSlotsResults.next()) {
				rosterQuery = new TestRosterQuery(Integer.parseInt(rosterSlotsResults.getString(1)),
						Integer.parseInt(rosterSlotsResults.getString(2)), rosterSlotsResults.getString(3),
						rosterSlotsResults.getLong(4), rosterSlotsResults.getLong(5), rosterSlotsResults.getInt(6),
						rosterSlotsResults.getInt(7));
				rosterQuerys[counter % 500] = rosterQuery;
				if (counter % 499 == 0 && counter != 0) {
					int innerCount = 0;
					while (innerCount < 500) {
						
						//pass an object this method and after calculating and assign the roster_id back to this object, finally return this object.
						this.createTestRosterPre(rosterQuerys[innerCount].getIntMinute(),
								rosterQuerys[innerCount].getIntHour(),
								rosterQuerys[innerCount].getStringTesterStatusCode(),
								rosterQuerys[innerCount].getLongResourceId(), rosterID,
								rosterQuerys[innerCount].getLongLocationId(), rosterQuerys[innerCount].getIntDay(),
								rosterQuerys[innerCount].getIntWeek(), gcalMapBaseDate, ps,rosterQuerys[innerCount]);
						
						innerCount++;
					}
					RosterUtil.report("Marking....");
					//ps.executeBatch();
				}
				counter++;
			}
		//	conn.commit();
		//	conn.setAutoCommit(true);
			ps.close();
			RosterUtil.report("rosterSlotResult finished");
			RosterUtil.report(counter);
			RosterUtil.report(System.currentTimeMillis() - record4Roster);
			// ps.executeBatch();
			// result.setTestRosterCount(counter);
			// conn.commit();
			// conn.setAutoCommit(true);
			// ps.close();
		}
		return result;
	}

	private GregorianCalendar initialCalen(GregorianCalendar gcalStartDate) {
		gcalStartDate.setFirstDayOfWeek(Calendar.SUNDAY);

		int intDay = gcalStartDate.get(Calendar.DAY_OF_WEEK);
		if (intDay == 1) {
			gcalStartDate.add(Calendar.DATE, 1);
		} else if (intDay == 7) {
			gcalStartDate.add(Calendar.DATE, 2);
		}
		RosterUtil.report(gcalStartDate.get(Calendar.DATE) + "/" + (gcalStartDate.get(Calendar.MONTH) + 1) + "/"
				+ gcalStartDate.get(Calendar.YEAR));
		return gcalStartDate;
	}

	private void createTestRoster(int intHour, int intMinute, String stringTesterStatusCode, long longResourceId,
			int rosterID, long longLocationId, int intDay, int intWeek, GregorianCalendar gcalMapBaseDate,
			PreparedStatement ps) throws SQLException {

		GregorianCalendar gcalMapDate = new GregorianCalendar(gcalMapBaseDate.get(Calendar.YEAR),
				gcalMapBaseDate.get(Calendar.MONTH), gcalMapBaseDate.get(Calendar.DATE));
		gcalMapDate.setFirstDayOfWeek(Calendar.SUNDAY);

		gcalMapDate.add(Calendar.DATE, ((intWeek - 1) * 7) + (intDay - 2));
		gcalMapDate.set(gcalMapDate.get(Calendar.YEAR), gcalMapDate.get(Calendar.MONTH), gcalMapDate.get(Calendar.DATE),
				intHour, intMinute);
		String stringSlotString = ("00" + gcalMapDate.get(Calendar.DATE))
				.substring(("00" + gcalMapDate.get(Calendar.DATE)).length() - 2)
				+ "/"
				+ ("00" + (gcalMapDate.get(Calendar.MONTH) + 1))
						.substring(("00" + (gcalMapDate.get(Calendar.MONTH) + 1)).length() - 2)
				+ "/" + gcalMapDate.get(Calendar.YEAR) + " "
				+ ("00" + gcalMapDate.get(Calendar.HOUR_OF_DAY))
						.substring(("00" + gcalMapDate.get(Calendar.HOUR_OF_DAY)).length() - 2)
				+ ":" + ("00" + gcalMapDate.get(Calendar.MINUTE))
						.substring(("00" + gcalMapDate.get(Calendar.MINUTE)).length() - 2);

	//	RosterUtil.report(stringSlotString);
		ps.clearParameters();
		ps.setLong(1, longLocationId);
		ps.setString(2, stringSlotString);
		ps.setInt(3, rosterID);
		ps.setLong(4, longLocationId);
		ps.setLong(5, longResourceId);
		ps.setString(6, stringTesterStatusCode);
		ps.addBatch();

	}
	
	private void createTestRosterPre(int intHour, int intMinute, String stringTesterStatusCode, long longResourceId,
			int rosterID, long longLocationId, int intDay, int intWeek, GregorianCalendar gcalMapBaseDate,
			PreparedStatement ps, TestRosterQuery query) throws SQLException {

		GregorianCalendar gcalMapDate = new GregorianCalendar(gcalMapBaseDate.get(Calendar.YEAR),
				gcalMapBaseDate.get(Calendar.MONTH), gcalMapBaseDate.get(Calendar.DATE));
		gcalMapDate.setFirstDayOfWeek(Calendar.SUNDAY);

		gcalMapDate.add(Calendar.DATE, ((intWeek - 1) * 7) + (intDay - 2));
		gcalMapDate.set(gcalMapDate.get(Calendar.YEAR), gcalMapDate.get(Calendar.MONTH), gcalMapDate.get(Calendar.DATE),
				intHour, intMinute);
		String stringSlotString = ("00" + gcalMapDate.get(Calendar.DATE))
				.substring(("00" + gcalMapDate.get(Calendar.DATE)).length() - 2)
				+ "/"
				+ ("00" + (gcalMapDate.get(Calendar.MONTH) + 1))
						.substring(("00" + (gcalMapDate.get(Calendar.MONTH) + 1)).length() - 2)
				+ "/" + gcalMapDate.get(Calendar.YEAR) + " "
				+ ("00" + gcalMapDate.get(Calendar.HOUR_OF_DAY))
						.substring(("00" + gcalMapDate.get(Calendar.HOUR_OF_DAY)).length() - 2)
				+ ":" + ("00" + gcalMapDate.get(Calendar.MINUTE))
						.substring(("00" + gcalMapDate.get(Calendar.MINUTE)).length() - 2);

	//	RosterUtil.report(stringSlotString+" location: "+longLocationId+";roster:"+rosterID);
		ps.clearParameters();
		ps.setLong(1, longLocationId);
		ps.setString(2, stringSlotString);
		ps.setInt(3, rosterID);
		ResultSet rs = ps.executeQuery();
		if(rs.next()){
			RosterUtil.report(rs.getInt(1));
			query.setRosterSlotId(rs.getInt(1));
		}
		//ps.addBatch();

	}

	private PreparedStatement createTestRosterPreStmt(Connection conn) throws SQLException {
		PreparedStatement ps = null;
		String stringCommand = "insert into DTBS.DTBS_TEST_ROSTER ";
		stringCommand += "(";
		stringCommand += "    ROSTER_SLOT_ID, ";
		stringCommand += "    LOCATION_ID, ";
		stringCommand += "    DTBS_TEST_RESOURCE_ID, ";
		stringCommand += "    TEST_ROSTER_STATUS_CODE ";
		stringCommand += ") ";
		stringCommand += "values ";
		stringCommand += "(";
		stringCommand += "    (SELECT ROSTER_SLOT_ID FROM DTBS.ROSTER_SLOTS WHERE LOCATION_ID = ? ";
		stringCommand += " AND TO_CHAR(SLOT, 'dd/mm/yyyy HH24:MI') = ?  AND DTBS_ROSTER_ID = ? )";
		stringCommand += ("    , ? ");
		stringCommand += ("    , ? ");
		stringCommand += ("	, ?  )");
		// RosterUtil.report(stringCommand);
		ps = conn.prepareStatement(stringCommand);
		return ps;
	}
	
	private PreparedStatement createTestRosterPreStmt1(Connection conn) throws SQLException {
		PreparedStatement ps = null;
		String stringCommand = "insert into DTBS.DTBS_TEST_ROSTER ";
		stringCommand += "(";
		stringCommand += "    ROSTER_SLOT_ID, ";
		stringCommand += "    LOCATION_ID, ";
		stringCommand += "    DTBS_TEST_RESOURCE_ID, ";
		stringCommand += "    TEST_ROSTER_STATUS_CODE ";
		stringCommand += ") ";
		stringCommand += "values ";
		stringCommand += "( ?,?,?,?";
		// RosterUtil.report(stringCommand);
		ps = conn.prepareStatement(stringCommand);
		return ps;
	}
	
	private PreparedStatement queryRosterPreStmt(Connection conn) throws SQLException {
		PreparedStatement ps = null;
		String stringCommand = "SELECT ROSTER_SLOT_ID FROM DTBS.ROSTER_SLOTS WHERE LOCATION_ID = ? ";
		stringCommand += " AND TO_CHAR(SLOT, 'dd/mm/yyyy HH24:MI') = ?  AND DTBS_ROSTER_ID = ? ";
		ps = conn.prepareStatement(stringCommand);
		return ps;
	}
	

	private PreparedStatement createRosterSlotPreStmt(Connection conn) throws SQLException {
		PreparedStatement ps = null;
		String stringCommand = "insert into DTBS.ROSTER_SLOTS (LOCATION_ID, SLOT, BOOKING_SYSTEM_CODE, DAY, WEEK, DTBS_ROSTER_ID) values ( ? , TO_DATE(?,'dd/mm/yyyy HH24:MI') ,'DTBS' , ?,?,?)";
		// RosterUtil.report(stringCommand);
		ps = conn.prepareStatement(stringCommand);
		return ps;
	}

	private ResultSet obtainRosterSlot(int rosterID, Connection conn) throws SQLException {
		return this.obtainRosterSlot(rosterID, null, false, conn);

	}

	private ResultSet obtainRosterSlot(int rosterID, String rolloutRegionCode, boolean byRegionCode, Connection conn)
			throws SQLException {
		String regionCode = "";
		String regionTable = "";
		if (byRegionCode) {
			regionCode += "  AND RRA.AREA_ID = LOC.AREA_ID ";
			regionCode += "   AND RRA.ROLLOUT_REGION_ID = RR.ROLLOUT_REGION_ID ";
			regionCode += "    AND RR.ROLLOUT_REGION_CODE = '" + rolloutRegionCode + "' ";

			regionTable += " DTBS.DTBS_ROLLOUT_REGION_AREA RRA, DTBS.DTBS_ROLLOUT_REGION RR ";
		}
		String stringCommand = "SELECT ";
		stringCommand += "    TO_CHAR(b.SLOT, 'HH24') HOUR, ";
		stringCommand += "    TO_CHAR(b.SLOT, 'MI') MINUTE, ";
		stringCommand += "    a.TESTER_STATUS_CODE, ";
		stringCommand += "    a.DTBS_TEST_RESOURCE_ID, ";
		stringCommand += "    b.LOCATION_ID, ";
		stringCommand += "    b.DAY, ";
		stringCommand += "    b.WEEK ";
		stringCommand += "FROM ";
		stringCommand += "    DTBS.DTBS_ROSTER_MAP a, DTBS.DTBS_ROSTER_SLOTS_MAP b, ";
		stringCommand += "    DTBS.DTBS_SLOTS c, DTBS.DTBS_ROSTER_LOCATIONS d, DTBS.LOCATION LOC, ";
		stringCommand += regionTable;
		stringCommand += "WHERE ";
		stringCommand += "    b.DTBS_ROSTER_SLOTS_MAP_ID = a.DTBS_ROSTER_SLOT_MAP_ID ";
		stringCommand += "    AND b.DTBS_ROSTER_ID = " + rosterID;
		stringCommand += "    AND b.DTBS_SLOTS_ID = c.DTBS_SLOTS_ID ";
		stringCommand += "    AND b.DTBS_ROSTER_LOCATIONS_ID = d.DTBS_ROSTER_LOCATIONS_ID ";
		stringCommand += "    AND d.LOCATION_ID = LOC.LOCATION_ID ";
		stringCommand += "    AND c.ACTIVE = 'Y' ";
		stringCommand += "    AND d.ACTIVE = 'Y' ";
		stringCommand += "    AND LOC.IS_CURRENT = 'Y' ";
		stringCommand += regionCode;
		stringCommand += " ORDER BY ";
		stringCommand += "a.DTBS_ROSTER_MAP_ID";
		// RosterUtil.report(stringCommand);
		return conn.createStatement().executeQuery(stringCommand);

	}

	private ResultSet obtainSlotLocation(int rosterID, int intDay, int intWeek, Connection conn) throws SQLException {
		return this.obtainSlotLocation(rosterID, intDay, intWeek, null, false, conn);
	}

	private ResultSet obtainSlotLocation(int rosterID, int intDay, int intWeek, String rolloutRegionCode,
			boolean byRegionCode, Connection conn) throws SQLException {

		String stringRegionCode = "";
		if (byRegionCode) {
			stringRegionCode = "SELECT TO_CHAR(a.SLOT, 'hh24:mi'),  c.LOCATION_ID "
					+ " FROM DTBS.DTBS_ROSTER_SLOTS_MAP a, DTBS.DTBS_SLOTS b, DTBS.DTBS_ROSTER_LOCATIONS c, DTBS.LOCATION LOC, "
					+ " DTBS.DTBS_ROLLOUT_REGION_AREA RRA, DTBS.DTBS_ROLLOUT_REGION RR" + " WHERE a.DTBS_ROSTER_ID = "
					+ rosterID + " AND a.DTBS_SLOTS_ID = b.DTBS_SLOTS_ID"
					+ " AND a.DTBS_ROSTER_LOCATIONS_ID = c.DTBS_ROSTER_LOCATIONS_ID"
					+ " AND c.LOCATION_ID = LOC.LOCATION_ID" + " AND a.WEEK = " + intWeek + " AND a.DAY = " + intDay
					+ " AND b.ACTIVE = 'Y'" + " AND c.ACTIVE = 'Y'" + " AND LOC.IS_CURRENT = 'Y'"
					+ " AND RRA.AREA_ID = LOC.AREA_ID" + " AND RRA.ROLLOUT_REGION_ID = RR.ROLLOUT_REGION_ID"
					+ " AND RR.ROLLOUT_REGION_CODE = '" + rolloutRegionCode + "' " + " ORDER BY 2, 1";
		} else {
			stringRegionCode = "SELECT TO_CHAR(a.SLOT, 'hh24:mi'), c.LOCATION_ID"
					+ " FROM DTBS.DTBS_ROSTER_SLOTS_MAP a, DTBS.DTBS_SLOTS b, DTBS.DTBS_ROSTER_LOCATIONS c, DTBS.LOCATION d"
					+ " WHERE a.DTBS_ROSTER_ID = " + rosterID + " AND a.DTBS_SLOTS_ID = b.DTBS_SLOTS_ID"
					+ " AND a.DTBS_ROSTER_LOCATIONS_ID = c.DTBS_ROSTER_LOCATIONS_ID"
					+ " AND c.LOCATION_ID = d.LOCATION_ID" + " AND a.WEEK = " + intWeek + " AND a.DAY = " + intDay
					+ " AND b.ACTIVE = 'Y'" + " AND c.ACTIVE = 'Y'"
					+ " AND d.IS_CURRENT = 'Y' ORDER BY c.LOCATION_ID, a.WEEK, a.DAY, a.SLOT";

		}
		RosterUtil.report("obtainSlouLocation: " + stringRegionCode);
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(stringRegionCode);
		// stmt.close();
		// stmt=null;
		return rs;
	}

	private void createRosterSlot(int rosterID, int intDay, int intWeek, GregorianCalendar gcalStartDate,
			Connection conn) throws SQLException {
		this.createRosterSlot(rosterID, intDay, intWeek, gcalStartDate, null, false, conn);
	}

	private int createRosterSlot(int rosterID, int intDay, int intWeek, GregorianCalendar gcalStartDate,
			String rolloutRegionCode, boolean byRegionCode, Connection conn) throws SQLException {
		long longLocationId = 0;
		String stringSlotString = null;
		ResultSet rs = null;
		if (byRegionCode) {
			rs = this.obtainSlotLocation(rosterID, intDay, intWeek, rolloutRegionCode, true, conn);
		} else {
			rs = this.obtainSlotLocation(rosterID, intDay, intWeek, conn);
		}
		PreparedStatement ps = this.createRosterSlotPreStmt(conn);
		int count = 0;
		while (rs.next()) {
			longLocationId = rs.getLong(2);
			gcalStartDate.set(gcalStartDate.get(Calendar.YEAR), gcalStartDate.get(Calendar.MONTH),
					gcalStartDate.get(Calendar.DATE), 0, 0);

			stringSlotString = gcalStartDate.get(Calendar.DATE) + "/" + (gcalStartDate.get(Calendar.MONTH) + 1) + "/"
					+ gcalStartDate.get(Calendar.YEAR) + " " + rs.getString(1);

			ps.setLong(1, longLocationId);
			ps.setString(2, stringSlotString);
			ps.setInt(3, intDay);
			ps.setInt(4, intWeek);
			ps.setInt(5, rosterID);
			ps.addBatch();
			ps.clearParameters();
			// RosterUtil.report(stringCommand);

			count++;
			if (count % 50 == 0) {
				ps.executeBatch();
			}

		}
		ps.executeBatch();
		ps.close();
		ps = null;
		rs.close();
		rs = null;
		return count;
	}

	private ArrayList<Integer> getActiveRosterIds(Connection conn) throws SQLException {
		String stringCommand = "SELECT DTBS_ROSTER_ID FROM DTBS.DTBS_ROSTER WHERE ACTIVE = 'Y'";
		ResultSet rs = conn.createStatement().executeQuery(stringCommand);
		ArrayList<Integer> rosterIDs = new ArrayList<Integer>();
		int rosterCount = 0;
		while (rs.next()) {
			rosterIDs.add(rosterCount, rs.getInt(1));
			rosterCount++;
		}
		return rosterIDs;
	}

	@Override
	public long createRosterRollout(Connection dbConn, String rolloutRegionCode, Date rolloutStartDate,
			String rolloutStatus, String currUser) throws Exception {
		long rosterRolloutId = -1L;
		String insCmd = "insert into dtbs.dtbs_roster_rollout (   rollout_region_id,   rollout_init_date,   rollout_start_date,   rollout_status,   last_update,   last_update_user ) "
				+ "values (   (select rollout_region_id from dtbs.dtbs_rollout_region where rollout_region_code = ?),   sysdate,   ?,   ?,   sysdate,   ?  )";

		String[] key = { "ROSTER_ROLLOUT_ID" };

		try {
			PreparedStatement stmt = dbConn.prepareStatement(insCmd, key);
			stmt.setString(1, rolloutRegionCode);
			stmt.setDate(2, rolloutStartDate);
			stmt.setString(3, rolloutStatus);
			stmt.setString(4, currUser);

			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();
			while (rs.next()) {
				rosterRolloutId = rs.getLong(1);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return rosterRolloutId;
	}

}
