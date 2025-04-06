package org.fpoly.capstone.config;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.repository.BrandRepository;
import org.fpoly.capstone.repository.CategoryRepository;
import org.fpoly.capstone.repository.ColorRepository;
import org.fpoly.capstone.repository.MaterialRepository;
import org.fpoly.capstone.repository.ProductRepository;
import org.fpoly.capstone.repository.SizeRepository;
import org.fpoly.capstone.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static org.fpoly.capstone.config.DataSeeder.*;

@Component
@RequiredArgsConstructor
@Getter
@Transactional
public class ApplicationStartupRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ColorRepository colorRepository;
    private final MaterialRepository materialRepository;
    private final SizeRepository sizeRepository;
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {

//        this.userRepository.saveAll(USER_LIST);
//        this.brandRepository.saveAll(BRAND_LIST);
//        this.categoryRepository.saveAll(CATEGORY_LIST);
//        this.colorRepository.saveAll(COLOR_LIST);
//        this.materialRepository.saveAll(MATERIAL_LIST);
//        this.sizeRepository.saveAll(SIZE_LIST);
//        this.productRepository.saveAll(PRODUCT_LIST);

    }
}
