package com.eclipse.DTBSManager.controller;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eclipse.DTBSManager.bean.RolloutQuery;
import com.eclipse.DTBSManager.bean.RolloutResult;
import com.eclipse.DTBSManager.service.RolloutService;
import com.eclipse.DTBSManager.util.RosterUtil;
/**
 * 
 * @author Leon Wang	
 * @email seasparta618@gmail.com || dev.leon618@gmail.com
 */
@Controller
public class RolloutController {

	private RolloutService service = new RolloutService();

	@GetMapping("/")
	public String greetingForm(Model model) {
		model.addAttribute("query", new RolloutQuery());
		return "index";
	}



	@RequestMapping("/rollout")
	public String rollout(@ModelAttribute RolloutQuery query, Model model, HttpServletRequest request) {
		System.out.print(query);

		java.sql.Date date = null;
		try {
			date = RosterUtil.fromSqlString2SqlDate(query.getStartDate());
		} catch (ParseException e) {
			try {
				date = RosterUtil.fromSqlString2SqlDate("01/01/2020");
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

		RolloutResult result = null;
		try {
			result=service.createRollout(query.getDays(), query.getRegion(), date, query.getRolloutStatus(),
					query.getUser());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		request.setAttribute("result", result);
		request.setAttribute("mode", "SHOW_RESULT");

		return "index";
	}

}
