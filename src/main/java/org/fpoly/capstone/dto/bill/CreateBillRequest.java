package org.fpoly.capstone.dto.bill;

import lombok.Data;

@Data
public class CreateBillRequest {
    private String idUser;
    private String name;
    private String numberPhone;
    private String address;
}
