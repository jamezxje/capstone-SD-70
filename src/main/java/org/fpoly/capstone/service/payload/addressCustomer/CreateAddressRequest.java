package org.fpoly.capstone.service.payload.addressCustomer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAddressRequest {

    private String fullName;
    private String phoneNumber;
    private String province;
    private Long provinceId;
    private String district;
    private Long districtId;
    private String ward;
    private Long wardCode;
    private String line;
}
