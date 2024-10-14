package org.fpoly.capstone.config;

import org.fpoly.capstone.entity.Category;
import org.fpoly.capstone.entity.Color;
import org.fpoly.capstone.entity.Image;
import org.fpoly.capstone.entity.Material;
import org.fpoly.capstone.entity.Product;
import org.fpoly.capstone.entity.ProductDetail;
import org.fpoly.capstone.entity.Size;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.type.ImageFormat;
import org.fpoly.capstone.entity.type.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class DataSeeder {

  private static PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  public static final List<User> USER_LIST = List.of(
      new User(null, "Admin", "ADMIN1", "admin1@example.com", passwordEncoder().encode("adminPass1"), "1234567890", LocalDate.of(1990, 1, 1), null, Role.ROLE_ADMIN, null, true),
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

  public static final List<Material> MATERIAL_LIST = List.of(
      new Material("Cotton", true),
      new Material("Polyester", true),
      new Material("Jersey", true),
      new Material("Pima Cotton", false),
      new Material("Rayon", false)
  );

  public static final List<Size> SIZE_LIST = List.of(
      new Size("S", true),
      new Size("M", true),
      new Size("L", true),
      new Size("XL", false),
      new Size("XXL", false)
  );

  public static final List<Image> IMAGE_LIST = List.of(
      new Image("7-series-exterior-right-front-three-quarter-3.webp", ImageFormat.WEBP, "uploads/7-series-exterior-right-front-three-quarter-3.webp", "img1_alt"),
      new Image("7-series-exterior-right-side-view.webp", ImageFormat.WEBP, "uploads/7-series-exterior-right-side-view.webp", "img2_alt"),
      new Image("new-7-series-exterior-rear-view.webp", ImageFormat.WEBP, "uploads/new-7-series-exterior-rear-view.webp", "img3_alt"),
      new Image("new-7-series-exterior-right-front-three-quarter.webp", ImageFormat.WEBP, "uploads/new-7-series-exterior-right-front-three-quarter.webp", "img4_alt"),
      new Image("fronx-exterior-right-front-three-quarter-109.webp", ImageFormat.WEBP, "uploads/fronx-exterior-right-front-three-quarter-109.webp", "img4_alt"),
      new Image("new-7-series-exterior-right-rear-three-quarter.webp", ImageFormat.WEBP, "uploads/new-7-series-exterior-right-rear-three-quarter.webp", "img5_alt"),
      new Image("s-class-exterior-right-front-three-quarter-8.webp", ImageFormat.WEBP, "uploads/s-class-exterior-right-front-three-quarter-8.webp", "img6_alt"),
      new Image("s-class-exterior-right-front-three-quarter-9.webp", ImageFormat.WEBP, "uploads/s-class-exterior-right-front-three-quarter-9.webp", "img7_alt"),
      new Image("Hyundai-Grand-i10-Nios-200120231541.jpg", ImageFormat.WEBP, "uploads/Hyundai-Grand-i10-Nios-200120231541.jpg", "img alt 9"),
      new Image("s-class-exterior-right-rear-three-quarter-2.webp", ImageFormat.WEBP, "uploads/s-class-exterior-right-rear-three-quarter-2.webp", "Rs. 1.82 - 1.85 Crore")

  );

  public static final List<Product> PRODUCT_LIST = List.of(
      new Product("P001", "Basic Polo", "200g", "High", IMAGE_LIST.get(0), "Classic Polo shirt", IMAGE_LIST.get(3), true),
      new Product("P002", "Crew Neck T-Shirt", "180g", "Medium", IMAGE_LIST.get(1), "Comfortable crew neck shirt", IMAGE_LIST.get(4), true),
      new Product("P003", "Henley T-Shirt", "220g", "Premium", IMAGE_LIST.get(2), "Henley shirt with buttons", IMAGE_LIST.get(5), true)
  );

  public static final List<ProductDetail> PRODUCT_DETAIL_LIST = List.of(
      new ProductDetail(PRODUCT_LIST.get(0), CATEGORY_LIST.get(0), COLOR_LIST.get(0), MATERIAL_LIST.get(0), SIZE_LIST.get(1), List.of(), 50L, new BigDecimal("19.99"), true),
      new ProductDetail(PRODUCT_LIST.get(1), CATEGORY_LIST.get(1), COLOR_LIST.get(1), MATERIAL_LIST.get(1), SIZE_LIST.get(2), List.of(), 100L, new BigDecimal("24.99"), true),
      new ProductDetail(PRODUCT_LIST.get(2), CATEGORY_LIST.get(2), COLOR_LIST.get(2), MATERIAL_LIST.get(2), SIZE_LIST.get(3), List.of(), 75L, new BigDecimal("29.99"), true)
  );

}
