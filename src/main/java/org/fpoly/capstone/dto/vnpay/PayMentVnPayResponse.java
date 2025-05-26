package org.fpoly.capstone.dto.vnpay;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.fpoly.capstone.dto.billDetail.BillDetailOnline;
import org.fpoly.capstone.dto.voucherdetail.CreateVoucherDetailRequest;
import org.fpoly.capstone.entity.VoucherDetail;
import org.fpoly.capstone.entity.enum_status.BillType;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PayMentVnPayResponse {
    private String vnp_Amount;
    private String vnp_BankCode;
    private String vnp_BankTranNo;
    private String vnp_CardType;
    private String vnp_OrderInfo;
    private String vnp_PayDate;
    private String vnp_ResponseCode;
    private String vnp_TmnCode;
    private String vnp_TransactionNo;
    private String vnp_TransactionStatus;
    private String vnp_TxnRef;
    private String vnp_SecureHash;
    private Long idUser;
    private String userName;
    private String phoneNumber;
    private String email;
    private Boolean openDelivery;
    private BigDecimal itemDiscount;
    private BigDecimal moneyShip;
    private BigDecimal totalPrice;
    private BillType type;
    private String address;
    private Long idVoucher;
    private Long idBill;
    private String note;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date shipDate;
    private List<BillDetailOnline> billDetail;
    private BigDecimal afterPrice;

//    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "dd/MM/yyyy")
    private String deliveryDate;
    private List<CreateVoucherDetailRequest> voucherDetails;
    public String toParamsString() {
        return "vnp_Amount=" + setParam(vnp_Amount) +
                "&vnp_BankCode=" + setParam(vnp_BankCode) +
                "&vnp_BankTranNo=" + setParam(vnp_BankTranNo) +
                "&vnp_CardType=" + setParam(vnp_CardType) +
                "&vnp_OrderInfo=" + setParam(vnp_OrderInfo) +
                "&vnp_PayDate=" + setParam(vnp_PayDate) +
                "&vnp_ResponseCode=" + setParam(vnp_ResponseCode) +
                "&vnp_TmnCode=" + setParam(vnp_TmnCode) +
                "&vnp_TransactionNo=" + setParam(vnp_TransactionNo) +
                "&vnp_TransactionStatus=" + setParam(vnp_TransactionStatus) +
                "&vnp_TxnRef=" + setParam(vnp_TxnRef);
    }

    public String setParam(String param){
        try {
            return URLEncoder.encode(param, StandardCharsets.US_ASCII.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
