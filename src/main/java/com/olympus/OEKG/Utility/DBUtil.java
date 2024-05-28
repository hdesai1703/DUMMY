package com.olympus.OEKG.Utility;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.olympus.OEKG.model.AppUtils;

public class DBUtil {
	

	private static final String DB_DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	
	public static String DRIVER_CLASS_CT;
	public static String SERVER_PATH_CT;
	public static String SERVER_USER_CT;
	public static String SERVER_PASSWORD_CT;
	
	public static String DRIVER_CLASS_AD;
	public static String SERVER_PATH_AD;
	public static String SERVER_USER_AD;
	public static String SERVER_PASSWORD_AD;
	
	public static String DRIVER_CLASS_CP;
	public static String SERVER_PATH_CP;
	public static String SERVER_USER_CP;
	public static String SERVER_PASSWORD_CP;

	private static DBUtil dbUtilObj = null;

	private DBUtil() {
	}

	public static DBUtil getDbUtilObj() {
		if (dbUtilObj == null) {
			dbUtilObj = new DBUtil();
		}
		return dbUtilObj;
	}

	public static ComboPooledDataSource dataSource;
	public static ComboPooledDataSource dataSourceClient;
	public static ComboPooledDataSource dataSourceClientAddRef;
	public static ComboPooledDataSource dataSourceClientComp;

	static {
		final Logger LOGGER =LoggerFactory.getLogger(DBUtil.class);
		System.setProperty("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", "WARNING");
		System.setProperty("com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");
		
		dataSource = new ComboPooledDataSource();
		dataSourceClient = new ComboPooledDataSource();
		dataSourceClientAddRef = new ComboPooledDataSource();
		dataSourceClientComp = new ComboPooledDataSource();
		try {
			dataSource.setDriverClass(DB_DRIVER_CLASS);
			dataSource.setJdbcUrl(AppUtils.properties
					.getProperty("spring.datasource.url"));
			dataSource.setUser(AppUtils.properties
					.getProperty("spring.datasource.username"));
			dataSource.setPassword(AppUtils.properties
					.getProperty("spring.datasource.password"));

			dataSource.setMinPoolSize(3);
			dataSource.setMaxPoolSize(500);
			dataSource.setAcquireIncrement(4);
			dataSource.setTestConnectionOnCheckin(true);
			dataSource.setIdleConnectionTestPeriod(300);
			dataSource.setMaxIdleTimeExcessConnections(240);
			
			
		} catch (Exception e) {
			utility.exceptionLog("BDUtil", "static", "", LOGGER, null, "", e.getMessage());

		}
	}

	public static DataSource getDataSource() {
		return dataSource;
	}

	public static DataSource getDataSourceClient() throws Exception {
			dataSourceClient.setDriverClass(DRIVER_CLASS_CT);
			dataSourceClient.setJdbcUrl(SERVER_PATH_CT);
			dataSourceClient.setUser(SERVER_USER_CT);
			dataSourceClient.setPassword(SERVER_PASSWORD_CT);
			
			dataSourceClient.setMinPoolSize(3);
			dataSourceClient.setMaxPoolSize(500);
			dataSourceClient.setAcquireIncrement(4);
			dataSourceClient.setTestConnectionOnCheckin(true);
			dataSourceClient.setIdleConnectionTestPeriod(300);
			dataSourceClient.setMaxIdleTimeExcessConnections(240);
			
			
				
		return dataSourceClient;
	}
	
	public static DataSource getDataSourceClientAddRef() throws Exception {
		dataSourceClientAddRef.setDriverClass(DRIVER_CLASS_AD);
		dataSourceClientAddRef.setJdbcUrl(SERVER_PATH_AD);
		dataSourceClientAddRef.setUser(SERVER_USER_AD);
		dataSourceClientAddRef.setPassword(SERVER_PASSWORD_AD);
		
		dataSourceClientAddRef.setMinPoolSize(3);
		dataSourceClientAddRef.setMaxPoolSize(500);
		dataSourceClientAddRef.setAcquireIncrement(4);
		dataSourceClientAddRef.setTestConnectionOnCheckin(true);
		dataSourceClientAddRef.setIdleConnectionTestPeriod(300);
		dataSourceClientAddRef.setMaxIdleTimeExcessConnections(240);
		
			
		return dataSourceClientAddRef;
	}
	
	public static DataSource getDataSourceClientComp() throws Exception {
		dataSourceClientComp.setDriverClass(DRIVER_CLASS_CP);
		dataSourceClientComp.setJdbcUrl(SERVER_PATH_CP);
		dataSourceClientComp.setUser(SERVER_USER_CP);
		dataSourceClientComp.setPassword(SERVER_PASSWORD_CP);
		
		dataSourceClientComp.setMinPoolSize(3);
		dataSourceClientComp.setMaxPoolSize(500);
		dataSourceClientComp.setAcquireIncrement(4);
		dataSourceClientComp.setTestConnectionOnCheckin(true);
		dataSourceClientComp.setIdleConnectionTestPeriod(300);
		dataSourceClientComp.setMaxIdleTimeExcessConnections(240);
		
			
		return dataSourceClientComp;
	}
}