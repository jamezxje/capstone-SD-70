package org.fpoly.capstone.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean("messageSource")
    public MessageSource getMessageSource() {
        ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
        ms.setDefaultEncoding("utf-8");
        ms.setBasename("classpath:i18n/voucher");
        return ms;
    }

    protected static final String[] PUBLIC_UNAUTHENTICATION_URI = {
            "/",
            "/sale-counter/**",
            "/getAllBill",
            "/deleteBill/**",
            "/save-bill/**",
            "/save-product-bill/**",
            "/getAllProduct/**",
            "/getAllVoucher/**",
            "/payment-vnpay/**",
            "/vnpay-success/**",
            "/customerPage/**",
            "/createCustomerBill/**",
            "/searchCustomer/**",
            "/payment-success/**",
            "/sendInvoice/**",
            "/address-user/**",
            "/getMinimumBill/**",
            "/paymnet-success/**",
            "/delete-product-bill/**",
            "/products/**",
            "/getAllProductDetail",
            "/auth/register",
            "/hello",
            "/login",
            "/register",
            "/index",
            "/bill/**",
            "/bill/detail/**",
            "/change-status/**",
            "/updateCustomer-bill/**",
            "/getStatus-history/**",
            "/getInforBill/**",
            "/cancel-bill/**",
            "/search",
            "/find",
            "/css/**",
            "/js/**",
            "/images/**",
            "/img/**",
            "/uploads/**",
            "/details/**",
            "/shop/**",
            "/cart/**",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        requests -> requests
                                .requestMatchers(PUBLIC_UNAUTHENTICATION_URI).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/assets/**")).permitAll()
                                .requestMatchers("/shop/assets/**").permitAll()
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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
//
//    @Bean
//    public AuthenticationManagerBuilder authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//        return (AuthenticationManagerBuilder) authConfig.getAuthenticationManager();
//    }

//    @Bean
//    protected void configure(AuthenticationManagerBuilder auth)throws Exception{
//        auth.userDetailsService(userDetailsServiceCustom).passwordEncoder(passwordEncoder());
//    }
}
