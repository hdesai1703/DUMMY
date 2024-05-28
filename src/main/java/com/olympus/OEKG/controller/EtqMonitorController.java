package com.olympus.OEKG.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/etqMonitor")
public class EtqMonitorController {
	
	@RequestMapping("/etqMonitor")
	public String etqMonitorPage(){
		return "etqMonitor";
	}

}
