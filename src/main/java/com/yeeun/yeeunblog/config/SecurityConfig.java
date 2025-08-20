//package com.yeeun.yeeunblog.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http
//                .authorizeHttpRequests(a -> a.requestMatchers("/api/**").permitAll()
//                        .anyRequest().permitAll())
//                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
//                .cors(c -> {})
//                .formLogin(f -> f.disable())
//                .httpBasic(b -> b.disable());
//
//        return http.build();
//    }
//}
