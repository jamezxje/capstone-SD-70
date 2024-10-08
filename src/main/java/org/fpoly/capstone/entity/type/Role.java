package org.fpoly.capstone.entity.type;

import lombok.Getter;

@Getter
public enum Role {

  ROLE_CUSTOMER("Customer"),
  ROLE_ADMIN("Admin");

  private final String value;

  Role(String value) {
    this.value = value;
  }

  public String getTextClass() {
    return switch (this) {
      case ROLE_CUSTOMER -> "primary";
      case ROLE_ADMIN -> "danger";
    };
  }

}

