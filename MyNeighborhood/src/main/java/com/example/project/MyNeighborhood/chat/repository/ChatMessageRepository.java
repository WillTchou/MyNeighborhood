package com.example.project.MyNeighborhood.chat.repository;

import com.example.project.MyNeighborhood.chat.model.ChatMessage;
import com.example.project.MyNeighborhood.chat.model.LatestMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    String QUERY = "SELECT new com.example.project.MyNeighborhood.chat.model.LatestMessage(MAX(c.date) AS latestDate, c.content, c.sender, c.recipient) FROM ChatMessage c " +
            "WHERE c.sender.id = :userId OR c.recipient.id = :userId GROUP BY c.sender.id, c.recipient.id, content " +
            "ORDER BY latestDate DESC";

    @Query("SELECT c FROM ChatMessage c WHERE c.sender.id = :senderId AND c.recipient.id = :recipientId " +
            "ORDER BY c.date")
    List<ChatMessage> findConversationBetweenSenderAndRecipient(@Param("senderId") final UUID senderId,
                                                                @Param("recipientId") final UUID recipientId);

    @Query(QUERY)
    List<LatestMessage> findLatestChatMessagesForSender(@Param("userId") final UUID userId);
}
