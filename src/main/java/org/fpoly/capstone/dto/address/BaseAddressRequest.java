package org.fpoly.capstone.dto.address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fpoly.capstone.entity.enum_status.AddressStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseAddressRequest {
    private Long id;
    private String line;
    private String district;
    private String province;
    private String ward;
    private Integer districtId;
    private Integer provinceId;
    private String wardCode;
    private String fullName;
    private String phoneNumber;
    private AddressStatus status;
    private Long idUser;
}
