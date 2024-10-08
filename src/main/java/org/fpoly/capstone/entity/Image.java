package org.fpoly.capstone.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fpoly.capstone.entity.base.BaseEntity;
import org.fpoly.capstone.entity.type.ImageFormat;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "image")
public class Image extends BaseEntity {

  @Column(name = "name")
  private String name;

  @Column(name = "format")
  @Enumerated(EnumType.STRING)
  private ImageFormat format;

  @Column(name = "url")
  private String url;

  @Column(name = "alt")
  private String alt;

}
