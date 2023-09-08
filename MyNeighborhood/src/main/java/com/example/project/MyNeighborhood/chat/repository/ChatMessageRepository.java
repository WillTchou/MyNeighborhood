package com.example.project.MyNeighborhood.chat.repository;

import com.example.project.MyNeighborhood.chat.model.ChatMessage;
import com.example.project.MyNeighborhood.chat.model.LatestFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    String QUERY = "SELECT new com.example.project.MyNeighborhood.chat.model.LatestFlow(c.flow, MAX(c.date) AS latestDate) " +
            "FROM ChatMessage c WHERE c.sender.id = :userId OR c.recipient.id = :userId " +
            "GROUP BY c.flow ORDER BY latestDate DESC";

    @Query("SELECT c FROM ChatMessage c WHERE (c.sender.id = :userId1 AND c.recipient.id = :userId2) OR " +
            "(c.recipient.id = :userId1 AND c.sender.id = :userId2) ORDER BY c.date")
    List<ChatMessage> findConversationBetweenSenderAndRecipient(@Param("userId1") final UUID userId1,
                                                                @Param("userId2") final UUID userId2);

    @Query(QUERY)
    List<LatestFlow> findLatestFlowsForUser(@Param("userId") final UUID userId);

    @Query("SELECT c FROM ChatMessage c WHERE c.flow = :flow AND c.date = :date")
    ChatMessage findLatestChatMessages(@Param("flow") final String flow, @Param("date") final LocalDateTime date);
}
