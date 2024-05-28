package com.olympus.OEKG.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.olympus.OEKG.repository.BatchLogDAO;
import com.olympus.OEKG.service.CustomerBatchService;


@Controller
@RequestMapping("/batchlog")
public class BatchLogController {
	
	@Autowired
	private CustomerBatchService blService;

	@Autowired
	private BatchLogDAO blDAO;

	@RequestMapping("/batchLog")
	public String menuPage() {
		return "batchLog";
	}
	
	
	
	
}
