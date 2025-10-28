package com.prolink.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para enviar mensajes de chat
 */
public class ChatMessageRequest {

    @NotNull(message = "El ID del destinatario es requerido")
    private Long recipientId;

    @NotBlank(message = "El contenido del mensaje no puede estar vacío")
    private String content;

    private String type = "CHAT"; // CHAT, NOTIFICATION, SYSTEM

    // Constructor vacío
    public ChatMessageRequest() {}

    public ChatMessageRequest(Long recipientId, String content) {
        this.recipientId = recipientId;
        this.content = content;
    }

    // Getters y setters
    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}