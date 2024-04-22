package com.barapp.web.business.service;

import com.barapp.web.model.UsuarioWeb;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UsuarioWebService extends BaseService<UsuarioWeb>, UserDetailsService {
    UsuarioWeb getByUsername(String username) throws UsernameNotFoundException;
}
