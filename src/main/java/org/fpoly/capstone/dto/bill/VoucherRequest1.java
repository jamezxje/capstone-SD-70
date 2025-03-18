package org.fpoly.capstone.dto.bill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoucherRequest1 {
    private Long id;
    private String code;
    private String name;
    private BigDecimal value;
    private Integer minimumBill;
    private Integer quantity;
    private Date startDate;
    private Date endDate;

}
