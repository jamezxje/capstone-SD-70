package org.fpoly.capstone.controller.payload.cart_detail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDetailViewModel {

    private Long id;
    private Long productDetailId;
    private String productName;
    private String productFeatureImageUrl;
    private String sizeName;
    private String colorName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;

    public void setPrice(BigDecimal price) {
        this.price = price;
        this.updateSubtotal();
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        this.updateSubtotal();
    }

    private void updateSubtotal() {
        if (this.price != null && this.quantity != null) {
            this.subtotal = this.price.multiply(BigDecimal.valueOf(this.quantity));
        } else {
            this.subtotal = BigDecimal.ZERO;
        }
    }
}
