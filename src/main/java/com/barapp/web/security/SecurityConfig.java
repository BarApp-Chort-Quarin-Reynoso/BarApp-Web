package com.barapp.web.security;

import com.barapp.web.business.service.UsuarioWebService;
import com.barapp.web.views.login.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
                    .permitAll());
        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
