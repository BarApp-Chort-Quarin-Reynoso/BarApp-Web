package com.barapp.web.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class BaseModel {
    @Builder.Default
    protected String id = UUID.randomUUID().toString();

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        BaseModel other = (BaseModel) obj;
        return Objects.equals(id, other.id);
    }
}
