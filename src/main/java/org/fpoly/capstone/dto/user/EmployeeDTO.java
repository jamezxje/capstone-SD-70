package org.fpoly.capstone.dto.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.fpoly.capstone.entity.enum_status.UserStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class EmployeeDTO {

    private String id;

    @NotEmpty(message = "Vui lòng không để trống họ và tên")
    private String fullName;

    @NotEmpty(message = "Vui lòng không để trống email")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotEmpty(message = "Vui lòng không để trống số điện thoại")
    private String phoneNumber;

    private String avatar;

    @NotNull(message = "Vui lòng không để trống giới tính")
    private Boolean gender;

    @NotNull(message = "Vui lòng không để trống trạng thái")
    private UserStatus status;

    @NotNull(message = "Vui lòng không để trống ngày sinh")
    private Date dateOfBirth;

    @NotEmpty(message = "Vui lòng không để trống căn cước công dân")
    private String citizenIdentity;

    @Valid // Kiểm tra validation cho danh sách địa chỉ
    @NotEmpty(message = "Danh sách địa chỉ không được để trống")
    private List<AddressDTO> addresses = new ArrayList<>();
}
