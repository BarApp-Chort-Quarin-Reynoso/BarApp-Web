package com.barapp.web.controllers;

import com.barapp.web.business.service.BaseService;
import com.barapp.web.business.service.UsuarioService;
import com.barapp.web.model.UsuarioApp;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/usuarios")
@CrossOrigin("*")
public class UsuarioRestController extends BaseController<UsuarioApp> {

    private final UsuarioService usuarioService;

    public UsuarioRestController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public BaseService<UsuarioApp> getService() {
        return usuarioService;
    }


}
