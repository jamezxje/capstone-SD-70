package org.fpoly.capstone.config;

import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.repository.CategoryRepository;
import org.fpoly.capstone.repository.ColorRepository;
import org.fpoly.capstone.repository.MaterialRepository;
import org.fpoly.capstone.repository.SizeRepository;
import org.fpoly.capstone.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static org.fpoly.capstone.config.DataSeeder.*;

@Component
@RequiredArgsConstructor
public class ApplicationStartupRunner implements CommandLineRunner {

  private final UserRepository userRepository;
  private final ColorRepository colorRepository;
  private final CategoryRepository categoryRepository;
  private final MaterialRepository materialRepository;
  private final SizeRepository sizeRepository;

  @Override
  public void run(String... args) throws Exception {

    this.userRepository.saveAll(USER_LIST);
    this.colorRepository.saveAll(COLOR_LIST);
    this.categoryRepository.saveAll(CATEGORY_LIST);
    this.materialRepository.saveAll(MATERIAL_LIST);
    this.sizeRepository.saveAll(SIZE_LIST);

  }
}
