package org.fpoly.capstone.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

  ADMIN_READ("admin:read"),
  ADMIN_UPDATE("admin:update"),
  ADMIN_CREATE("admin:create"),
  ADMIN_DELETE("admin:delete"),
  CUSTOMER_READ("customer:read"),
  CUSTOMER_UPDATE("admin:update"),
  CUSTOMER_CREATE("admin:create"),
  CUSTOMER_DELETE("admin:delete"),
  ;

  @Getter
  private final String permission;
}
