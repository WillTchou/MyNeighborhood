package com.example.project.MyNeighborhood.chat.model;

import lombok.Getter;

@Getter
public enum MessageType {
    CHAT("CHAT"),
    JOIN("JOIN"),
    LEAVE("LEAVE");

    private final String value;

    MessageType(final String value) {
        this.value = value;
    }
}
