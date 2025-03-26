package org.fpoly.capstone.dto.bill;

import lombok.*;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.enum_status.BillStatus;
import org.fpoly.capstone.entity.enum_status.BillType;
import org.fpoly.capstone.entity.enum_status.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class BaseBill {
    private Long id;
    private String code;
    private String phoneNumber;
    private String address;
    private String userName;
    private String email;
    private BigDecimal itemDiscount;
    private BigDecimal totalMoney;
    private Date confirmationDate;
    private Date shipDate;
    private Date receiveDate;
    private Date completionDate;
    private BillType type;
    private String note;
    private BigDecimal moneyShip;
    private BillStatus status;
    private PaymentMethod method;
    private String vnpTransaction;
    private LocalDateTime createDate;  // change LocalDateTime to Date
    private String createdBy;
    private LocalDateTime lastModifiedDate;  // change LocalDateTime to Date
    private String updatedBy;
    private User customer;
    private User employee;


}

