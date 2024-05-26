package com.barapp.web.model.enums;

public enum Rol {
    ADMIN, BAR;

    public String getGrantedAuthorityName() {
        return "ROLE_" + this;
    }
}
