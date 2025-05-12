package org.fpoly.capstone.dto.billDetail;

import lombok.Data;

@Data
public class BillDetailDTO {
    private Long id;
    private String productName;
    private Float price;
    private Integer quantity;
    private String size;
    private String color;
    private Long productId;

    public BillDetailDTO(Object[] row) {
        if (row != null && row.length >= 7) {
            this.id = (row[0] != null) ? ((Number) row[0]).longValue() : null;
            this.productName = (row[1] != null) ? row[1].toString() : "N/A";
            this.price = (row[2] != null) ? ((Number) row[2]).floatValue() : 0.0f;
            this.quantity = (row[3] != null) ? ((Number) row[3]).intValue() : 0;
            this.size = (row[4] != null) ? row[4].toString() : "Unknown";
            this.color = (row[5] != null) ? row[5].toString() : "Unknown";
            this.productId = (row[6] != null) ? ((Number) row[6]).longValue() : null;
        }
    }
}
