package com.dcode7.iwell.utils.mail;


import com.dcode7.iwell.common.service.LocalService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService{

    private final JavaMailSender mailSender;

    private final LocalService localService;

    private final TemplateEngine templateEngine;

    /**
     * Sends an account created email to the specified recipient.
     *
     * @param to       Email recipient
     * @param fullname Full name of the user
     * @param userType Type of user (e.g., admin, customer)
     * @throw MessagingException if an error occurs during email sending
     */
    public void sendAccountCreatedEmail(String to, String fullname, String userType) {
        try{
            Context context = new Context();
            context.setVariable("fullname", fullname);
            context.setVariable("userType", userType);
            String htmlContent = templateEngine.process("account_created", context);
            sendHtmlMessage(to, "Account Created", htmlContent);
            context.clearVariables();
        }catch (MessagingException e){
            e.printStackTrace();
            throw new RuntimeException(localService.getMessage("mail.failed", HttpStatus.BAD_GATEWAY));
        }
    }


    /**
     * Sends an HTML email using JavaMailSender.
     *
     * @param to        Email recipient
     * @param subject   Email subject
     * @param htmlBody  HTML content of the email
     * @throws MessagingException if an error occurs during email sending
     */
    private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        mailSender.send(message);
    }
}