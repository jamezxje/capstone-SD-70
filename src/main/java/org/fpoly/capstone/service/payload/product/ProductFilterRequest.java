package org.fpoly.capstone.service.payload.product;

import lombok.Getter;
import lombok.Setter;
import org.fpoly.capstone.entity.enum_status.ProductStatus;

@Getter
@Setter
public class ProductFilterRequest {

    private String code;
    private String name;
    private ProductStatus status;
    private Long categoryId;

}
