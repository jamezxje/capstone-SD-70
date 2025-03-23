package org.fpoly.capstone.config;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static org.fpoly.capstone.config.DataSeeder.USER_LIST;

@Component
@RequiredArgsConstructor
@Getter
@Transactional
public class ApplicationStartupRunner implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        this.userRepository.saveAll(USER_LIST);

    }
}
