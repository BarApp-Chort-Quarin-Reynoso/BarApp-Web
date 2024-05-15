package com.barapp.web.security;

import com.barapp.web.views.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.WebStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

        if (abstractAuthenticationEvent instanceof AbstractAuthenticationFailureEvent) {
            logger.warn("Invalid login detected with username '{}'", username);

        } else if (abstractAuthenticationEvent instanceof AuthenticationSuccessEvent) {
            logger.info("User '{}' logged in successfully", username);
        }
    }
}
