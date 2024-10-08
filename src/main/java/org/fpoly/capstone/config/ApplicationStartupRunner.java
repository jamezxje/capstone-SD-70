package org.fpoly.capstone.config;

import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.repository.ColorRepository;
import org.fpoly.capstone.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static org.fpoly.capstone.config.DataSeeder.COLOR_LIST;
import static org.fpoly.capstone.config.DataSeeder.USER_LIST;

@Component
@RequiredArgsConstructor
public class ApplicationStartupRunner implements CommandLineRunner {

  private final UserRepository userRepository;
  private final ColorRepository colorRepository;

  @Override
  public void run(String... args) throws Exception {

    this.userRepository.saveAll(USER_LIST);
    this.colorRepository.saveAll(COLOR_LIST);

  }
}
