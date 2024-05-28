package com.olympus.OEKG.controller;

import java.text.ParseException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;




import com.olympus.OEKG.Utility.utility;
import com.olympus.OEKG.repository.UserDAO;

@Controller
@RequestMapping("/user")
public class UserController {
	
    

	
	@Value("${app.baseurl}")
	private String baseUrl;

	@Autowired
	private UserDAO userDAO;

//	@RequestMapping("/OEKG")
//	public String loginPage() {
//		return "login";
//	}
//	
	@RequestMapping("/oekg-feign")
	public String oekgFeign() {
		return "redirect:/user";
	}
	
	@RequestMapping("/user")
	public String userPage() {
		return "user";
	}

	@PostMapping("/updateUser")
	public String updateUser(Integer USER_ID, String USER_NAME,String USER_TYPE,String PASSWORD,
			String EMAIL_ADDRESS, String IS_ACTIVE, String START_DATE,
			String END_DATE,Integer CREATED_BY) throws ParseException {


		boolean isValidEndDate = false;
		String datePattern = "\\d{1,2}-\\d{1,2}-\\d{4}";
		isValidEndDate = END_DATE.matches(datePattern);		

		if (!isValidEndDate) {
			userDAO.updateUser(utility.update, USER_ID, USER_NAME,USER_TYPE,PASSWORD,
					EMAIL_ADDRESS, IS_ACTIVE, START_DATE, null, CREATED_BY);
		} else {
			userDAO.updateUser(utility.update, USER_ID, USER_NAME,USER_TYPE,PASSWORD,
					EMAIL_ADDRESS, IS_ACTIVE, START_DATE, END_DATE,CREATED_BY);
		}

		return "redirect:"+baseUrl+"/oekg/user/user";
	}

	@PostMapping("/addUser")
	public String addUser(Integer USER_ID, String USER_NAME,String USER_TYPE,String PASSWORD,
			String EMAIL_ADDRESS, String IS_ACTIVE, String START_DATE,
			String END_DATE,Integer CREATED_BY) throws ParseException {

		if (END_DATE == "" || END_DATE == null) {
			userDAO.updateUser(utility.insert, 0, USER_NAME,USER_TYPE,PASSWORD, EMAIL_ADDRESS,
					IS_ACTIVE, START_DATE, null,CREATED_BY);
		} else {
			userDAO.updateUser(utility.insert, 0, USER_NAME,USER_TYPE,PASSWORD, EMAIL_ADDRESS,
					IS_ACTIVE, START_DATE, END_DATE,CREATED_BY);
		}

		return "redirect:"+baseUrl+"/oekg/user/user";
	}	
}
