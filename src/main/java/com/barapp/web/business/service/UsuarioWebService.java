package com.barapp.web.business.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.barapp.web.model.UsuarioWebDto;

public interface UsuarioWebService extends BaseService<UsuarioWebDto>, UserDetailsService {

}
