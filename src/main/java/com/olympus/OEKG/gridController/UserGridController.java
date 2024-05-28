package com.olympus.OEKG.gridController;

import java.sql.SQLException;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.olympus.OEKG.Utility.utility;
import com.olympus.OEKG.repository.UserDAO;
import com.olympus.OEKG.service.CustomerBatchService;

@RestController
@RequestMapping("/user")
public class UserGridController {

	private static utility objUtil = new utility();
	
	@Autowired
	private UserDAO userDAO;

	@Autowired
	private CustomerBatchService custBatchService;

	@GetMapping("/getAllUserData")
	public Map<String, Object> getAllUserData() {
		return userDAO.getalldata();
	}

	@PostMapping("/getSearchUserData")
	public Map<String, Object> getSearchUserData(@RequestParam("USER_NAME") String USER_NAME,
			@RequestParam("START_DATE") String START_DATE, @RequestParam("END_DATE") String END_DATE,
			@RequestParam("IS_ACTIVE") String IS_ACTIVE) {

		return userDAO.getSearchUserData(USER_NAME, START_DATE, END_DATE, IS_ACTIVE);

	}

	@GetMapping("/testBatch")
	public void testBatch() throws SQLException, InterruptedException {
		custBatchService.testBatch();
	}

	@GetMapping("/mari")
	public String mari() {
		return "Marikannan";
	}
	
	@PostMapping("/changepassword")
	public  Map<String, Object> changepassword(@RequestParam("EMAIL_ADDRESS") String EMAIL_ADDRESS, @RequestParam("PASSWORD") String PASSWORD,
			@RequestParam("NEW_PASSWORD") String NEW_PASSWORD) {
		System.out.println("inside chnagepassword");
		return userDAO.changepassword(EMAIL_ADDRESS,PASSWORD,NEW_PASSWORD);
		

	}
	@PostMapping("/sendmail")
	public void sendmail() {
		try {
			objUtil.sendMail(27);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
