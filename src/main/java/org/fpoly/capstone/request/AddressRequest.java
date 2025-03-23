package org.fpoly.capstone.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.fpoly.capstone.entity.enum_status.AddressStatus;

import java.util.Date;

@Data
public class AddressRequest {

    private String id;
    private String userId; // Thay vì ánh xạ entity User, chỉ lưu userId

    @NotEmpty(message = "Vui lòng không để trống số nhà/đường")
    private String line;

    private String province;
    private String district;

    @NotBlank(message = "Vui lòng không để trống xã/phường")
    private String ward;

    @NotBlank(message = "Vui lòng không để trống mã xã/phường")
    private String wardCode;

    @NotNull(message = "Vui lòng không để trống tỉnh/thành phố")
    private Integer provinceId;

    @NotNull(message = "Vui lòng không để trống quận/huyện")
    private Integer toDistrictId;

    private String fullName;
    private String phoneNumber;
    private AddressStatus addressStatus;
    private Date createDate;
    private Date lastModifiedDate;
    private String createdBy;
    private String updatedBy;
}
