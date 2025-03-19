package org.fpoly.capstone.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.BillDetail;
import org.fpoly.capstone.repository.BillDetailRepository;
import org.fpoly.capstone.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;
@Autowired
private BillDetailRepository billDetailRepository;
    @Override
    public void sendEmail(String to, String subject, String htmlContent){
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try{
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
            System.out.println("Email sent");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Email Failed");
        }
    }

    @Override
    public String generateHtmlContent(Bill bill) {
        StringBuilder html = new StringBuilder();
        String meThodBill = String.valueOf(bill.getMethod());
        if (meThodBill.equals("TIEN_MAT")) {
            meThodBill = "Tiền mặt";
        }else if (meThodBill.equals("CHUYEN_KHOAN")) {
            meThodBill = "Chuyển khoản";
        }
        List<BillDetail> billDetails = billDetailRepository.findByBillId(bill.getId());
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
                .append("<p><span class='info-title'>Nhân viên bán hàng:</span> <strong>").append("Kim Thanh").append("</strong></p>")
                .append("</div>")
                .append("</div>")

                .append("<div class='info-section'>")
                .append("<div class='info-left'>")
                .append("<p><span class= 'info-title'>Thông tin người mua hàng</span></p>")
                .append("<p><span class='info-title'>Khách hàng:</span> <strong>").append(bill.getUser().getFullName() != null ? bill.getUser().getFullName() : "Khách lẻ").append("</strong></p>")
                .append("<p><span class='info-title'>Số điện thoại:</span> <strong>").append(bill.getUser().getPhoneNumber() != null ? bill.getUser().getPhoneNumber(): "").append("</strong></p>")
                .append("<p><span class= 'info-title'>Thông tin người nhận hàng</span></p>")
                .append("<p><span class='info-title'>Họ tên:</span> <strong>").append(bill.getUserName() != null ? bill.getUserName() : "Khách lẻ").append("</strong></p>")
                .append("<p><span class='info-title'>Số điện thoại người nhận:</span> <strong>").append(bill.getPhoneNumber() != null ? bill.getPhoneNumber() : "").append("</strong></p>")
                .append("<p><span class='info-title'>Địa chỉ:</span> <strong>").append(bill.getAddress()).append("</strong></p>")
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
                .append("<p class='total'>Tổng tiền thanh toán: <strong>").append(currencyFormat.format(bill.getTotalMoney())).append("</strong></p>")

                .append("</body></html>");

        return html.toString();
    }

}
