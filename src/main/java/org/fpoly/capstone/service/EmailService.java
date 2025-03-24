package org.fpoly.capstone.service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender; // Đổi thành email của bạn

    public void sendEmailPassword(String to, String subject, String password) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            String htmlBody = "<html>"
                    + "<head>"
                    + "<style>"
                    + "@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;700&display=swap');"
                    + "body { font-family: 'Roboto', sans-serif; background-color: #ecf0f1; padding: 20px; }"
                    + ".container { max-width: 600px; margin: auto; background: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0px 4px 6px rgba(0,0,0,0.1); }"
                    + ".header { text-align: center; padding-bottom: 10px; border-bottom: 2px solid #E67E22; }"
                    + ".header h1 { color: #2C3E50; }"
                    + ".content { padding: 20px; text-align: center; }"
                    + ".info { background: #E67E22; color: white; padding: 10px; border-radius: 5px; font-size: 18px; margin-bottom: 10px; }"
                    + ".button { display: inline-block; background: #2C3E50; color: #ffffff; padding: 10px 20px; border-radius: 5px; text-decoration: none; font-size: 16px; margin-top: 10px; }"
                    + ".footer { text-align: center; font-size: 12px; color: #7f8c8d; margin-top: 20px; }"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "<div class='container'>"
                    + "<div class='header'>"
                    + "<h1>CAPSTONE</h1>"
                    + "</div>"
                    + "<div class='content'>"
                    + "<p>Chào mừng bạn đến với <strong>CAPSTONE</strong>! Dưới đây là thông tin tài khoản của bạn:</p>"
                    + "<div class='info'>Email đăng nhập: " + to + "</div>"
                    + "<div class='info'>Mật khẩu: " + password + "</div>"
                    + "</div>"
                    + "<div class='footer'>"
                    + "Nếu bạn không đăng ký tài khoản, vui lòng bỏ qua email này."
                    + "</div>"
                    + "</div>"
                    + "</body>"
                    + "</html>";
            helper.setFrom(sender);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
