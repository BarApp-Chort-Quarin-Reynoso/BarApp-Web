package com.barapp.web.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barapp.web.business.service.BaseService;
import com.barapp.web.business.service.UsuarioService;
import com.barapp.web.model.UsuarioDto;

@RestController
@RequestMapping(value = "/api/usuarios")
@CrossOrigin("*")
public class UsuarioRestController extends BaseController<UsuarioDto> {
    
    private final UsuarioService usuarioService;
    
    public UsuarioRestController(UsuarioService usuarioService) {
	this.usuarioService = usuarioService;
    }
    
    @Override
    public BaseService<UsuarioDto> getService() {
        return usuarioService;
    }
    

}
