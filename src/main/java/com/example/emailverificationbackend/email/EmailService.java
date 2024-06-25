package com.example.emailverificationbackend.email;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender{
    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);


    @Override
    @Async
    public void send(String to, String email) {
        final String username = "abhaygupta3212@gmail.com";
        final String password = "xmkb dahy ogrb xgpy";
        Properties props = new Properties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator(){
                    protected PasswordAuthentication getPasswordAuthentication(){
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {

            MimeMessage helper = new MimeMessage(session);

            helper.setText(email);
            helper.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            helper.setSubject("Confirm your email");
            helper.setFrom(new InternetAddress("noreply@google.com"));
            Transport.send(helper);
        } catch (javax.mail.MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
