package com.trashcamp.trashcampbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Izinkan akses ke H2 Console tanpa autentikasi
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**", "/api/**").permitAll()
                        .anyRequest().authenticated()
                )
                // 2. Matikan CSRF secara penuh agar request API POST/PUT/DELETE dari frontend diizinkan
                .csrf(csrf -> csrf.disable())
                // 3. Izinkan penggunaan Frame (H2 Console menggunakan frame/layout terpisah)
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                // 4. Aktifkan Basic Auth (opsional untuk testing API nanti)
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults());

        return http.build();
    }
}