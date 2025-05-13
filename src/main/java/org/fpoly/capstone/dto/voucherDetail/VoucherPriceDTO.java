package org.fpoly.capstone.dto.voucherdetail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoucherPriceDTO {
    private BigDecimal beforePrice;
    private BigDecimal afterPrice;
    private BigDecimal discountPrice;
    private BigDecimal moneyShip;
}
