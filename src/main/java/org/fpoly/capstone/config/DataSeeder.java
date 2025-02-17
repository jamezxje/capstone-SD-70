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
            new User(
                    1L, "Admin One", new Date(90, 5, 20), "0123456789", "admin1@example.com",
                    true, "avatar1.jpg", "123456789", UserStatus.ACTIVATED,
                    passwordEncoder().encode("adminPass1"), UserRole.ROLE_ADMIN,
                    new Date(), new Date(), "system", "system", null
            ),
            new User(
                    2L, "Admin Two", new Date(88, 3, 15), "0987654321", "admin2@example.com",
                    false, "avatar2.jpg", "987654321", UserStatus.ACTIVATED,
                    passwordEncoder().encode("adminpass2"), UserRole.ROLE_ADMIN,
                    new Date(), new Date(), "system", "system", null
            ),
            new User(
                    3L, "Customer One", new Date(95, 1, 10), "0112233445", "customer1@example.com",
                    true, "avatar3.jpg", "111223344", UserStatus.ACTIVATED,
                    passwordEncoder().encode("customerpass1"), UserRole.ROLE_CUSTOMER,
                    new Date(), new Date(), "system", "system", null
            ),
            new User(
                    4L, "Customer Two", new Date(92, 8, 5), "0223344556", "customer2@example.com",
                    false, "avatar4.jpg", "222334455", UserStatus.ACTIVATED,
                    passwordEncoder().encode("customerpass2"), UserRole.ROLE_CUSTOMER,
                    new Date(), new Date(), "system", "system", null
            ),
            new User(
                    5L, "Customer Three", new Date(99, 10, 25), "0334455667", "customer3@example.com",
                    true, "avatar5.jpg", "333445566", UserStatus.ACTIVATED,
                    passwordEncoder().encode("customerpass3"), UserRole.ROLE_CUSTOMER,
                    new Date(), new Date(), "system", "system", null
            )
    );
}
