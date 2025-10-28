package com.prolink.repository;

import com.prolink.entity.Conversation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para operaciones de Conversation
 */
@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    /**
     * Busca conversaciones de un usuario específico
     */
    @Query("SELECT c FROM Conversation c " +
           "WHERE c.participant1.id = :userId OR c.participant2.id = :userId " +
           "ORDER BY c.lastMessageAt DESC")
    List<Conversation> findUserConversations(@Param("userId") Long userId, Pageable pageable);

    /**
     * Busca conversación entre dos usuarios específicos
     */
    @Query("SELECT c FROM Conversation c " +
           "WHERE (c.participant1.id = :userId1 AND c.participant2.id = :userId2) " +
           "OR (c.participant1.id = :userId2 AND c.participant2.id = :userId1)")
    Optional<Conversation> findConversationBetweenUsers(
            @Param("userId1") Long userId1, 
            @Param("userId2") Long userId2);

    /**
     * Cuenta conversaciones activas de un usuario
     */
    @Query("SELECT COUNT(c) FROM Conversation c " +
           "WHERE c.participant1.id = :userId OR c.participant2.id = :userId")
    long countUserConversations(@Param("userId") Long userId);

    /**
     * Busca conversaciones con mensajes no leídos
     */
    @Query("SELECT DISTINCT c FROM Conversation c " +
           "JOIN Message m ON m.conversation.id = c.id " +
           "WHERE (c.participant1.id = :userId OR c.participant2.id = :userId) " +
           "AND m.receiver.id = :userId " +
           "AND m.isRead = false " +
           "ORDER BY c.lastMessageAt DESC")
    List<Conversation> findConversationsWithUnreadMessages(@Param("userId") Long userId);

    /**
     * Busca conversaciones por nombre de participante
     */
    @Query("SELECT c FROM Conversation c " +
           "WHERE (c.participant1.id = :userId OR c.participant2.id = :userId) " +
           "AND (LOWER(c.participant1.firstName) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "OR LOWER(c.participant1.lastName) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "OR LOWER(c.participant2.firstName) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "OR LOWER(c.participant2.lastName) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "ORDER BY c.lastMessageAt DESC")
    List<Conversation> findConversationsByParticipantName(
            @Param("userId") Long userId, 
            @Param("name") String name, 
            Pageable pageable);

    /**
     * Elimina conversaciones de un usuario
     */
    @Query("DELETE FROM Conversation c " +
           "WHERE c.participant1.id = :userId OR c.participant2.id = :userId")
    void deleteUserConversations(@Param("userId") Long userId);
}