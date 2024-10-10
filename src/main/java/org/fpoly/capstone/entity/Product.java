package org.fpoly.capstone.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fpoly.capstone.entity.base.BaseEntity;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product extends BaseEntity {

  @Column(name = "code")
  private String code;

  @Column(name = "name")
  private String name;

  @Column(name = "weight")
  private String weight;

  @Column(name = "gsm_qualification")
  private String gsmQualification;

  @Column(name = "description")
  private String description;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "size_guide_id")
  private Image sizeGuide;

  @Column(name = "status")
  private Boolean status;

}
