package org.fpoly.capstone.constant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class VnPayConstant {
    public static String vnp_Version = "2.1.0";
    public static String vnp_Command = "2.1.0";
    public static String vnp_TmnCode = "76DX0M7W";

    public static String vnp_HashSecret = "4UVIZHKXOAGINENTYU76BHUUJU1CSX70";
    public static String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static String vnp_Refound ="https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";
    public static String vnp_BankCode = "";
    public static String vnp_CurrCode = "VND";
    public static String vnp_Locale = "vn";
    public static String vnp_ReturnUrl = "http://localhost:8080/payment-success";
    public static String vnp_ReturnUrlBuyOnline = "http://localhost:3000/client/payment/payment-success";
}
