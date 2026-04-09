package com.saijyothi.bookstore.config;

import com.saijyothi.bookstore.entity.AppUser;
import com.saijyothi.bookstore.entity.UserRole;
import com.saijyothi.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminBootstrapConfig {

    @Bean
    CommandLineRunner bootstrapAdminUser(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.name}") String adminName,
            @Value("${app.admin.email}") String adminEmail,
            @Value("${app.admin.password}") String adminPassword) {
        return args -> {
            String normalizedEmail = adminEmail.trim().toLowerCase();
            if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
                return;
            }

            AppUser admin = new AppUser();
            admin.setName(adminName.trim());
            admin.setEmail(normalizedEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword.trim()));
            admin.setRole(UserRole.ROLE_ADMIN);
            userRepository.save(admin);
        };
    }
}
