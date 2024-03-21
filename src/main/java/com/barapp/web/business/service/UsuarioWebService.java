package com.barapp.web.business.service;

import com.barapp.web.model.UsuarioWebDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsuarioWebService extends BaseService<UsuarioWebDto>, UserDetailsService {

}
