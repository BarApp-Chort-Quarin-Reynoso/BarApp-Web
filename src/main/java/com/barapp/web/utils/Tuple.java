package com.barapp.web.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tuple<S, T> {
    S first;
    T second;

    public Tuple(S first, T second) {
        this.first = first;
        this.second = second;
    }

    public Tuple() {}
}
