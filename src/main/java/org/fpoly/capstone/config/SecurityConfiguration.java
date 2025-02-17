package org.fpoly.capstone.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    protected static final String[] PUBLIC_UNAUTHENTICATION_URI = {
            "/",
            "/auth/register",
            "/home",
            "/login",
            "/register",
            "/index",
            "/search",
            "/find",
            "/css/**",
            "/js/**",
            "/images/**",
            "/img/**",
            "/uploads/**",
            "/details/**",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        requests -> requests
                                .requestMatchers(PUBLIC_UNAUTHENTICATION_URI).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/assets/**")).permitAll()
                                .requestMatchers("/dashboard/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin(
                        form -> form
                                .loginPage("/auth/login")
                                .defaultSuccessUrl("/hello", true)
                                .permitAll()
                )
                .logout(
                        logout -> logout
                                .logoutSuccessUrl("/auth/login")
                                .invalidateHttpSession(true)
                                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "GET"))
                                .permitAll()
                );

        return http.build();

    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
