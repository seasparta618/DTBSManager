package com.eclipse.DTBSManager.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;

import com.eclipse.DTBSManager.bean.RosterVisual;
import com.eclipse.DTBSManager.dao.DTBSRolloutAsisDao;
import com.eclipse.DTBSManager.dao.OracleDTBSRolloutAsisDaoImpl;
import com.eclipse.DTBSManager.service.RolloutService;
/**
 * 
 * @author Leon Wang	
 * @email seasparta618@gmail.com || dev.leon618@gmail.com
 */
public class GeneralTest {

	// @Test
	public void TestConnection() {
		Connection conn = RosterUtil.getConnection();
		RosterUtil.report(conn);
	}
	
	@Test
	public void calc()
	{
		RosterUtil.report((int)Math.floor((2/(float)3)*8));
		
	}

	//@Test
	public void DTBSRolloutAsisDao() {
		Connection conn = RosterUtil.getConnection();
		DTBSRolloutAsisDao dao = new OracleDTBSRolloutAsisDaoImpl();
		try {
			java.sql.Date date = dao.getLastRolloutStartDateFromRegion(conn, "SOUTH");
			GregorianCalendar gcalStartDate = new GregorianCalendar();
			gcalStartDate.setTime(date);
			RosterUtil.report(gcalStartDate.getTime());
			gcalStartDate.add(Calendar.DATE, -25);
			RosterUtil.report(gcalStartDate.getTime());
			RosterUtil.report(gcalStartDate.get(Calendar.DAY_OF_WEEK)-1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void TestgetLocations() {
		RolloutService service = new RolloutService();
		try {
			for (String tmp : service.getLocations("SOUTH")) {
				RosterUtil.report(tmp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void TestgetRolloutid() {
		Connection conn = RosterUtil.getConnection();
		DTBSRolloutAsisDao dao = new OracleDTBSRolloutAsisDaoImpl();
		try {
			int result = dao.getLatestRolloutIdByRegion(conn, "SOUTH");
			RosterUtil.report(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void TestAvailSlots() {
		Connection conn = RosterUtil.getConnection();
		DTBSRolloutAsisDao dao = new OracleDTBSRolloutAsisDaoImpl();
		try {
			List<RosterVisual> rosters = dao.getRosterVisuals(conn, "SOUTH", "92", 1);
			for (RosterVisual roster : rosters) {
				RosterUtil.report(roster);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void TestInsertTunning() {
		Connection conn = RosterUtil.getConnection();
		String sql = "insert into DTBS.DTBS_TEST_ROSTER (  ROSTER_SLOT_ID,  LOCATION_ID,  DTBS_TEST_RESOURCE_ID,  TEST_ROSTER_STATUS_CODE ) values ( 86  , ?   , 1 , 'Testing')";
		long start = System.currentTimeMillis();

		String stringCommand = "SELECT ROSTER_SLOT_ID FROM DTBS.ROSTER_SLOTS WHERE LOCATION_ID = 86 ";
		stringCommand += " AND TO_CHAR(SLOT, 'dd/mm/yyyy HH24:MI') = '20/07/2017 08:15'  AND DTBS_ROSTER_ID = 1";

		try {
			conn.setAutoCommit(false);
			Statement st = conn.createStatement();
			int j = 0;
			ResultSet rs = null;
			while (j < 50) {
				rs = st.executeQuery(stringCommand);
				if (rs.next()) {
					RosterUtil.report(rs.getInt(1));
				}
			}
			st.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
			RosterUtil.report(System.currentTimeMillis() - start);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			ResultSet rs = obtainRosterSlot(1, "SOUTH", true, conn);
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement(sql);
			int i = 0;
			while (rs.next()) {

			}
			while (i < 5000) {
				ps.setLong(1, i);
				i++;
				ps.addBatch();
				ps.clearParameters();
			}
			ps.executeBatch();
			conn.commit();
			RosterUtil.report(System.currentTimeMillis() - start);
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static ResultSet obtainRosterSlot(int rosterID, String rolloutRegionCode, boolean byRegionCode,
			Connection conn) throws SQLException {
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

}
