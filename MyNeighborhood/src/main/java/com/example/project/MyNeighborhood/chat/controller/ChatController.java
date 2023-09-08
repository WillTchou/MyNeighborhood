package com.example.project.MyNeighborhood.chat.controller;

import com.example.project.MyNeighborhood.chat.model.ChatMessage;
import com.example.project.MyNeighborhood.chat.model.LatestFlow;
import com.example.project.MyNeighborhood.chat.repository.ChatMessageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageRepository chatMessageRepository;

    public ChatController(final SimpMessagingTemplate simpMessagingTemplate,
                          final ChatMessageRepository chatMessageRepository) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatMessageRepository = chatMessageRepository;
    }

    @GetMapping(path = "/api/v1/chat/flow/{recipientId}")
    public ResponseEntity<List<ChatMessage>> getChatFlow(@RequestHeader(name = "userId") final UUID senderId,
                                                         @PathVariable("recipientId") final UUID recipientId) {
        return ResponseEntity.ok(chatMessageRepository.findConversationBetweenSenderAndRecipient(senderId, recipientId));
    }

    @GetMapping(path = "/api/v1/chat/latest")
    public ResponseEntity<List<ChatMessage>> getLatestChatMessagesForSender(@RequestHeader(name = "userId") final UUID userId) {
        List<ChatMessage> results = new ArrayList<>();
        for (LatestFlow latestFlow : chatMessageRepository.findLatestFlowsForUser(userId)) {
            results.add(chatMessageRepository.findLatestChatMessages(latestFlow.getFlow(), latestFlow.getLatestDate()));
        }
        return ResponseEntity.ok(results);
    }

    @MessageMapping("/chat")
    @SendTo("/chatroom/public")
    public ChatMessage sendMessage(@Payload final ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/private-message")
    public ChatMessage sendPrivateMessage(@Payload final ChatMessage chatMessage) {
        final String recipientId = chatMessage.getRecipient().getId().toString();
        simpMessagingTemplate.convertAndSendToUser(recipientId,
                "/private",
                chatMessage);
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    @MessageMapping("/addUser")
    @SendTo("/chatroom/public")
    public ChatMessage addUser(@Payload final ChatMessage chatMessage,
                               final SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
