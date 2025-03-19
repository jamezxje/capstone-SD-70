package org.fpoly.capstone.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fpoly.capstone.common.CommonUtils;
import org.fpoly.capstone.common.CommonUtils;
import org.fpoly.capstone.entity.enum_status.BillStatus;
import org.fpoly.capstone.entity.enum_status.BillType;
import org.fpoly.capstone.entity.enum_status.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.List;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "bill")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_employee", referencedColumnName = "id")
    @JsonBackReference
    private User employee;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "email")
    private String email;

    @Column(name = "item_discount", precision = 38, scale = 2)
    private BigDecimal itemDiscount;

    @Column(name = "total_money", precision = 38, scale = 2)
    private BigDecimal totalMoney;

    @Column(name = "confirmation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date confirmationDate;

    @Column(name = "ship_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date shipDate;

    @Column(name = "receive_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receiveDate;

    @Column(name = "completion_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime completionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private BillType type;

    @Column(name = "note")
    private String note;

    @Column(name = "money_ship", precision = 38, scale = 2)
    private BigDecimal moneyShip;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BillStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "method")
    private PaymentMethod method;

    @Column(name = "vnp_transaction")
    private String vnpTransaction;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VoucherDetail> voucherDetailList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bill")
    private List<BillDetail> billDetailList;

    @PrePersist
    public void prePersist() {
        if (this.createDate == null) {
            this.completionDate = LocalDateTime.now();
            this.createDate = LocalDateTime.now();
        }
        this.createdBy = CommonUtils.getPrincipal();
    }

    @PreUpdate
    public void preUpdate() {
        if (this.lastModifiedDate == null) {
            this.lastModifiedDate = LocalDateTime.now();
        }

        this.updatedBy = CommonUtils.getPrincipal();
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }
}
