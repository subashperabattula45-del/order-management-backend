package com.subash.ordermanagement.config;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.http.HttpMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

        @Value("${jwt.secret}")
        private String jwtSecret;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http)
                        throws Exception {

                http
                                .csrf(csrf -> csrf.disable())

                                .authorizeHttpRequests(auth -> auth

                                                .requestMatchers(
                                                                "/api/auth/**",
                                                                "/swagger-ui/**",
                                                                "/v3/api-docs/**")
                                                .permitAll()

                                                .requestMatchers(HttpMethod.POST, "/api/users")
                                                .permitAll()

                                                .requestMatchers(
                                                                HttpMethod.POST,
                                                                "/api/products/**")
                                                .hasRole("ADMIN")

                                                .requestMatchers(
                                                                HttpMethod.PUT,
                                                                "/api/products/**")
                                                .hasRole("ADMIN")

                                                .requestMatchers(
                                                                HttpMethod.DELETE,
                                                                "/api/products/**")
                                                .hasRole("ADMIN")

                                                .requestMatchers(
                                                                HttpMethod.GET,
                                                                "/api/products/**")
                                                .hasAnyRole("USER", "ADMIN")

                                                .requestMatchers(HttpMethod.POST, "/api/promos/**")
                                                .hasRole("ADMIN")

                                                .requestMatchers(HttpMethod.PUT, "/api/promos/**")
                                                .hasRole("ADMIN")

                                                .requestMatchers(HttpMethod.DELETE, "/api/promos/**")
                                                .hasRole("ADMIN")

                                                .requestMatchers(HttpMethod.GET, "/api/promos/**")
                                                .hasAnyRole("USER", "ADMIN")

                                                .requestMatchers(HttpMethod.POST, "/api/orders")
                                                .hasAnyRole("USER", "ADMIN")

                                                .requestMatchers(HttpMethod.PUT, "/api/orders/*/status")
                                                .hasRole("ADMIN")

                                                .requestMatchers(HttpMethod.GET, "/api/orders")
                                                .hasRole("ADMIN")

                                                .requestMatchers(HttpMethod.GET, "/api/orders/**")
                                                .hasAnyRole("USER", "ADMIN")

                                                .requestMatchers(
                                                                HttpMethod.PUT,
                                                                "/api/payments/*/status")
                                                .hasRole("ADMIN")

                                                .requestMatchers(
                                                                HttpMethod.GET,
                                                                "/api/payments/order/**")
                                                .hasRole("ADMIN")

                                                .anyRequest()
                                                .authenticated())

                                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(
                                                jwtAuthenticationConverter())));

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecretKey jwtSecretKey() {
                return new SecretKeySpec(
                                jwtSecret.getBytes(StandardCharsets.UTF_8),
                                "HmacSHA256");
        }

        @Bean
        public JwtEncoder jwtEncoder(SecretKey secretKey) {
                return NimbusJwtEncoder.withSecretKey(secretKey).build();
        }

        @Bean
        public JwtDecoder jwtDecoder(SecretKey secretKey) {
                return NimbusJwtDecoder.withSecretKey(secretKey).build();
        }

        @Bean
        public JwtAuthenticationConverter jwtAuthenticationConverter() {

                JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();

                authoritiesConverter.setAuthoritiesClaimName("role");
                authoritiesConverter.setAuthorityPrefix("ROLE_");

                JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();

                authenticationConverter.setJwtGrantedAuthoritiesConverter(
                                authoritiesConverter);

                return authenticationConverter;
        }

}
