package com.barapp.web.model;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseDto {
    protected String id;

    @Override
    public int hashCode() {
	return Objects.hash(id);
    }
    
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	BaseDto other = (BaseDto) obj;
	return Objects.equals(id, other.id);
    }
}
