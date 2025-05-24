package ru.pressstart9.petproject.api_ms.presentation.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {
    private JwtAuthEntryPoint authenticationEntryPoint;

    private JwtAuthFilter authenticationFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/login/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/people/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/pets/**").authenticated()

                        .requestMatchers(HttpMethod.POST, "/people/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/people/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/people/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/people/**").authenticated()

                        .requestMatchers(HttpMethod.POST, "/pets/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/pets/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/pets/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/pets/**").authenticated()

                        .anyRequest().denyAll()
                )

                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling( exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint))

                .build();
    }
}
