package org.fpoly.capstone.service.payload.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartUpdateRequest {

    private Long cartId;
    private Long cartDetailId;
    private Long productDetailId;
    private Integer quantity;

}
