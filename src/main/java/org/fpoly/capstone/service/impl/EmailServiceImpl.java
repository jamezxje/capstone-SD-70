package org.fpoly.capstone.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.BillDetail;
import org.fpoly.capstone.repository.BillDetailRepository;
import org.fpoly.capstone.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private BillDetailRepository billDetailRepository;

    @Override
    public void sendEmail(String to, String subject, String htmlContent) {
        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            this.mailSender.send(mimeMessage);
            System.out.println("Email sent");
            System.out.println("Email đã được gửi đến: " + to);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Email Failed");
        }
    }

    @Override
    public String generateHtmlContent(Bill bill) {
        StringBuilder html = new StringBuilder();
        String meThodBill = String.valueOf(bill.getMethod());
        Date shipDate = bill.getShipDate();
        if (meThodBill.equals("TIEN_MAT")) {
            meThodBill = "Tiền mặt";
        } else if (meThodBill.equals("CHUYEN_KHOAN")) {
            meThodBill = "Chuyển khoản";
        }
        LocalDateTime createDate = bill.getCreateDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        String formattedDate = createDate.format(formatter);
        List<BillDetail> billDetails = this.billDetailRepository.findByBillId(bill.getId());
        html.append("<html><head>")
                .append("<style>")
                .append("body { font-family: Arial, sans-serif; font-size: 14px; line-height: 1.6; margin: 0; padding: 0; background-color: #f4f4f4; }")
                .append("h2 { font-size: 24px; color: #2c3e50; text-align: center; margin-top: 20px; margin-bottom: 20px; }")
                .append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }")
                .append("table, th, td { border: 1px solid #ddd; padding: 8px; text-align: center; }")
                .append("th { background-color: #f2f2f2; font-size: 16px; color: #333; }")
                .append("td { font-size: 14px; color: #555; }")
                .append("p { margin: 8px 0; font-size: 14px; }")
                .append(".info-section { display: flex; justify-content: space-between; margin-bottom: 10px; }")
                .append(".info-left, .info-right { width: 48%; }")
                .append(".info-title { font-weight: bold; }")
                .append(".info-title-1 { font-weight: bold; width: 100%; }")
                .append(".total { font-size: 18px; font-weight: bold; margin-top: 20px; }")
                .append("</style>")
                .append("</head><body>")

                .append("<h2 style=\"color: blue\">CAPSTOME</h2>")
                .append("<p style=\"color: black ; font-size: 15px ; text-align: center\">Thông tin hóa đơn</p>")
                .append("<div class='info-section'>")
                .append("<div class='info-left'>")
                .append("<p><span class='info-title'>Mã hóa đơn:</span> <strong>").append(bill.getCode()).append("</strong></p>")
                .append("<p><span class='info-title'>Ngày tạo:</span> <strong>").append(formattedDate).append("</strong></p>")
                .append("</div>")
                .append("<div class='info-right'>")
                .append("<p><span class='info-title'>Từ:</span> <strong>").append("CAPSTONE").append("</strong></p>")
                .append("<p><span class='info-title'>Nhân viên bán hàng:</span> <strong>").append("Bảo").append("</strong></p>")
                .append("</div>")
                .append("</div>")

                .append("<div class='info-section'>")
                .append("<div class='info-left'>")
                .append("<p><span class= 'info-title-1'>Thông tin người mua hàng</span></p>")
                .append("<p><span class='info-title-1'>Khách hàng:</span> <strong>").append(bill.getUser().getFullName() != null ? bill.getUser().getFullName() : "Khách lẻ").append("</strong></p>")
                .append("<p><span class='info-title-1'>Số điện thoại:</span> <strong>").append(bill.getUser().getPhoneNumber() != null ? bill.getUser().getPhoneNumber() : "").append("</strong></p>")
                .append(shipDate != null ? "<p><span class='info-title'>Thông tin người nhận hàng</span></p>"
                        + "<p><span class='info-title'>Họ tên:</span> <strong>" + (bill.getUserName() != null ? bill.getUserName() : "Khách lẻ") + "</strong></p>"
                        + "<p><span class='info-title'>Số điện thoại người nhận:</span> <strong>" + (bill.getPhoneNumber() != null ? bill.getPhoneNumber() : "") + "</strong></p>"
                        + "<p><span class='info-title'>Địa chỉ:</span> <strong>" + bill.getAddress() + "</strong></p>"
                        : "")
                .append("</div>")
                .append("<div class='info-right'>")
                .append("<p><span class='info-title'>Phương thức thanh toán:</span> <strong>").append(meThodBill).append("</strong></p>")
                .append("</div>")
                .append("</div>")

                // Bảng sản phẩm
                .append("<h3>Sản phẩm đã mua:</h3>")
                .append("<table><tr><th>STT</th><th>Tên sản phẩm</th><th>Số lượng</th><th>Đơn giá</th><th>Tổng tiền</th></tr>");

        BigDecimal totalAmount = BigDecimal.ZERO;
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        int stt = 1;
        for (BillDetail detail : billDetails) {
            BigDecimal productTotal = detail.getPrice().multiply(new BigDecimal(detail.getQuantity()));
            html.append("<tr><td>").append(stt++).append("</td><td>")
                    .append(detail.getProductDetail() != null && detail.getProductDetail().getProduct() != null ?
                            detail.getProductDetail().getProduct().getName() : "Sản phẩm không có tên")
                    .append("</td>")
                    .append("<td>").append(detail.getQuantity()).append("</td>")
                    .append("<td>").append(currencyFormat.format(detail.getPrice())).append("</td>")
                    .append("<td>").append(currencyFormat.format(productTotal)).append("</td></tr>");
            totalAmount = totalAmount.add(productTotal);
        }
        html.append("</table>")

                .append("<p class='total'>Tổng tiền: <strong>").append(currencyFormat.format(totalAmount)).append("</strong></p>")
                .append("<p class='total'>Giảm giá: <strong>").append(currencyFormat.format(bill.getItemDiscount())).append("</strong></p>")
                .append("<p class='total'>Phí giao hàng: <strong>").append(currencyFormat.format(bill.getMoneyShip())).append("</strong></p>")
                .append("<p class='total'>Tổng tiền thanh toán: <strong>").append(currencyFormat.format(bill.getTotalMoney().add(bill.getMoneyShip()))).append("</strong></p>")

                .append("</body></html>");

        return html.toString();
    }


    @Value("${spring.mail.username}")
    private String sender; // Đổi thành email của bạn

    public void sendEmailPassword(String to, String subject, String password) {
        try {
            MimeMessage message = this.mailSender.createMimeMessage();
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
            helper.setFrom(this.sender);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            this.mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String generateHtmlContentBillForOnlineUser(Bill bill) {
        StringBuilder html = new StringBuilder();
        String meThodBill = String.valueOf(bill.getMethod());
        Date shipDate = bill.getShipDate();
        if (meThodBill.equals("TIEN_MAT")) {
            meThodBill = "Tiền mặt";
        } else if (meThodBill.equals("CHUYEN_KHOAN")) {
            meThodBill = "Chuyển khoản";
        }
        List<BillDetail> billDetails = this.billDetailRepository.findByBillId(bill.getId());
        html.append("<html><head>")
                .append("<style>")
                .append("body { font-family: Arial, sans-serif; font-size: 14px; line-height: 1.6; margin: 0; padding: 0; background-color: #f4f4f4; }")
                .append("h2 { font-size: 24px; color: #2c3e50; text-align: center; margin-top: 20px; margin-bottom: 20px; }")
                .append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }")
                .append("table, th, td { border: 1px solid #ddd; padding: 8px; text-align: center; }")
                .append("th { background-color: #f2f2f2; font-size: 16px; color: #333; }")
                .append("td { font-size: 14px; color: #555; }")
                .append("p { margin: 8px 0; font-size: 14px; }")
                .append(".info-section { display: flex; justify-content: space-between; margin-bottom: 10px; }")
                .append(".info-left, .info-right { width: 48%; }")
                .append(".info-title { font-weight: bold; }")
                .append(".info-title-1 { font-weight: bold; width: 100%; }")
                .append(".total { font-size: 18px; font-weight: bold; margin-top: 20px; }")
                .append("</style>")
                .append("</head><body>")

                .append("<h2 style=\"color: blue\">CAPSTOME</h2>")
                .append("<p style=\"color: black ; font-size: 15px ; text-align: center\">Thông tin hóa đơn</p>")
                .append("<div class='info-section'>")
                .append("<div class='info-left'>")
                .append("<p><span class='info-title'>Mã hóa đơn:</span> <strong>").append(bill.getCode()).append("</strong></p>")
                .append("<p><span class='info-title'>Ngày tạo:</span> <strong>").append(bill.getCreateDate()).append("</strong></p>")
                .append("</div>")
                .append("<div class='info-right'>")
                .append("<p><span class='info-title'>Từ:</span> <strong>").append("CAPSTONE").append("</strong></p>")
                .append("<p><span class='info-title'>Nhân viên bán hàng:</span> <strong>").append("DuyTX").append("</strong></p>")
                .append("</div>")
                .append("</div>")

                .append("<div class='info-section'>")
                .append("<div class='info-left'>")
                .append("<p><span class= 'info-title-1'>Thông tin người mua hàng</span></p>")
                .append("<p><span class='info-title-1'>Khách hàng:</span> <strong>").append(bill.getUser().getFullName() != null ? bill.getUser().getFullName() : "Khách lẻ").append("</strong></p>")
                .append("<p><span class='info-title-1'>Số điện thoại:</span> <strong>").append(bill.getUser().getPhoneNumber() != null ? bill.getUser().getPhoneNumber() : "").append("</strong></p>")
                .append(shipDate != null ? "<p><span class='info-title'>Thông tin người nhận hàng</span></p>"
                        + "<p><span class='info-title'>Họ tên:</span> <strong>" + (bill.getUserName() != null ? bill.getUserName() : "Khách lẻ") + "</strong></p>"
                        + "<p><span class='info-title'>Số điện thoại người nhận:</span> <strong>" + (bill.getPhoneNumber() != null ? bill.getPhoneNumber() : "") + "</strong></p>"
                        + "<p><span class='info-title'>Địa chỉ:</span> <strong>" + bill.getAddress() + "</strong></p>"
                        : "")
                .append("</div>")
                .append("<div class='info-right'>")
                .append("<p><span class='info-title'>Phương thức thanh toán:</span> <strong>").append(meThodBill).append("</strong></p>")
                .append("</div>")
                .append("</div>")

                // Bảng sản phẩm
                .append("<h3>Sản phẩm đã mua:</h3>")
                .append("<table><tr><th>STT</th><th>Tên sản phẩm</th><th>Số lượng</th><th>Đơn giá</th><th>Tổng tiền</th></tr>");

        BigDecimal totalAmount = BigDecimal.ZERO;
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        int stt = 1;
        for (BillDetail detail : billDetails) {
            BigDecimal productTotal = detail.getPrice().multiply(new BigDecimal(detail.getQuantity()));
            html.append("<tr><td>").append(stt++).append("</td><td>")
                    .append(detail.getProductDetail() != null && detail.getProductDetail().getProduct() != null ?
                            detail.getProductDetail().getProduct().getName() : "Sản phẩm không có tên")
                    .append("</td>")
                    .append("<td>").append(detail.getQuantity()).append("</td>")
                    .append("<td>").append(currencyFormat.format(detail.getPrice())).append("</td>")
                    .append("<td>").append(currencyFormat.format(productTotal)).append("</td></tr>");
            totalAmount = totalAmount.add(productTotal);
        }
        html.append("</table>")

                .append("<p class='total'>Tổng tiền: <strong>").append(currencyFormat.format(totalAmount)).append("</strong></p>")
                .append("<p class='total'>Phí giao hàng: <strong>").append(currencyFormat.format(bill.getMoneyShip())).append("</strong></p>")
                .append("<p class='total'>Tổng tiền thanh toán: <strong>").append(currencyFormat.format(bill.getTotalMoney())).append("</strong></p>")

                .append("</body></html>");

        return html.toString();
    }
}
