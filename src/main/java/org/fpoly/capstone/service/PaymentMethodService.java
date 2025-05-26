package org.fpoly.capstone.service;

import jakarta.servlet.http.HttpServletRequest;
import org.fpoly.capstone.dto.vnpay.CreatePayMentMethodRequest;
import org.fpoly.capstone.dto.vnpay.PayMentVnPayResponse;
import org.fpoly.capstone.entity.Bill;

import java.io.UnsupportedEncodingException;

public interface PaymentMethodService {
    String payWithVnpay(CreatePayMentMethodRequest payModel , HttpServletRequest request) throws UnsupportedEncodingException;
    boolean paymentSucessFully(PayMentVnPayResponse response);
    boolean payMentSucessFullyOnlineNoLogin(PayMentVnPayResponse request);
}
