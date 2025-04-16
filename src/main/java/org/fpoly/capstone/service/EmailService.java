package org.fpoly.capstone.service;

import jakarta.mail.MessagingException;
import org.fpoly.capstone.entity.Bill;

public interface EmailService {
    void sendEmail(String to, String subject, String htmlContent) throws MessagingException;

    String generateHtmlContent(Bill bill);

    String generateHtmlContentBillForOnlineUser(Bill bill);
}
