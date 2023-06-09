package com.example.project.MyNeighborhood.chat.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatNotification {
    private long id;
    private UUID senderId;
    private UUID recipientId;
}
