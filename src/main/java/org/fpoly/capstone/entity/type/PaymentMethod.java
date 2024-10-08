package org.fpoly.capstone.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentMethod {

  PAYMENT_WHEN_PICK_UP("Payment when pick up"),
  PAYMENT_ONLINE("Payment online");

  private final String paymentMethod;

}
