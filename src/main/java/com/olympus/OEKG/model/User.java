package com.olympus.OEKG.model;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

	private Integer USER_ID;
	private String EMAIL_ADDRESS;
	private String USER_NAME;
	private String IS_ACTIVE;
	private Date START_DATE;
	private Date END_DATE;
	private Integer CREATED_BY;
	private Date CREATION_DATE;
	private Integer LAST_UPDATED_BY;
	private Date LAST_UPDATE_DATE;
	private String ATTRIBUTE1;
	private String ATTRIBUTE2;
	private String ATTRIBUTE3;
	private String ATTRIBUTE4;
	private String ATTRIBUTE5;
	private String ATTRIBUTE6;
	private String ATTRIBUTE7;
	private String ATTRIBUTE8;
	private String ATTRIBUTE9;
	private String ATTRIBUTE10;
}
