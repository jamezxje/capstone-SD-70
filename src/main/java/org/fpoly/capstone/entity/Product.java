package org.fpoly.capstone.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.fpoly.capstone.entity.base.BaseEntity;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "product")
public class Product extends BaseEntity {

  @Column(name = "code")
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID code;

  @Column(name = "name")
  private String name;

  @Column(name = "weight")
  private String weight;

  @Column(name = "gsm_qualification")
  private String gsmQualification;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "feature_image_id")
  private Image featureImage;

  @Column(name = "description")
  private String description;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "size_guide_id")
  private Image sizeGuide;

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
  private Double basePrice;

  @Column(name = "status")
  private Boolean status;

}
