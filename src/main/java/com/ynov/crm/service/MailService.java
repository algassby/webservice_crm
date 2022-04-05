package com.ynov.crm.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;
    
    @Async("threadPoolTaskExecutor")
    public void sendEmail(String subject,String text, String mail) {
    	try {
    		 Thread.sleep(2000);
    		SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(mail);
            msg.setSubject(subject);
            msg.setText(text);
            javaMailSender.send(msg);
           
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        

    }

}
