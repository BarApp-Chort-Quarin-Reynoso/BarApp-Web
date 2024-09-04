package com.barapp.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class LoginListener implements ApplicationListener<AbstractAuthenticationEvent> {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onApplicationEvent(AbstractAuthenticationEvent abstractAuthenticationEvent) {

        String username = abstractAuthenticationEvent.getAuthentication().getName();

        if (abstractAuthenticationEvent instanceof AbstractAuthenticationFailureEvent event) {
            logger.warn("Invalid login detected with username '{}'. '{}'", username, event.getException().toString());

        } else if (abstractAuthenticationEvent instanceof AuthenticationSuccessEvent) {
            logger.info("User '{}' logged in successfully", username);
        }
    }
}
