package com.barapp.web.model;

public enum Rol {
    ADMIN, BAR;

    public String getGrantedAuthorityName() {
        return "ROLE_" + this;
    }
}
