package org.fpoly.capstone.service;

import jakarta.servlet.http.HttpServletRequest;
import org.fpoly.capstone.dto.vnpay.CreatePayMentMethodRequest;

import java.io.UnsupportedEncodingException;

public interface VnPayService {
    String payWithVNPAY(CreatePayMentMethodRequest payment , HttpServletRequest request) throws UnsupportedEncodingException;

}
