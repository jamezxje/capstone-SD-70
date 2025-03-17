package org.fpoly.capstone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "voucher_detail")
public class VoucherDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_bill", referencedColumnName = "id")
    private Bill bill;

    @ManyToOne
    @JoinColumn(name = "id_voucher", referencedColumnName = "id")
    private Voucher voucher;

    @Column(name = "befor_price")
    private BigDecimal beforePrice;

    @Column(name = "after_price")
    private BigDecimal afterPrice;

    @Column(name = "discount_price")
    private BigDecimal discountPrice;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "last_modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;
    @PrePersist
    protected void onCreate() {
        this.createDate = new Date();
        this.lastModifiedDate = new Date();
    }
    @PreUpdate
    protected void onUpdate() {
        this.lastModifiedDate = new Date();
    }

}
