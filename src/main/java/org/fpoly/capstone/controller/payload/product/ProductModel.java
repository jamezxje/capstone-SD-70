package org.fpoly.capstone.controller.payload.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fpoly.capstone.entity.enum_status.ProductStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel {

    private Long id;
    private String code;
    private String name;
    private ProductStatus status;
    private Long categoryId;
}
