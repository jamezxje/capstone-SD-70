package org.fpoly.capstone.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {

    private String id;

    @NotEmpty(message = "Vui lòng không để trống số nhà/đường")
    private String line;

    @NotBlank(message = "Vui lòng không để trống quận/huyện")
    private String district;

    @NotBlank(message = "Vui lòng không để trống tỉnh/thành phố")
    private String province;

    @NotBlank(message = "Vui lòng không để trống xã/phường")
    private String ward;

    @NotEmpty(message = "Vui lòng không để trống mã xã/phường")
    private String wardCode;

    @NotBlank(message = "Vui lòng không để trống mã quận/huyện")
    private String toDistrictId;

    @NotBlank(message = "Vui lòng không để trống mã tỉnh/thành phố")
    private String provinceId;
}
