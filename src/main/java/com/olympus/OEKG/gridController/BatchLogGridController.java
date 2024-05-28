package com.olympus.OEKG.gridController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.olympus.OEKG.repository.BatchLogDAO;
import com.olympus.OEKG.service.CustomerBatchService;

@RestController
@RequestMapping("/batchlog")
public class BatchLogGridController {

	@Autowired
	private CustomerBatchService blService;

	@Autowired
	private BatchLogDAO batchLogDAO;

	@Autowired
	private BatchLogDAO blDao;

	@GetMapping("/getBLData")
	public Map<String, Object> getalldata() {
		return blDao.getalldata();
	}

	@PostMapping("/getSearchBLData")
	public Map<String, Object> getSearchBLData(
			@RequestParam("REQ_MODULE") String INTERFACE_NAME,
			@RequestParam("REQ_FOR") String INTERFACE_TYPE,
			@RequestParam("STATUS") String STATUS,
			@RequestParam("START_DATE") String START_DATE,
			@RequestParam("END_DATE") String END_DATE) {

		return batchLogDAO.getSearchBLData(INTERFACE_NAME, INTERFACE_TYPE,
				STATUS, START_DATE, END_DATE);

	}
	
	@GetMapping("/getInterfaceList")
	public Map<String, Object> getInterfaceList(){
		return blDao.getInterfaceList();
	}

	@GetMapping("/getLogList")
	public Map<String, Object> getLogList() {
		return blDao.getLogList();
	}

	@GetMapping("/getInterfaceTypeList")
	public Map<String, Object> getInterfaceTypeList() {
		return blDao.getInterfaceTypeList();
	}

}
