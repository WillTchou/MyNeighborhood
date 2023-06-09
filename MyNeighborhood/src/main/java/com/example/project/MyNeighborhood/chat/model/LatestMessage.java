package com.example.project.MyNeighborhood.chat.model;

import com.example.project.MyNeighborhood.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class LatestMessage {
    LocalDateTime latestDate;
    String content;
    User sender;
    User recipient;
}
