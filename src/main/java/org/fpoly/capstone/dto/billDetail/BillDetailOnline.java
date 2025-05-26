package org.fpoly.capstone.dto.billDetail;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class BillDetailOnline {
    private Long idProductDetail;
    private BigDecimal price;
    private Integer quantity;

}
