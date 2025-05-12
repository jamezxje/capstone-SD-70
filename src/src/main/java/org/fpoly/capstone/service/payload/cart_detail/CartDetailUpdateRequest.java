package org.fpoly.capstone.service.payload.cart_detail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDetailUpdateRequest {

    private Long cartDetailId;
    private Long productDetailId;
    private Integer quantity;

}
