//package org.fpoly.capstone.service.payload.user;
//
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@Service
//public class SendEmailService {
//    private static final Logger logger = LoggerFactory.getLogger(SendEmailService.class);
//
//    @Autowired
//    private JavaMailSender javaMailSender;
//
//    @Value("${spring.mail.username}")
//    private String sender;
//
//    @Async
//    public boolean sendEmailPassword(String to, String subject, String password) {
//        try {
//            MimeMessage message = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
//
//            String htmlBody = "<html>"
//                    + "<head><style>"
//                    + "body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; text-align: center; }"
//                    + ".container { max-width: 600px; margin: auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); }"
//                    + "h1 { color: #007bff; }"
//                    + ".info { font-size: 16px; margin-top: 20px; }"
//                    + ".info label { font-weight: bold; display: block; margin-top: 10px; }"
//                    + ".footer { margin-top: 20px; font-size: 14px; color: #666; }"
//                    + "</style></head>"
//                    + "<body>"
//                    + "<div class='container'>"
//                    + "<h1>CAPTIONE</h1>"
//                    + "<p>Xin chào,</p>"
//                    + "<p>Bạn đã được cấp tài khoản để đăng nhập vào hệ thống.</p>"
//                    + "<div class='info'>"
//                    + "<label>Tài khoản: " + to + "</label>"
//                    + "<label>Mật khẩu: " + password + "</label>"
//                    + "</div>"
//                    + "<p class='footer'>Vui lòng đổi mật khẩu sau khi đăng nhập để đảm bảo an toàn.</p>"
//                    + "</div></body></html>";
//
//            helper.setFrom(sender);
//            helper.setTo(to);
//            helper.setSubject(subject);
//            helper.setText(htmlBody, true);
//
//            javaMailSender.send(message);
//            logger.info("✅ Email gửi thành công đến: {}", to);
//            return true;
//        } catch (MessagingException e) {
//            logger.error("❌ Gửi email thất bại đến: {}, lỗi: {}", to, e.getMessage());
//            return false;
//        }
//    }
//}
