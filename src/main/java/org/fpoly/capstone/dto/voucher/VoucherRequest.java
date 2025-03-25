package org.fpoly.capstone.dto.voucher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fpoly.capstone.entity.enum_status.VoucherStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoucherRequest {
private Long id;
private String code;
private String name;
private BigDecimal value;
private Integer minimumBill;
private Integer quantity;
private LocalDateTime startDate;
private LocalDateTime endDate;
private VoucherStatus status;
}
