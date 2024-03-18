package com.barapp.web.data.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioWebEntity extends BaseEntity {
    private String email;
    private String hashedPassword;
    private String rol;
}
