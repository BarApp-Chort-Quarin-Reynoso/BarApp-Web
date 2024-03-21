package com.barapp.web.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class UsuarioWebDto extends BaseModel {
    private String email;
    private String hashedPassword;
    private Rol rol;

    public UsuarioWebDto(String email, String hashedPassword, Rol rol) {
        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.rol = rol;
    }
}
