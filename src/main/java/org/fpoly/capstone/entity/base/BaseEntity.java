package org.fpoly.capstone.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

  @Id
  @Column(updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "created_on")
  private LocalDateTime createdOn;

  @Column(name = "created_by")
  private String createdBy;

  @Column(name = "updated_on")
  private LocalDateTime updatedOn;

  @Column(name = "updated_by")
  private String updatedBy;

  @PrePersist
  public void prePersist() {
    if (this.createdOn == null) {
      this.createdOn = LocalDateTime.now();
    }

//    createdBy = LoggedUser.get();
  }

  @PreUpdate
  public void preUpdate() {
    if (this.updatedOn == null) {
      this.updatedOn = LocalDateTime.now();
    }

//    updatedBy = LoggedUser.get();
  }

}