package org.fpoly.capstone.config;

import org.fpoly.capstone.entity.Brand;
import org.fpoly.capstone.entity.Category;
import org.fpoly.capstone.entity.Color;
import org.fpoly.capstone.entity.Material;
import org.fpoly.capstone.entity.Product;
import org.fpoly.capstone.entity.Size;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.enum_status.BrandStatus;
import org.fpoly.capstone.entity.enum_status.CategoryStatus;
import org.fpoly.capstone.entity.enum_status.ProductStatus;
import org.fpoly.capstone.entity.enum_status.UserRole;
import org.fpoly.capstone.entity.enum_status.UserStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
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

    public static final List<Brand> BRAND_LIST = List.of(
            new Brand(1L, "Nike", BrandStatus.DANG_SU_DUNG),
            new Brand(2L, "Adidas", BrandStatus.DANG_SU_DUNG),
            new Brand(3L, "Puma", BrandStatus.DANG_SU_DUNG),
            new Brand(4L, "Minzuno", BrandStatus.DANG_SU_DUNG),
            new Brand(5L, "Kamito", BrandStatus.DANG_SU_DUNG)
    );

    public static final List<Category> CATEGORY_LIST = List.of(
            new Category(1L, "Áo Premier League", CategoryStatus.DANG_SU_DUNG),
            new Category(2L, "Áo La Liga", CategoryStatus.DANG_SU_DUNG),
            new Category(3L, "Áo Serie A", CategoryStatus.DANG_SU_DUNG),
            new Category(4L, "Áo Ligue 1", CategoryStatus.DANG_SU_DUNG),
            new Category(5L, "Áo Bundesliga", CategoryStatus.DANG_SU_DUNG),
            new Category(6L, "Áo Khong su dung", CategoryStatus.NGUNG_SU_DUNG)
    );

    public static final List<Color> COLOR_LIST = List.of(
            new Color(1L, "Xanh", "blue"),
            new Color(2L, "Đỏ", "red"),
            new Color(3L, "Tím", "blueviolet"),
            new Color(4L, "Vàng", "yellow"),
            new Color(5L, "Lục", "green"),
            new Color(6L, "Xanh than", "darkblue"),
            new Color(7L, "Be", "khaki"),
            new Color(8L, "Xám", "gray"),
            new Color(9L, "Đen", "black"),
            new Color(10L, "Trắng", "white"),
            new Color(11L, "Cam", "orange")
    );

    public static final List<Material> MATERIAL_LIST = List.of(
            new Material(1L, "Thun thái cao cấp"),
            new Material(2L, "Thun lạnh cao cấp"),
            new Material(3L, "Cotton"),
            new Material(4L, "Polyeste"),
            new Material(5L, "Polycotton")
    );

    public static final List<Size> SIZE_LIST = List.of(
            new Size(1L, "S"),
            new Size(2L, "M"),
            new Size(3L, "L"),
            new Size(4L, "XL"),
            new Size(5L, "XXL")
    );

    public static final List<Product> PRODUCT_LIST = List.of(
            new Product(null, CATEGORY_LIST.get(0), "PRD001", "Áo Manchester United", ProductStatus.DANG_SU_DUNG, LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(5), "system", "system", null),
            new Product(null, CATEGORY_LIST.get(1), "PRD002", "Áo Real Madrid", ProductStatus.DANG_SU_DUNG, LocalDateTime.now().minusDays(20), LocalDateTime.now().minusDays(10), "system", "system", null),
            new Product(null, CATEGORY_LIST.get(2), "PRD003", "Áo AC Milan", ProductStatus.NGUNG_SU_DUNG, LocalDateTime.now().minusDays(30), LocalDateTime.now().minusDays(15), "system", "system", null),
            new Product(null, CATEGORY_LIST.get(3), "PRD004", "Áo PSG", ProductStatus.DANG_SU_DUNG, LocalDateTime.now().minusDays(40), LocalDateTime.now().minusDays(20), "system", "system", null),
            new Product(null, CATEGORY_LIST.get(4), "PRD005", "Áo Bayern Munich", ProductStatus.HET_SAN_PHAM, LocalDateTime.now().minusDays(50), LocalDateTime.now().minusDays(25), "system", "system", null),
            new Product(null, CATEGORY_LIST.get(0), "PRD006", "Áo Liverpool", ProductStatus.DANG_SU_DUNG, LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(2), "system", "system", null),
            new Product(null, CATEGORY_LIST.get(1), "PRD007", "Áo Barcelona", ProductStatus.DANG_SU_DUNG, LocalDateTime.now().minusDays(15), LocalDateTime.now().minusDays(7), "system", "system", null),
            new Product(null, CATEGORY_LIST.get(2), "PRD008", "Áo Juventus", ProductStatus.NGUNG_SU_DUNG, LocalDateTime.now().minusDays(25), LocalDateTime.now().minusDays(12), "system", "system", null),
            new Product(null, CATEGORY_LIST.get(3), "PRD009", "Áo Monaco", ProductStatus.DANG_SU_DUNG, LocalDateTime.now().minusDays(35), LocalDateTime.now().minusDays(17), "system", "system", null),
            new Product(null, CATEGORY_LIST.get(4), "PRD010", "Áo Dortmund", ProductStatus.HET_SAN_PHAM, LocalDateTime.now().minusDays(45), LocalDateTime.now().minusDays(22), "system", "system", null)
    );

}
