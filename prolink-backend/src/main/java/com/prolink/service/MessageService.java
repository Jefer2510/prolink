package com.prolink.service;

import com.prolink.entity.*;
import com.prolink.exception.ResourceNotFoundException;
import com.prolink.repository.ConversationRepository;
import com.prolink.repository.MessageRepository;
import com.prolink.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para operaciones de mensajes y conversaciones
 */
@Service
@Transactional
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Envía un mensaje entre dos usuarios
     */
    public Message sendMessage(Long senderId, Long recipientId, String content) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("Remitente no encontrado"));

        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new ResourceNotFoundException("Destinatario no encontrado"));

        // Buscar o crear conversación
        Conversation conversation = findOrCreateConversation(sender, recipient);

        // Crear mensaje
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(recipient);
        message.setConversation(conversation);
        message.setContent(content);
        message.setType(MessageType.TEXT);
        message.setIsRead(false);
        message.setCreatedAt(LocalDateTime.now());

        Message savedMessage = messageRepository.save(message);

        // Actualizar última actividad de la conversación
        conversation.setLastMessageAt(LocalDateTime.now());
        conversationRepository.save(conversation);

        return savedMessage;
    }

    /**
     * Obtiene mensajes de una conversación
     */
    public List<Message> getConversationMessages(Long conversationId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return messageRepository.findByConversationIdOrderByCreatedAtDesc(conversationId, pageable);
    }

    /**
     * Obtiene conversaciones de un usuario
     */
    public List<Conversation> getUserConversations(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("lastMessageAt").descending());
        return conversationRepository.findUserConversations(userId, pageable);
    }

    /**
     * Marca mensajes como leídos
     */
    public void markMessagesAsRead(Long conversationId, Long userId) {
        List<Message> unreadMessages = messageRepository
                .findUnreadMessagesByConversationAndRecipient(conversationId, userId);

        for (Message message : unreadMessages) {
            message.setIsRead(true);
        }

        messageRepository.saveAll(unreadMessages);
    }

    /**
     * Busca o crea una conversación entre dos usuarios
     */
    private Conversation findOrCreateConversation(User user1, User user2) {
        Optional<Conversation> existingConversation = conversationRepository
                .findConversationBetweenUsers(user1.getId(), user2.getId());

        if (existingConversation.isPresent()) {
            return existingConversation.get();
        }

        // Crear nueva conversación
        Conversation conversation = new Conversation();
        conversation.setParticipant1(user1);
        conversation.setParticipant2(user2);
        conversation.setCreatedAt(LocalDateTime.now());
        conversation.setLastMessageAt(LocalDateTime.now());

        return conversationRepository.save(conversation);
    }

    /**
     * Obtiene el número de mensajes no leídos para un usuario
     */
    public long getUnreadMessagesCount(Long userId) {
        return messageRepository.countUnreadMessagesByRecipient(userId);
    }

    /**
     * Busca mensajes por contenido
     */
    public List<Message> searchMessages(Long userId, String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return messageRepository.searchUserMessages(userId, query, pageable);
    }

    /**
     * Elimina un mensaje (soft delete)
     */
    public void deleteMessage(Long messageId, Long userId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Mensaje no encontrado"));

        // Verificar que el usuario sea el remitente del mensaje
        if (!message.getSender().getId().equals(userId)) {
            throw new RuntimeException("No tienes permisos para eliminar este mensaje");
        }

        // Soft delete - marcar como eliminado
        message.setContent("[Mensaje eliminado]");
        message.setType(MessageType.DELETED);
        messageRepository.save(message);
    }

    /**
     * Obtiene una conversación específica
     */
    public Conversation getConversation(Long conversationId, Long userId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversación no encontrada"));

        // Verificar que el usuario sea participante de la conversación
        if (!conversation.getParticipant1().getId().equals(userId) && 
            !conversation.getParticipant2().getId().equals(userId)) {
            throw new RuntimeException("No tienes acceso a esta conversación");
        }

        return conversation;
    }
}