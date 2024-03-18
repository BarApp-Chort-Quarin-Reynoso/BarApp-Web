package com.barapp.web.model;

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
public class UsuarioWebDto extends BaseDto {
    private String email;
    private String hashedPassword;
    private Rol rol;
}
