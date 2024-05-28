package com.olympus.OEKG.Utility;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class SmtpMailSender {
    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail() throws MessagingException {
    	String body = "Dear Administrator,\n\nBatch ID:"+3011+" has resulted in an Upload Error.\nPlease refer to EtQ Monitoring Upload Message for details.\n\nThanks,\nEMA Admin";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        helper = new MimeMessageHelper(message, true);//true indicates multipart message

        helper.setFrom(from);// <--- THIS IS IMPORTANT

        helper.setSubject("EtQ Monitoring Notification: Dev Instance");
        helper.setTo("mariinbumurugan27@gmail.com");
        helper.setText(body, true);//true indicates body is html
        javaMailSender.send(message);
    }
}
