package com.barapp.web.business.impl;

import com.barapp.web.business.service.UsuarioWebService;
import com.barapp.web.data.dao.BaseDao;
import com.barapp.web.data.dao.UsuarioWebDao;
import com.barapp.web.data.entities.BaseEntity;
import com.barapp.web.model.UsuarioWebDto;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioWebServiceImpl extends BaseServiceImpl<UsuarioWebDto> implements UsuarioWebService {

    private final UsuarioWebDao usuarioWebDao;

    public UsuarioWebServiceImpl(UsuarioWebDao usuarioWebDao) {
        this.usuarioWebDao = usuarioWebDao;
    }

    @Override
    public BaseDao<UsuarioWebDto, ? extends BaseEntity> getDao() throws Exception {
        return usuarioWebDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UsuarioWebDto> userOpt = usuarioWebDao.findByEmail(username);
        UsuarioWebDto user = userOpt.orElseThrow(() -> new UsernameNotFoundException("No user present with username: " + username));

        return new User(user.getEmail(), user.getHashedPassword(), getAuthorities(user));
    }

    private static List<SimpleGrantedAuthority> getAuthorities(UsuarioWebDto user) {
        return List.of(new SimpleGrantedAuthority(user.getRol().getGrantedAuthorityName()));
    }

}
