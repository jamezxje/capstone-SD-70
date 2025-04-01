package org.fpoly.capstone.service.payload.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fpoly.capstone.entity.enum_status.ProductStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private Long id;
    private String code;
    private String name;
    private ProductStatus status;
    private Long categoryId;
    private String categoryName;
    private LocalDateTime createDate;
    private String createBy;
    private LocalDateTime lastModifiedDate;
    private String updateBy;

}
