package org.fpoly.capstone.dto.bill;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.enum_status.BillType;
import org.fpoly.capstone.entity.enum_status.PaymentMethod;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class BaseBill {
    private String phoneNumber;
    private String address;
    private String userName;
    private String email;
    private BigDecimal itemDiscount;
    private BigDecimal totalMoney;
    private Date comfirmationDate;
    private Date shipDate;
    private Date receiveDate;
    private Date completionDate;
    private BillType type;
    private String note;
    private PaymentMethod method;
    private String vnpTransaction;
    private Date createDate;
    private String createdBy;
    private Date lastModifiedDate;
    private String updateby;
    private User customer;
    private User employee;
}
