package org.fpoly.capstone.service.payload.bill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBillDetailFromCartRequest {

    private Long cartDetailId;

}
