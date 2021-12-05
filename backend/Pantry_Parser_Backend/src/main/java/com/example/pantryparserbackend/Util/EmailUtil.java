package com.example.pantryparserbackend.Util;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import com.example.pantryparserbackend.Users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * The function that sends the email for password resetting
     * @param user user we are sending to
     * @param otp one time password we are sending
     * @return success or fail
     */
    public static boolean sendPasswordResetEmail(User user, String otp) {
        String html = "<h3>Dear " + user.getDisplayName() + ",</h3></br>"
                + "<p>Hello! It looks like you are trying to change your password. </p>" + "</br></br>"
                + "<h1>Your OTP is: " + otp + "</h1>" + "</br></br>"
                + "<p> Please return to the app and type in this code! If this wasn't you, please disregard this email </p>";

        return sendHTMLEmail(user.getEmail(), "Pantry Parser Password Reset", html);
    }

    /**
     * Sends an email for new users
     * @param user user we are sending to
     * @param otp one time password we are sending
     * @return success or fail
     */
    public static boolean sendRegistrationConfirmationEmail(User user, String otp) {
        String html = "<h3>Hello " + user.getDisplayName() + ",</h3></br>"
                + "<p>Welcome to Pantry Parser! Please verify your email! </p>" + "</br></br>"
                + "<h1>Your OTP is: " + otp + "</h1>" + "</br></br>"
                + "<p> Please return to the app and go to the settings page to find where to input this OTP </p>";

        return sendHTMLEmail(user.getEmail(), "Welcome to Pantry Parser!", html);
    }

    /**
     * The main email sending function
     * @param address address to send the email too
     * @param html html code for the email
     * @return success or fail
     */
    private static boolean sendHTMLEmail(String address, String subject, String html) {
        logger.info("HTML email sending start");
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            // Set multipart mime message true
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(address);
            mimeMessageHelper.setSubject(subject);
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
