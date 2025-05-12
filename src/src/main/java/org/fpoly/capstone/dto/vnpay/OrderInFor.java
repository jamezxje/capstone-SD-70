package org.fpoly.capstone.dto.vnpay;

import lombok.Getter;
import lombok.Setter;
import org.fpoly.capstone.entity.enum_status.BillType;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class OrderInFor {
    private String userName;
    private String phoneNumber;
    private String email;
    private Boolean openDelivery;
    private BigDecimal itemDiscount;
    private BigDecimal moneyShip;
    private BillType type;
    private String address;
    private Date deliveryDate;
}
