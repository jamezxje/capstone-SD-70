package org.fpoly.capstone.dto.voucherdetail;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateVoucherDetailRequest {
    private Long idVoucher;
    private String beforVoucher;
    private String afterVoucher;
    private String  discountVoucher;
}
