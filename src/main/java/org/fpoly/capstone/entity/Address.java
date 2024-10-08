package org.fpoly.capstone.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Address {

  @Column(name = "city")
  private String city;

  @Column(name = "district")
  private String district;

  @Column(name = "province")
  private String province;

  @Column(name = "town")
  private String town;

  @Column(name = "street")
  private String street;

  @Column(name = "address_no")
  private String addressNo;

  @Column(name = "description")
  private String description;

}
