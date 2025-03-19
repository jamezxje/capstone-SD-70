package org.fpoly.capstone.dto.billDetail;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBillDetailRequest {
//    @NotEmpty
    private Long idBill;
//    @NotEmpty
    private Long idProduct;
//    @NotNull
    private Integer quantity;
//    @NotEmpty
    private String totalMoney;
//    @NotEmpty
    private String price;
    private String note;
}
