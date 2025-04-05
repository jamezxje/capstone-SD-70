package org.fpoly.capstone.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fpoly.capstone.entity.enum_status.VoucherStatus;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "voucher")
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @NotEmpty(message = "Tên không được trống")
    @Column(name = "name")
    private String name;

    @NotNull(message = "Số tiền không được trống")
    @Column(name = "value")
    private BigDecimal value;

    @NotNull(message = "Số lượng không được trống")
    @Column(name = "quantity")
    private Integer quantity;

    @NotNull(message = "Ngày bắt đầu không được trống")
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startDate;

    @NotNull(message = "Ngày kết thúc không được trống")
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private VoucherStatus status;

    @Column(name = "minimum_bill")
    private Integer minimumBill;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "last_modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastModifiedDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @OneToMany(mappedBy = "voucher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VoucherDetail> voucherDetails;


}
