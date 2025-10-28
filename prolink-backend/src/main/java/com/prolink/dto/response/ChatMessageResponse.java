package com.prolink.dto.response;

import com.prolink.entity.Message;
import com.prolink.entity.MessageType;
import com.prolink.entity.User;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para mensajes de chat
 */
public class ChatMessageResponse {

    private Long id;
    private String content;
    private MessageType type;
    private LocalDateTime timestamp;
    private Boolean isRead;
    
    // Información del remitente
    private Long senderId;
    private String senderUsername;
    private String senderFirstName;
    private String senderLastName;
    private String senderProfilePicture;
    
    // Información del destinatario
    private Long recipientId;
    private String recipientUsername;
    
    // Información de la conversación
    private Long conversationId;

    // Constructor vacío
    public ChatMessageResponse() {}

    /**
     * Factory method para crear ChatMessageResponse desde Message entity
     */
    public static ChatMessageResponse fromMessage(Message message) {
        ChatMessageResponse response = new ChatMessageResponse();
        
        response.setId(message.getId());
        response.setContent(message.getContent());
        response.setType(message.getType());
        response.setTimestamp(message.getCreatedAt());
        response.setIsRead(message.getIsRead());
        
        // Información del remitente
        User sender = message.getSender();
        if (sender != null) {
            response.setSenderId(sender.getId());
            response.setSenderUsername(sender.getUsername());
            response.setSenderFirstName(sender.getFirstName());
            response.setSenderLastName(sender.getLastName());
            response.setSenderProfilePicture(sender.getProfilePictureUrl());
        }
        
        // Información del destinatario
        User recipient = message.getReceiver();
        if (recipient != null) {
            response.setRecipientId(recipient.getId());
            response.setRecipientUsername(recipient.getUsername());
        }
        
        // Información de la conversación
        if (message.getConversation() != null) {
            response.setConversationId(message.getConversation().getId());
        }
        
        return response;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getSenderFirstName() {
        return senderFirstName;
    }

    public void setSenderFirstName(String senderFirstName) {
        this.senderFirstName = senderFirstName;
    }

    public String getSenderLastName() {
        return senderLastName;
    }

    public void setSenderLastName(String senderLastName) {
        this.senderLastName = senderLastName;
    }

    public String getSenderProfilePicture() {
        return senderProfilePicture;
    }

    public void setSenderProfilePicture(String senderProfilePicture) {
        this.senderProfilePicture = senderProfilePicture;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public String getRecipientUsername() {
        return recipientUsername;
    }

    public void setRecipientUsername(String recipientUsername) {
        this.recipientUsername = recipientUsername;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }
}