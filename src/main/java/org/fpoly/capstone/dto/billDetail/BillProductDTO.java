package org.fpoly.capstone.dto.billDetail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillProductDTO {
private Long id;
private String name;
private BigDecimal price;
private Integer quantity;
private String size;
private String color;
private Long idProductDetail;
}
