package com.barapp.web.data.entities;

import lombok.*;

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
