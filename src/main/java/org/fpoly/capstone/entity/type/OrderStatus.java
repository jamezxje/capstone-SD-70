package org.fpoly.capstone.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {

  READY("Người bán đang chuẩn bị hàng"),
  READY_DONE("Người bán đã chuẩn bị xong, đang bàn giao cho đơn vị vận chuyển"),
  SENDING("Đơn hàng của bạn đang trên đường tới chỗ bạn"),
  DONE("Đơn hàng của bạn đã hoàn thành"),
  CANCEL("Đơn hàng của bạn đã được hủy");

  private final String orderStatus;

}
