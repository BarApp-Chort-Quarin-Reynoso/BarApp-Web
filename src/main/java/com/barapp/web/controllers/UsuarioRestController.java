package com.barapp.web.controllers;

import com.barapp.web.business.service.BaseService;
import com.barapp.web.business.service.UsuarioService;
import com.barapp.web.model.DetalleUsuario;
import com.barapp.web.model.RestauranteUsuario;
import com.barapp.web.model.UsuarioApp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/usuarios")
@CrossOrigin("*")
public class UsuarioRestController extends BaseController<UsuarioApp> {

    private final UsuarioService usuarioService;

    public UsuarioRestController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public BaseService<UsuarioApp> getService() {return usuarioService;}

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
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<DetalleUsuario> getUserDetail(@PathVariable String id) {
        try {
            Optional<DetalleUsuario> detalleUsuario = this.usuarioService.getUserDetail(id);
            if (!detalleUsuario.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(detalleUsuario.get(), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/detalle/{id}")
    public ResponseEntity<String> addUserDetail(@PathVariable String id, @RequestBody DetalleUsuario detalleUsuario) {
        try {
            return new ResponseEntity<>(this.usuarioService.addUserDetail(id, detalleUsuario), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/favoritos")
    public ResponseEntity<List<RestauranteUsuario>> getFavoritos(@PathVariable String id) {
        try {
            List<RestauranteUsuario> restaurantes = this.usuarioService.getFavoritos(id);
            return new ResponseEntity<>(restaurantes, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/vistos-recientemente")
    public ResponseEntity<List<RestauranteUsuario>> getVistosRecientemente(@PathVariable String id) {
        try {
            List<RestauranteUsuario> restaurantes = this.usuarioService.getVistosRecientemente(id);
            return new ResponseEntity<>(restaurantes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}/foto")
    public ResponseEntity<Void> updateFoto(@PathVariable String id, @RequestBody String foto) {
        try {
            this.usuarioService.updateFoto(id, foto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/detalle/{id}/busquedas-recientes")
    public ResponseEntity<Void> updateRecentSearches(@PathVariable String id, @RequestBody List<String> busquedasRecientes) {
        try {
            Optional<DetalleUsuario> detalleUsuario = this.usuarioService
                    .updateBusquedasRecientes(id, busquedasRecientes);
            if (!detalleUsuario.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<String> registrarUsuario(@RequestParam String mail, @RequestParam String contrasenia) {
        try {
            return new ResponseEntity<>(this.usuarioService.registrarUsuario(mail, contrasenia), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
