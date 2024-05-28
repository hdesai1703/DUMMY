package com.olympus.OEKG.gridController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.olympus.OEKG.repository.MappingDAO;

@RestController
@RequestMapping("/mapping")
public class MappingGridController {

	@Autowired
	private MappingDAO mappingDAO;

	@GetMapping("/getAllMapingData")
	public Map<String, Object> getAllRoleData() {
		return mappingDAO.getalldata();
	}

	@PostMapping("/getSearchMappingData")
	public Map<String, Object> getSearchRoleData(
			@RequestParam("ROLE_ID") Integer ROLE_ID,
			@RequestParam("USER_NAME") String USER_NAME,
			@RequestParam("IS_ACTIVE") String IS_ACTIVE,
			@RequestParam("START_DATE") String START_DATE,
			@RequestParam("END_DATE") String END_DATE) {
	
		return mappingDAO.getSearchMappingData(3, USER_NAME, IS_ACTIVE,
				START_DATE, END_DATE);
	}
	
	@GetMapping("/getEmailAddressList")
	public Map<String, Object> getEmailAddressList() {
		return mappingDAO.getEmailAddressList();
	}
	
	@PostMapping("/getMappingBUList")
	public Map<String, Object> getMappingBUList(@RequestParam("EMAILADDRESS") String EMAILADDRESS){
		
		return mappingDAO.getMappingBUList(EMAILADDRESS);
	}
	
//	@GetMapping("/testScheduler")
//	public Map<String, Object> testScheduler(){
//		return mappingDAO.testScheduler();
//	}
}
