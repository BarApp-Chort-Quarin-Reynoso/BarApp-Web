package com.barapp.web.data.impl;

import com.barapp.web.data.converter.BaseConverter;
import com.barapp.web.data.converter.UsuarioConverter;
import com.barapp.web.data.dao.UsuarioDao;
import com.barapp.web.data.entities.UsuarioEntity;
import com.barapp.web.model.UsuarioApp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDaoImpl extends BaseDaoImpl<UsuarioApp, UsuarioEntity> implements UsuarioDao {

    private final Firestore firestore;
    private final FirebaseAuth firebaseAuth;

    public UsuarioDaoImpl(Firestore firestore, FirebaseAuth firebaseAuth) {
        super(UsuarioEntity.class);

        this.firestore = firestore;
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public CollectionReference getCollection() {return firestore.collection("usuarios");}

    @Override
    public BaseConverter<UsuarioApp, UsuarioEntity> getConverter() {return new UsuarioConverter();}

    @Override
    public String registrarUsuario(String mail, String contrasenia) throws FirebaseAuthException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(mail)
                .setPassword(contrasenia);

        UserRecord userRecord = firebaseAuth.createUser(request);
        return userRecord.getUid();
    }
}
