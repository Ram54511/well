package com.dcode7.iwell.utils.mail;

import jakarta.mail.MessagingException;

public interface MailService {
     void sendAccountCreatedEmail(String to, String fullname, String userType);
}