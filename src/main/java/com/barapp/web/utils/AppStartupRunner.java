package com.barapp.web.utils;

import com.barapp.web.business.service.ReservaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(AppStartupRunner.class);

    private final ReservaService reservaService;

    public AppStartupRunner(ReservaService reservaService) {this.reservaService = reservaService;}

    @Override
    public void run(ApplicationArguments args) throws Exception {
        reservaService.inicializarNotificaciones();
    }
}
