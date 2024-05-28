package com.olympus.OEKG.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.olympus.OEKG.Utility.utility;
import com.olympus.OEKG.repository.MappingDAO;


@Controller
@RequestMapping("/mapping")
public class MappingController {
	@Value("${app.baseurl}")
	private String baseUrl;
	
	@Autowired
	private MappingDAO mappingDAO;

	@RequestMapping("/mapping")
	public String mappingPage() {
		return "mapping";
	}

	@PostMapping("/updateMapping")
	public String updateMapping(Integer ROLE_USER_ID, Integer ROLE_ID,
			Integer USER_ID, String IS_ACTIVE, String START_DATE,
			String END_DATE, Integer BUSINESS_UNIT_SITE_ID,Integer CREATED_BY)
			throws ParseException {

		boolean isValidEndDate = false;
		String datePattern = "\\d{1,2}-\\d{1,2}-\\d{4}";
		isValidEndDate = END_DATE.matches(datePattern);
		
		if (!isValidEndDate) {
			mappingDAO.updateMapping(utility.update, ROLE_USER_ID, 3,
					USER_ID, IS_ACTIVE, START_DATE, null,
					BUSINESS_UNIT_SITE_ID, CREATED_BY);
		} else {
			mappingDAO.updateMapping(utility.update, ROLE_USER_ID, 3,
					USER_ID, IS_ACTIVE, START_DATE, END_DATE,
					BUSINESS_UNIT_SITE_ID, CREATED_BY);
		}

		return "redirect:"+baseUrl+"/oekg/mapping/mapping";
	}

	@PostMapping("/addMapping")
	public String addMapping(Integer ROLE_ID, Integer USER_ID,
			String IS_ACTIVE, String START_DATE, String END_DATE,
			Integer BUSINESS_UNIT_SITE_ID,Integer CREATED_BY) throws ParseException {

		if (END_DATE == "" || END_DATE == null) {
			mappingDAO.updateMapping(utility.insert, 0, 3, USER_ID,
					IS_ACTIVE, START_DATE, null, BUSINESS_UNIT_SITE_ID, CREATED_BY);
		} else {
			mappingDAO.updateMapping(utility.insert, 0, 3, USER_ID,
					IS_ACTIVE, START_DATE, END_DATE, BUSINESS_UNIT_SITE_ID, CREATED_BY);
		}

		return "redirect:"+baseUrl+"/oekg/mapping/mapping";
	}
	
}
