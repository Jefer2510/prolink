package com.prolink.controller;

import com.prolink.dto.request.ChatMessageRequest;
import com.prolink.dto.response.ChatMessageResponse;
import com.prolink.entity.Message;
import com.prolink.security.UserPrincipal;
import com.prolink.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import io.swagger.v3.oas.annotations.Hidden;

/**
 * Controlador WebSocket para mensajes de chat en tiempo real
 * Los endpoints WebSocket no aparecen en la documentación Swagger
 */
@Controller
@Hidden
public class WebSocketChatController {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketChatController.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;

    /**
     * Maneja mensajes privados entre usuarios
     * Endpoint: /app/chat.sendMessage
     */
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageRequest chatMessage, Authentication authentication) {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Long senderId = userPrincipal.getId();

            logger.info("Mensaje WebSocket recibido de usuario {} para usuario {}", 
                       senderId, chatMessage.getRecipientId());

            // Guardar mensaje en la base de datos
            Message savedMessage = messageService.sendMessage(
                senderId, 
                chatMessage.getRecipientId(), 
                chatMessage.getContent()
            );

            // Crear respuesta
            ChatMessageResponse response = ChatMessageResponse.fromMessage(savedMessage);

            // Enviar mensaje al destinatario específico
            messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId().toString(),
                "/queue/messages",
                response
            );

            // También enviar confirmación al remitente
            messagingTemplate.convertAndSendToUser(
                senderId.toString(),
                "/queue/messages",
                response
            );

            logger.info("Mensaje enviado exitosamente: {}", savedMessage.getId());

        } catch (Exception e) {
            logger.error("Error enviando mensaje WebSocket: {}", e.getMessage());
            
            // Enviar error al remitente
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            messagingTemplate.convertAndSendToUser(
                userPrincipal.getId().toString(),
                "/queue/errors",
                "Error enviando mensaje: " + e.getMessage()
            );
        }
    }

    /**
     * Maneja notificaciones de estado (typing, online, etc.)
     * Endpoint: /app/chat.typing
     */
    @MessageMapping("/chat.typing")
    public void handleTyping(@Payload ChatMessageRequest typingNotification, Authentication authentication) {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            
            // Enviar notificación de "escribiendo" al destinatario
            messagingTemplate.convertAndSendToUser(
                typingNotification.getRecipientId().toString(),
                "/queue/typing",
                "Usuario " + userPrincipal.getUsername() + " está escribiendo..."
            );

        } catch (Exception e) {
            logger.error("Error enviando notificación de typing: {}", e.getMessage());
        }
    }

    /**
     * Maneja conexión de usuarios
     * Endpoint: /app/chat.addUser  
     */
    @MessageMapping("/chat.addUser")
    public void addUser(@Payload String username, Authentication authentication) {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            
            logger.info("Usuario conectado al chat: {}", userPrincipal.getUsername());

            // Notificar a otros usuarios conectados (opcional)
            messagingTemplate.convertAndSend("/topic/public", 
                "Usuario " + userPrincipal.getUsername() + " se ha conectado");

        } catch (Exception e) {
            logger.error("Error conectando usuario al chat: {}", e.getMessage());
        }
    }

    /**
     * Marca mensajes como leídos
     * Endpoint: /app/chat.markRead
     */
    @MessageMapping("/chat.markRead")
    public void markMessagesAsRead(@Payload Long conversationId, Authentication authentication) {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            
            messageService.markMessagesAsRead(conversationId, userPrincipal.getId());
            
            logger.info("Mensajes marcados como leídos en conversación {}", conversationId);

        } catch (Exception e) {
            logger.error("Error marcando mensajes como leídos: {}", e.getMessage());
        }
    }
}