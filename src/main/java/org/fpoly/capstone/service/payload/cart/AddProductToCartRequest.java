package org.fpoly.capstone.service.payload.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddProductToCartRequest {

    private Long productId;
    private Long sizeId;
    private Integer quantity;

}
