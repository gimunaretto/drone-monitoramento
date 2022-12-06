package com.integration.agromonitor.kafkaproducer.service.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
public class EmailController {

    private EmailCfg emailCfg;
    public EmailController(EmailCfg emailCfg){
        this.emailCfg = emailCfg;
    }

    public void sendEmail(String mensagem){

        // Create a mail sender
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.emailCfg.getHost());
        mailSender.setPort(this.emailCfg.getPort());
        mailSender.setUsername(this.emailCfg.getUsername());
        mailSender.setPassword(this.emailCfg.getPassword());
        // Create an email instance
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("wendy.monitoria@hotmail.com");
        mailMessage.setTo("jrosivan@gmail.com");
        mailMessage.setSubject("mail");
        mailMessage.setText(mensagem);

        // Send mail
        mailSender.send(mailMessage);

    }

}
