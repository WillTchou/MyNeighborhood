package com.example.project.MyNeighborhood.config;

import com.example.project.MyNeighborhood.chat.model.ChatMessage;
import com.example.project.MyNeighborhood.chat.model.MessageType;
import com.example.project.MyNeighborhood.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @EventListener
    public void handleWebSocketDisconnectListener(final SessionDisconnectEvent event) {
        final StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(event.getMessage());
        final User user = (User) stompHeaderAccessor.getSessionAttributes().get("username");
        if (user != null) {
            log.info("User disconnected: {}", user);
            ChatMessage chatMessage = ChatMessage.builder()
                    .messageType(MessageType.LEAVE)
                    .sender(user)
                    .build();
            simpMessageSendingOperations.convertAndSend("chatroom/public", chatMessage);
        }
    }
}
