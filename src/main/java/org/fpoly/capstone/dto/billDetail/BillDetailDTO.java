package org.fpoly.capstone.dto.billDetail;

import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillDetailDTO {
    private Long id;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private String size;
    private String color;
    private Long productId;
    private String image;
}
