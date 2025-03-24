package org.fpoly.capstone.validation;

import org.fpoly.capstone.entity.Address;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AddressValidator {
    public static Map<String, String> validate(Address address, Set<String> fieldsToValidate) {
        Map<String, String> errors = new HashMap<>();

        if (fieldsToValidate.contains("line")) {
            if (address.getLine() == null || address.getLine().trim().isEmpty()) {
                errors.put("line", "Vui lòng không để trống số nhà/đường");
            }
        }

        if (fieldsToValidate.contains("wardCode")) {
            if (address.getWardCode() == null || address.getWardCode().trim().isEmpty()) {
                errors.put("wardCode", "Vui lòng không để trống xã/phường");
            }
        }

        if (fieldsToValidate.contains("provinceId")) {
            if (address.getProvinceId() == null) {
                errors.put("provinceId", "Vui lòng không để trống tỉnh/thành phố");
            }
        }

        if (fieldsToValidate.contains("toDistrictId")) {
            if (address.getToDistrictId() == null) {
                errors.put("toDistrictId", "Vui lòng không để trống quận/huyện");
            }
        }

        if (fieldsToValidate.contains("fullName")) {
            if (address.getFullName() == null || address.getFullName().trim().isEmpty()) {
                errors.put("fullName", "Vui lòng không để trống họ và tên");
            } else if (!address.getFullName().matches("^[\\p{L} ]{1,50}$")) {
                errors.put("fullName", "Họ và tên chỉ được chứa chữ và tối đa 50 ký tự");
            }
        }

        if (fieldsToValidate.contains("phoneNumber")) {
            if (address.getPhoneNumber() == null || address.getPhoneNumber().trim().isEmpty()) {
                errors.put("phoneNumber", "Vui lòng không để trống số điện thoại");
            } else if (!address.getPhoneNumber().matches("^(0\\d{9})$")) {
                errors.put("phoneNumber", "Số điện thoại phải bắt đầu từ 0 (10 số)");
            }
        }

        return errors;
    }
}
