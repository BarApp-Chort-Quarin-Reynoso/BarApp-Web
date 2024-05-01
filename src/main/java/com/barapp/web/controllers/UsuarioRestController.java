package com.barapp.web.controllers;

import com.barapp.web.business.service.BaseService;
import com.barapp.web.business.service.UsuarioService;
import com.barapp.web.model.UsuarioApp;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/mail/{mail}")
    public ResponseEntity<UsuarioApp> getByMail(@PathVariable String mail) {
      try {
        Optional<UsuarioApp> usuario = this.usuarioService.getByMail(mail);
        if (!usuario.isPresent()) {
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
        // return new ResponseEntity<UsuarioApp>(this.usuarioService.getByCorreo(id), HttpStatus.OK);
      } catch (Exception e) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }


}
