package org.fpoly.capstone.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterDTO {

    @NotEmpty(message = "Vui lòng nhập họ và tên")
    private String fullName;

    @NotEmpty(message = "Vui lòng nhập email")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotEmpty(message = "Vui lòng nhập số điện thoại")
    @Pattern(regexp = "^(0\\d{9})$", message = "Số điện thoại không hợp lệ")
    private String phoneNumber;

    @NotEmpty(message = "Vui lòng nhập mật khẩu")
    private String password;
}