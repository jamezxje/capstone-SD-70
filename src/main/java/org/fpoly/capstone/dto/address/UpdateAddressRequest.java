package org.fpoly.capstone.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.fpoly.capstone.entity.enum_status.AddressStatus;

@Getter
@Setter
public class UpdateAddressRequest {
    private String id;

    @NotNull(message = "Tỉnh/Thành phố không được để trống")
    private String provinceId;

    @NotBlank(message = "Tên tỉnh/thành phố không được để trống")
    private String province;

    @NotNull(message = "Quận/Huyện không được để trống")
    private String toDistrictId;

    @NotBlank(message = "Tên quận/huyện không được để trống")
    private String district;

    @NotNull(message = "Xã/Phường không được để trống")
    private String wardCode;

    @NotBlank(message = "Tên xã/phường không được để trống")
    private String ward;

    @NotBlank(message = "Địa chỉ cụ thể không được để trống")
    private String line;

    private AddressStatus status;

    private String userId;

    private String fullName;

    private String phoneNumber;
}
