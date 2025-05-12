package org.fpoly.capstone.dto.bill;

import lombok.Getter;
import lombok.Setter;
import org.fpoly.capstone.entity.enum_status.UserRole;

@Getter
@Setter
public class CreateCustomerBill {
    private String customerName;
    private String customerEmail;
    private String customerPhone;
}
