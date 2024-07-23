package com.barapp.web.security;

import com.barapp.web.business.service.UsuarioWebService;
import com.barapp.web.views.login.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    private final UsuarioWebService usuarioWebService;

    public SecurityConfig(UsuarioWebService usuarioWebService) {
        this.usuarioWebService = usuarioWebService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/images/*.png"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/line-awesome/**/*.svg"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/fakeview/*"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/**"))
                    .permitAll());
        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
      CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
      http
        .csrf(AbstractHttpConfigurer::disable);
      return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
