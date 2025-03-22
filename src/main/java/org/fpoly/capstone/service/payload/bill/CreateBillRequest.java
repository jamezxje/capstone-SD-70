package org.fpoly.capstone.service.payload.bill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBillRequest {

    private Date receiveDate;
    private BigDecimal moneyShip;
    private String address;
    private String note;
    private BigDecimal grandTotal;

}
