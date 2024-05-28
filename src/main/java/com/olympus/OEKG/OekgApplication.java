package com.olympus.OEKG;

import java.time.ZoneId;
import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableEurekaClient
public class OekgApplication {
	
	 
	public static void main(String[] args) {
		ZoneId zoneId = ZoneId.of("GMT+9");
	    TimeZone zone = TimeZone.getTimeZone(zoneId);
	    TimeZone.setDefault(zone);
		SpringApplication.run(OekgApplication.class, args);
	}
}
