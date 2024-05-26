package com.barapp.web.model;

import com.barapp.web.model.enums.Rol;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UsuarioWeb extends BaseModel {
    private String email;
    private String hashedPassword;
    private Rol rol;

    public UsuarioWeb(String email, String hashedPassword, Rol rol) {
        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.rol = rol;
    }
}
