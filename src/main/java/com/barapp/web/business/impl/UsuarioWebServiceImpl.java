package com.barapp.web.business.impl;

import com.barapp.web.business.ImageContainer;
import com.barapp.web.business.service.UsuarioWebService;
import com.barapp.web.data.dao.BaseDao;
import com.barapp.web.data.dao.ImageDao;
import com.barapp.web.data.dao.UsuarioWebDao;
import com.barapp.web.data.entities.BaseEntity;
import com.barapp.web.model.UsuarioWeb;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class UsuarioWebServiceImpl extends BaseServiceImpl<UsuarioWeb> implements UsuarioWebService {

    private final UsuarioWebDao usuarioWebDao;

    public UsuarioWebServiceImpl(UsuarioWebDao usuarioWebDao) {
        this.usuarioWebDao = usuarioWebDao;
    }

    @Override
    public BaseDao<UsuarioWeb, ? extends BaseEntity> getDao() throws Exception { return usuarioWebDao; }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UsuarioWeb> userOpt = usuarioWebDao.findByEmail(username);
        UsuarioWeb user = userOpt
            .orElseThrow(() -> new UsernameNotFoundException("No user present with username: " + username));

        return new User(user.getEmail(), user.getHashedPassword(), getAuthorities(user));
    }

    @Override
    public UsuarioWeb getByUsername(String username) throws UsernameNotFoundException {
        Optional<UsuarioWeb> userOpt = usuarioWebDao.findByEmail(username);
        return userOpt
            .orElseThrow(() -> new UsernameNotFoundException("No user present with username: " + username));
    }

    private static List<SimpleGrantedAuthority> getAuthorities(UsuarioWeb user) {
        return List.of(new SimpleGrantedAuthority(user.getRol().getGrantedAuthorityName()));
    }

}
