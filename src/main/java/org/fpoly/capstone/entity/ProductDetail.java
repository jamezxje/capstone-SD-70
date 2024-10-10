package org.fpoly.capstone.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fpoly.capstone.entity.base.BaseEntity;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_detail")
public class ProductDetail extends BaseEntity {

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "product_id")
  private Product product;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "category_id")
  private Category category;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "color_id")
  private Color color;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "material_id")
  private Material material;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "size_id")
  private Size size;

  @OneToMany
  private List<Image> images;

  @Column(name = "stock_quantity")
  private Long stockQuantity;

  @Column(name = "base_price")
  private BigDecimal basePrice;

  @Column(name = "status")
  private Boolean status;

}
