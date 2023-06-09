package com.example.project.MyNeighborhood.request.model;

import lombok.Getter;

@Getter
public enum Type {
    OneTimeTask("One-time task"),
    MaterialNeed("Material need");

    private final String value;

    Type(final String value) {
        this.value = value;
    }
}
