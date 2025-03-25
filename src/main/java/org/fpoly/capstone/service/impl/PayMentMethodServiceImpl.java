package org.fpoly.capstone.service.impl;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.fpoly.capstone.constant.MessageError;
import org.fpoly.capstone.constant.VnPayConstant;
import org.fpoly.capstone.dto.vnpay.CreatePayMentMethodRequest;
import org.fpoly.capstone.dto.vnpay.OrderInFor;
import org.fpoly.capstone.dto.vnpay.PayMentVnPayResponse;
import org.fpoly.capstone.entity.*;
import org.fpoly.capstone.entity.enum_status.BillStatus;
import org.fpoly.capstone.entity.enum_status.BillType;
import org.fpoly.capstone.entity.enum_status.PaymentMethod;
import org.fpoly.capstone.repository.*;
import org.fpoly.capstone.service.EmailService;
import org.fpoly.capstone.service.PaymentMethodService;
import org.fpoly.capstone.utils.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class PayMentMethodServiceImpl implements PaymentMethodService {
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private BillHistoryRepository billHistoryRepository;
    @Autowired
    private VoucherDetailReponsitory voucherDetailReponsitory;
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Override
    public String payWithVnpay(CreatePayMentMethodRequest payModel, HttpServletRequest request) throws UnsupportedEncodingException {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        LocalDateTime expireTime = now.plusMinutes(15);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String vnp_CreateDate = now.format(formatter);
        String vnp_ExpireDate = expireTime.format(formatter);

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VnPayConstant.vnp_Version);
        vnp_Params.put("vnp_Command", VnPayConstant.vnp_Command);
        vnp_Params.put("vnp_TmnCode", VnPayConstant.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", payModel.getVnp_Amount() + "00");
        vnp_Params.put("vnp_BankCode", VnPayConstant.vnp_BankCode);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_CurrCode", VnPayConstant.vnp_CurrCode);
        vnp_Params.put("vnp_IpAddr", Config.getIpAddress(request));
        vnp_Params.put("vnp_Locale", VnPayConstant.vnp_Locale);
        vnp_Params.put("vnp_OrderInfo", payModel.getVnp_OrderInfo());
        vnp_Params.put("vnp_OrderType", payModel.getVnp_OrderType());
        vnp_Params.put("vnp_ReturnUrl", VnPayConstant.vnp_ReturnUrl);
        vnp_Params.put("vnp_TxnRef", String.valueOf(payModel.getVnp_TxnRef()));
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        List fieldList = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldList);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        Iterator itr = fieldList.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append("=");
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append("=");
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                if (itr.hasNext()) {
                    query.append("&");
                    hashData.append("&");
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(VnPayConstant.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnPayConstant.vnp_Url + "?" + queryUrl;
        return paymentUrl;
    }

    @Override
    public boolean paymentSucessFully(PayMentVnPayResponse response) {
        System.out.println("Check trạngthaisi " + response.getVnp_ResponseCode());

        if (response.getVnp_ResponseCode().equals("00")) {
            String billCode = response.getVnp_TxnRef().split("-")[0];
            String deliveryDateStr = response.getDeliveryDate();

            Date deliveryDate = null;
            if (deliveryDateStr != null && !deliveryDateStr.trim().isEmpty() && !deliveryDateStr.equals("null")) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    deliveryDate = dateFormat.parse(deliveryDateStr);
                } catch (Exception e) {
                    System.out.println("Ngày không hợp lệ: " + deliveryDateStr);
                }
            }
            Optional<Bill> billOptional = billRepository.findByCode(billCode);


            if (billOptional.isPresent()) {

                Bill bill = billOptional.get();
                Long idVoucher = response.getIdVoucher();
                Long idBill = response.getIdBill();
                Optional<Bill> idBillOptional = billRepository.findById(idBill);
                Optional<User> optionalUser = userRepository.findById(response.getIdUser());
                if (!idBillOptional.isPresent()) {
                    throw new RuntimeException(MessageError.BILL_NULL.getMessage());
                }
                if (!optionalUser.isPresent()) {
                    throw new RuntimeException(MessageError.EMAIL_NULL.getMessage());
                }
                User user = optionalUser.get();
                Bill billId = idBillOptional.get();
                bill.setUser(user);
                bill.setLastModifiedDate(Calendar.getInstance().getTime());
                bill.setTotalMoney(new BigDecimal(response.getVnp_Amount().substring(0, response.getVnp_Amount().length() - 2)));  // Chuyển đổi tiền
                bill.setMethod(PaymentMethod.CHUYEN_KHOAN);
                bill.setUserName(response.getUserName());
                System.out.println("Láy username " + response.getUserName());
                System.out.println("Lay email " + bill.getEmail());
                System.out.println("lay phone" + bill.getPhoneNumber());
                bill.setPhoneNumber(response.getPhoneNumber());
                bill.setEmail(response.getEmail());
                bill.setAddress(response.getAddress());
                bill.setItemDiscount(response.getItemDiscount());
                bill.setMoneyShip(response.getMoneyShip());
                bill.setNote("Thanh toán thành công VNPay");
                System.out.println("Cehck ngggayf ship" + response.getDeliveryDate());
                if (deliveryDate != null) {
                    bill.setShipDate(deliveryDate);
                    bill.setStatus(BillStatus.CHO_XAC_NHAN);
                } else {
                    bill.setShipDate(null);  // Nếu không có ngày giao hàng, gán null
                    bill.setStatus(BillStatus.THANH_CONG);
                }
                bill.setVnpTransaction(response.getVnp_TransactionNo());
                billRepository.save(bill);

                billHistoryRepository.save(BillHistory.builder()
                        .status(BillStatus.DA_THANH_TOAN)
                        .bill(bill)
                        .user(bill.getEmployee())
                        .build());

                response.getVoucherDetails().forEach(voucher -> {
                    Optional<Voucher> vouchers = voucherRepository.findById(idVoucher);
                    if (!vouchers.isPresent()) {
                        throw new RuntimeException("Voucher not found");
                    }
                    if (vouchers.get().getQuantity() <= 0 &&
                            vouchers.get().getEndDate().toInstant(ZoneOffset.UTC).toEpochMilli() < System.currentTimeMillis()) {
                        throw new RuntimeException("Voucher end date is less than current date");
                    }
                    vouchers.get().setQuantity(vouchers.get().getQuantity() - 1);
                    voucherRepository.save(vouchers.get());
                    VoucherDetail voucherDetail = VoucherDetail.builder()
                            .voucher(vouchers.get())
                            .bill(billId)
                            .afterPrice(new BigDecimal(voucher.getAfterVoucher()))
                            .beforePrice(new BigDecimal(voucher.getBeforVoucher()))
                            .discountPrice(new BigDecimal(voucher.getDiscountVoucher()))
                            .build();
                    voucherDetailReponsitory.save(voucherDetail);
                    System.out.println("okoko");
                });
                try {
                   if (bill.getEmail() != null) {
                       emailService.sendEmail(bill.getEmail() , "Thannh toán hóa đơn" ,emailService.generateHtmlContent(bill) );
                   }else {
                       System.out.println("Email null no send");
                   }
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return true;
    }

    private void sendInVoiceEmail(Bill bill) throws MessagingException {
        System.out.println("Check gửi mail " + bill.getEmail());
        System.out.println("Gửi email đến: " );
        System.out.println("Chủ đề email: " );
        System.out.println("Nội dung email: ");

        emailService.sendEmail(bill.getEmail(), "Thanh toán hóa đơn ", emailService.generateHtmlContent(bill));
    }

}
