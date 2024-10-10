package org.fpoly.capstone.config;

import org.fpoly.capstone.entity.Category;
import org.fpoly.capstone.entity.Color;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.type.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

public class DataSeeder {

  private static PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  public static final List<User> USER_LIST = List.of(
      new User(null, "Admin", "User1", "admin1@example.com", passwordEncoder().encode("adminPass1"), "1234567890", LocalDate.of(1990, 1, 1), null, Role.ROLE_ADMIN, null, true),
      new User(null, "Admin", "User2", "admin2@example.com", passwordEncoder().encode("adminPass2"), "0987654321", LocalDate.of(1985, 5, 15), null, Role.ROLE_ADMIN, null, true),
      new User(null, "Customer", "User3", "customer1@example.com", passwordEncoder().encode("customerPass1"), "1112223333", LocalDate.of(1995, 8, 20), null, Role.ROLE_CUSTOMER, null, true),
      new User(null, "Customer", "User4", "customer2@example.com", passwordEncoder().encode("customerPass2"), "4445556666", LocalDate.of(1992, 12, 10), null, Role.ROLE_CUSTOMER, null, true)
  );

  public static final List<Color> COLOR_LIST = List.of(
      new Color("red", true),
      new Color("blue", true),
      new Color("green", true),
      new Color("black", false)
  );

  public static final List<Category> CATEGORY_LIST = List.of(
      new Category("Polo", true),
      new Category("Crew Neck", true),
      new Category("Henly T-Shirts", true),
      new Category("Off Shoulder T-Shirts", false),
      new Category("Cowl", false)
  );

}
