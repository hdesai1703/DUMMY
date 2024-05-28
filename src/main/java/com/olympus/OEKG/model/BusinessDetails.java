package com.olympus.OEKG.model;

import lombok.Data;

@Data
public class BusinessDetails {
	private Integer BU_ID;
	private String BU_NAME;
	
private static BusinessDetails buobj = null;
	
	private BusinessDetails() {
	}

	public static BusinessDetails buObj() {
		if (buobj == null) {
			buobj = new BusinessDetails();
		}
		return buobj;
	}

}
