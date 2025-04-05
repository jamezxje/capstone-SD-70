package org.fpoly.capstone.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fpoly.capstone.common.CommonUtils;
import org.fpoly.capstone.entity.enum_status.Gender;
import org.fpoly.capstone.entity.enum_status.ProductVariantStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_detail")
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_product", referencedColumnName = "id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "id_size", referencedColumnName = "id")
    private Size size;

    @ManyToOne
    @JoinColumn(name = "id_color", referencedColumnName = "id")
    private Color color;

    @ManyToOne
    @JoinColumn(name = "id_material", referencedColumnName = "id")
    private Material material;

    @ManyToOne
    @JoinColumn(name = "id_brand", referencedColumnName = "id")
    private Brand brand;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price", precision = 38, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductVariantStatus status;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Column(name = "created_by", length = 255)
    private String createdBy;

    @Column(name = "updated_by", length = 255)
    private String updatedBy;

    @OneToMany(mappedBy = "productDetail", fetch = FetchType.EAGER)
    private List<BillDetail> billDetail;

    @OneToMany(mappedBy = "productDetail")
    @JsonBackReference
    private List<Image> images;

    @Column(name = "feature_image", length = 255)
    private String featureImage;


    @PrePersist
    public void prePersist() {
        if (this.createDate == null) {
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
        return this.getClass().hashCode(); // Avoid circular reference in hashCode
    }

}
