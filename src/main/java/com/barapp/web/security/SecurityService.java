package com.barapp.web.security;

import com.vaadin.flow.spring.security.AuthenticationContext;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {

    private final AuthenticationContext authenticationContext;

    public SecurityService(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
    }

    public Optional<UserDetails> getAuthenticatedUser() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class);
    }
    
    public boolean isAuthenticated() {
        return authenticationContext.isAuthenticated();
    }

    public void logout() {
        authenticationContext.logout();
    }
}