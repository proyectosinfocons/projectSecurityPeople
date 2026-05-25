package com.SecurityPeople.projectSecurityPeople.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    // 🔥 ESTA ES LA CLAVE
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Permitir raíz y recursos básicos
                        .requestMatchers("/", "/index", "/error", "/favicon.ico").permitAll()

                        // Permitir preflight requests de CORS (OPTIONS)
                        .requestMatchers("/api/usuarios/login").permitAll()
                        .requestMatchers("/api/usuarios/registro").permitAll()

                         .requestMatchers("/api/usuarios/enviar-codigo").permitAll()
                        .requestMatchers("/api/usuarios/verificar-codigo").permitAll()

                        .requestMatchers("/api/usuarios/recuperar").permitAll()
                        // =========================================================
                        // 🔥 INICIO CAMBIO: PERMITIR REPORTES
                        // =========================================================
                        .requestMatchers(HttpMethod.GET, "/api/reportes/**").authenticated()
                        // =========================================================
                        // 🔥 FIN CAMBIO
                        // =========================================================

                        // Todo lo demás protegido
                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable());
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
