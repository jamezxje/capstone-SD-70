package org.fpoly.capstone.validation;

import org.fpoly.capstone.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class UserValidator {

    public static Map<String, String> validate(User user, Set<String> fieldsToValidate) {
        Map<String, String> errors = new HashMap<>();

        if (fieldsToValidate.contains("fullName")) {
            if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
                errors.put("fullName", "Vui lòng không để trống họ và tên");
            } else if (!user.getFullName().matches("^[\\p{L} ]{1,50}$")) {
                errors.put("fullName", "Họ và tên phải là chữ và tối đa 50 ký tự");
            }
        }
        if (fieldsToValidate.contains("dateOfBirth")) {
            if (user.getDateOfBirth() == null) {
                errors.put("dateOfBirth", "Vui lòng không để trống ngày sinh");
            } else {
                LocalDate birthDate = user.getDateOfBirth().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate today = LocalDate.now();
                long age = ChronoUnit.YEARS.between(birthDate, today);

                if (birthDate.isAfter(today)) {
                    errors.put("dateOfBirth", "Vui lòng không chọn ngày sinh trong tương lai");
                } else if (age < 18) {
                    errors.put("dateOfBirth", "Tuổi phải từ 18 tuổi trở lên");
                } else if (age > 100) {
                    errors.put("dateOfBirth", "Tuổi không thể lớn hơn 100");
                }
            }
        }
        if (fieldsToValidate.contains("phoneNumber")) {
            if (user.getPhoneNumber() == null || user.getPhoneNumber().trim().isEmpty()) {
                errors.put("phoneNumber", "Vui lòng không để trống số điện thoại");
            } else if (!user.getPhoneNumber().matches("^(0\\d{9})$")) {
                errors.put("phoneNumber", "Số điện thoại phải bắt đầu từ 0 (10 số)");
            }
        }
        if (fieldsToValidate.contains("email")) {
            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                errors.put("email", "Vui lòng không để trống email");
            } else if (!user.getEmail().matches("^[a-zA-Z0-9][a-zA-Z0-9._%+-]*@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
                errors.put("email", "Email không đúng định dạng");
            }
        }

        if (fieldsToValidate.contains("citizenIdentity")) {
            if (user.getCitizenIdentity() == null || user.getCitizenIdentity().trim().isEmpty()) {
                errors.put("citizenIdentity", "Vui lòng không để trống CCCD");
            } else if (!user.getCitizenIdentity().matches("^[0-9]{12}$")) {
                errors.put("citizenIdentity", "Căn cước công dân phải gồm 12 chữ số");
            }
        }
        if (fieldsToValidate.contains("gender")) {
            if (user.getGender() == null) {
                errors.put("gender", "Vui lòng không để trống giới tính");
            }
        }

        if (fieldsToValidate.contains("password")) {
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                errors.put("password", "Vui lòng không để trống mật khẩu");
            } else if (user.getPassword().length() < 6 || user.getPassword().length() > 20) {
                errors.put("password", "Mật khẩu phải có từ 6 đến 20 ký tự");
            } else if (!user.getPassword().matches("^[A-Za-z0-9]+$")) {
                errors.put("password", "Mật khẩu chỉ được chứa chữ cái và số, không có ký tự đặc biệt");
            }
        }

        return errors;
    }
}
