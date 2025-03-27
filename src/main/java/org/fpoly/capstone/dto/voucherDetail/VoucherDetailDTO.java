package org.fpoly.capstone.dto.voucherDetail;

import java.math.BigDecimal;

public class VoucherDetailDTO {
    private BigDecimal beforePrice;
    private BigDecimal afterPrice;
    private BigDecimal discountPrice;

    public VoucherDetailDTO(BigDecimal beforePrice, BigDecimal afterPrice, BigDecimal discountPrice) {
        this.beforePrice = beforePrice;
        this.afterPrice = afterPrice;
        this.discountPrice = discountPrice;
    }

    // Getters & Setters
    public BigDecimal getBeforePrice() { return beforePrice; }
    public void setBeforePrice(BigDecimal beforePrice) { this.beforePrice = beforePrice; }

    public BigDecimal getAfterPrice() { return afterPrice; }
    public void setAfterPrice(BigDecimal afterPrice) { this.afterPrice = afterPrice; }

    public BigDecimal getDiscountPrice() { return discountPrice; }
    public void setDiscountPrice(BigDecimal discountPrice) { this.discountPrice = discountPrice; }
}
