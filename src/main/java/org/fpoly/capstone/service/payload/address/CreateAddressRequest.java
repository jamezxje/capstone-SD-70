package org.fpoly.capstone.service.payload.address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAddressRequest {

    private String province;
    private Integer provinceId;
    private String district;
    private Integer districtId;
    private String ward;
    private String wardCode;
    private String detailAddress;
}
