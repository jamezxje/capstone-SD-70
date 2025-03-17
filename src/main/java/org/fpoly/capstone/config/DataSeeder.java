package org.fpoly.capstone.config;

import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.enum_status.UserRole;
import org.fpoly.capstone.entity.enum_status.UserStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;

public class DataSeeder {

    private DataSeeder() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static final List<User> USER_LIST = List.of(

    );
}
