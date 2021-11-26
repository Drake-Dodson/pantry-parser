package com.example.pantryparserbackend.Util;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.example.pantryparserbackend.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailUtil {
    private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);

    private static JavaMailSender javaMailSender;

    @Autowired
    public void setJavaMailSender(JavaMailSender sender) {
        javaMailSender = sender;
    }

    public static boolean sendPasswordResetEmail(User user, String otp) {
        String html = "<h3>Dear " + user.getDisplayName() + ",</h3></br>"
                + "<p>Hello! It looks like you are trying to change your password. </p>" + "</br></br>"
                + "<h1>Your OTP is: " + otp + "</h1>" + "</br></br>"
                + "<p> Please return to the app and type in this code! If this wasn't you, please disregard this email </p>";

        return sendHTMLEmail(user.getEmail(), html);
    }

    public static void sendTextEmail() {
        logger.info("Simple Email sending start");

        SimpleMailMessage simpleMessage = new SimpleMailMessage();
        simpleMessage.setTo("pbrink21@live.com");
        simpleMessage.setSubject("Spring Boot=> Sending simple email");
        simpleMessage.setText("Dear Dhirendra, Hope you are doing well.");

        javaMailSender.send(simpleMessage);

        logger.info("Simple Email sent");

    }

    private static boolean sendHTMLEmail(String address, String html) {
        logger.info("HTML email sending start");
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            // Set multipart mime message true
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(address);
            mimeMessageHelper.setSubject("Pantry Parser Password Reset");
            mimeMessageHelper.setText(html, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            logger.error("Exeception=>sendHTMLEmail ", e);
            return false;
        }

        logger.info("HTML email sent");
        return true;
    }
}
