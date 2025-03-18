package org.fpoly.capstone.dto.billDetail;

import lombok.Getter;
import lombok.Setter;
import org.fpoly.capstone.entity.enum_status.BillStatus;
import org.fpoly.capstone.entity.enum_status.BillType;
import org.fpoly.capstone.entity.enum_status.PaymentMethod;

import java.util.List;

@Getter
@Setter
public class ChangeStatusBillRequest {
    private String actionDescription;
    private PaymentMethod method;
    private String totalMoney;
    private boolean statusCancel = false;
    private String paymentMethod;
    private List<BillDetailOnline> billDetail;
}
