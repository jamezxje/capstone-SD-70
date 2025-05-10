package org.fpoly.capstone.dto.billDetail;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BillDetailRequest {
//    @NotEmpty
    private Long id;
    private String statusBill;
}
