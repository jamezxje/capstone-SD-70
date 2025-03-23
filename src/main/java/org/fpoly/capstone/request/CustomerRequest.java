package org.fpoly.capstone.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.fpoly.capstone.entity.enum_status.UserRole;
import org.fpoly.capstone.entity.enum_status.UserStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class CustomerRequest {

    private String id;

    @NotEmpty(message = "Vui lòng không để trống họ và tên")
    @Pattern(regexp = "^[\\p{L} ]{1,50}$", message = "Họ và tên phải là chữ và tối đa 50 ký tự")
    private String fullName;

    @NotNull(message = "Vui lòng không để trống ngày sinh")
    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    @NotEmpty(message = "Vui lòng không để trống số điện thoại")
    @Pattern(regexp = "^(0\\d{9})$", message = "Số điện thoại phải bắt đầu từ 0 (10 số)")
    private String phoneNumber;

    @NotEmpty(message = "Vui lòng không để trống email")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotNull(message = "Vui lòng không để trống giới tính")
    private Boolean gender;

    private String avatar;
    private UserStatus status;
    private String password;
    private UserRole roles;
    private Date createDate;
    private Date lastModifiedDate;
    private String createdBy;
    private String updatedBy;

    @Valid
    private List<AddressRequest> addresses;
}
