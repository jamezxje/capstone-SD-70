package org.fpoly.capstone.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "matetial")
public class Material extends BaseEntity {

  @Column(name = "name")
  private String name;

  @Column(name = "status")
  private Boolean status;

}
