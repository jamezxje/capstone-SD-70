package org.fpoly.capstone.constant;



public enum MessageError {
    PHONE_ERROR("Số điện thoại đã tồn tại"),
    EMAIL_ERROR("Email đã tồn tại"),
    EMAIL_NULL("Vui lòng nhập email!") ,
        PHONE_NULL("VUi lòng nhập số điện thoại!"),
    NAME_NULL("Vui lòng nhập họ tên!"),
    USER_NULL("Email không tồn tại"),
    BILL_NULL("Id bill không tồn tại");


    private final String message;
    MessageError(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}

