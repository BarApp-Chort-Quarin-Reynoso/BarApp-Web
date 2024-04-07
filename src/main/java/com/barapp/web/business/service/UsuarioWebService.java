package com.barapp.web.business.service;

import com.barapp.web.model.UsuarioWebDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UsuarioWebService extends BaseService<UsuarioWebDto>, UserDetailsService {
    UsuarioWebDto getByUsername(String username) throws UsernameNotFoundException;
}
