package org.fpoly.capstone.dto.bill;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.fpoly.capstone.dto.billDetail.BillDetailOnline;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CreateBillCustomerOnlineRequest {
    private String userName;
    private String phoneNumber;
    private String email;
    private String address;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date shipDate;
    private Date receiveDate;
    private BigDecimal moneyShip;
    private BigDecimal itemDiscount;
    private BigDecimal totalMoney;
    private String vnpTransaction;
    private List<BillDetailOnline> billDetail;
    private String paymentMethod;
    private Long idVoucher;
    private BigDecimal afterPrice;
    private String note;
    private Long idUser;
    private String method;
    private Long productId;
    private Long sizeId;
    private Long colorId;
}


