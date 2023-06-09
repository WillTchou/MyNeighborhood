package com.example.project.MyNeighborhood.request.model;

import lombok.Getter;

@Getter
public enum Status {
    Fulfilled("Fulfilled"),
    Unfulfilled("Unfulfilled");

    private final String label;

    Status(final String label) {
        this.label = label;
    }
}
