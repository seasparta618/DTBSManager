package com.eclipse.DTBSManager.controller;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eclipse.DTBSManager.bean.RolloutQuery;
import com.eclipse.DTBSManager.bean.RolloutResult;
import com.eclipse.DTBSManager.bean.RosterVisualJson;
import com.eclipse.DTBSManager.service.RolloutService;
import com.eclipse.DTBSManager.util.RosterUtil;
/**
 * 
 * @author Leon Wang	
 * @email seasparta618@gmail.com || dev.leon618@gmail.com
 */
@RestController
public class RolloutAsisController {

	private RolloutService service = new RolloutService();
	double result = 0;

	@GetMapping("/getLocation")
	public String getLocationByRegionCode(@RequestParam(value = "regionCode", defaultValue = "ALL") String regionCode) {

		String[] result = null;
		try {
			result = service.getLocations(regionCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Arrays.toString(result);
	}

	@GetMapping("/greeting")
	public String greetingForm() {
		return "hello world";
	}

	@GetMapping("/getSlotsVisualJson")
	public List<RosterVisualJson> getSlotsVisualJson(
			@RequestParam(value = "regionCode", defaultValue = "SOUTH") String regionCode,
			@RequestParam(value = "locationId", defaultValue = "86") String locationId) {
		List<RosterVisualJson> rosters = null;
		try {
			java.util.Date date= service.getLastRolloutDate(regionCode);
			rosters = RosterUtil.convertRosterVisualFormat4Json(service.getRosterVisuals(regionCode, locationId,1),date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rosters;
	}

	@GetMapping("/getProcessing")
	public String getRolloutProcessingMethod() {
		double result = RosterUtil.getProcessingPercentage();
		return RosterUtil.getPercentage(100, (int) result);
	}

	@GetMapping("/lastRolloutDate")
	public String lastRolloutDateMethod(@RequestParam(value = "regionCode", defaultValue = "ALL") String regionCode) {
		String lastRolloutDate = "no data";
		try {
			lastRolloutDate = RosterUtil.fromUtilDate2FormatString(service.getLastRolloutDate(regionCode),
					"dd/MM/yyyy");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lastRolloutDate;
	}

	@GetMapping("/nextRolloutDate")
	public String nextRolloutDateMethod(@RequestParam(value = "regionCode", defaultValue = "ALL") String regionCode) {
		String lastRolloutDate = "no data";
		try {
			lastRolloutDate = RosterUtil.fromUtilDate2FormatString(service.getNextRolloutDate(regionCode),
					"dd/MM/yyyy");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lastRolloutDate + " for " + regionCode;
	}

	@GetMapping("/startRolloutDate")
	public String getStartRolloutDateMethod(
			@RequestParam(value = "regionCode", defaultValue = "ALL") String regionCode) {
		String lastRolloutDate = "no data";
		try {
			lastRolloutDate = RosterUtil.fromUtilDate2FormatString(service.getStartRolloutDate(regionCode),
					"dd/MM/yyyy");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lastRolloutDate;
	}

	@GetMapping("/cancelBooking")
	public String cancelBookingMethod(@RequestParam(value = "regionCode", defaultValue = "ALL") String regionCode) {
		String result = "no data";
		int count = 0;
		try {
			count = service.cancelMonthBooking(regionCode);
			if (count == 0) {
				result = "There is not relative booking in database. for " + regionCode;
			} else {
				result = "Delete " + count + " for " + regionCode;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@GetMapping("/checkAvailableSlotsNum")
	public String checkAvailableSlotsNumMethod(
			@RequestParam(value = "regionCode", defaultValue = "ALL") String regionCode) {
		String result = "no data";
		int count = 0;
		try {
			count = service.checkAvailableSlotsNum(regionCode);
			if (count == 0) {
				result = "There is not available slot." + regionCode;
			} else {
				result = "AvailableSlots:  " + count + " for " + regionCode;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@GetMapping("/acquireSlotsNum")
	public String acquirekSlotsNumMethod(@RequestParam(value = "regionCode", defaultValue = "ALL") String regionCode) {
		String result = "no data";
		int count = 0;
		try {
			count = service.checkTotalSlotsNum(regionCode);
			result = count + "";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@GetMapping("/checkAvailableSlotsPer")
	public String checkAvailableSlotsPercentMethod(
			@RequestParam(value = "regionCode", defaultValue = "ALL") String regionCode) {
		String result = "no data";
		try {
			result = service.checkAvailableSlotsPercent(regionCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("/test")
	public String rollout(@ModelAttribute RolloutQuery query, Model model, HttpServletRequest request) {
		System.out.print(query);

		java.sql.Date date = null;
		RolloutResult result = null;
		try {
			date = RosterUtil.fromSqlString2SqlDate(query.getStartDate());
			result = service.createRollout(query.getDays(), query.getRegion(), date, query.getRolloutStatus(),
					query.getUser());
		} catch (ParseException e) {
			try {
				date = RosterUtil.fromSqlString2SqlDate("01/01/2020");
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		request.setAttribute("result", result);
		request.setAttribute("mode", "SHOW_RESULT");

		return "index";
	}
}
