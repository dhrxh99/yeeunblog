package com.yeeun.yeeunblog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 이 클래스 안에서 스프링 빈을 등록한다는 표시
public class SecurityConfig {

    @Bean // 이 메서드의 리턴값을 스프링 컨테이너에 등록함
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        http
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/signup", "/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("logout")
                        .logoutSuccessUrl("logout?logout")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 해시
    }

}
