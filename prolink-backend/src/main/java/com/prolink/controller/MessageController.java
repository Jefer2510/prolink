package com.prolink.controller;

import com.prolink.dto.response.ChatMessageResponse;
import com.prolink.entity.Conversation;
import com.prolink.entity.Message;
import com.prolink.security.UserPrincipal;
import com.prolink.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

/**
 * Controlador REST para operaciones de mensajes
 * Complementa la funcionalidad WebSocket con endpoints HTTP
 */
@RestController
@RequestMapping("/messages")
@PreAuthorize("hasRole('USER')")
@Tag(name = "Mensajes", description = "Gestión de mensajes y conversaciones (REST API)")
@SecurityRequirement(name = "bearerAuth")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * Obtiene conversaciones del usuario actual
     */
    @GetMapping("/conversations")
    @Operation(summary = "Obtener conversaciones", description = "Obtiene todas las conversaciones del usuario autenticado con paginación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Conversaciones obtenidas exitosamente",
            content = @Content(schema = @Schema(type = "array", implementation = Conversation.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<List<Conversation>> getConversations(
            Authentication authentication,
            @RequestParam(defaultValue = "0") @Parameter(description = "Número de página", example = "0") int page,
            @RequestParam(defaultValue = "20") @Parameter(description = "Tamaño de página", example = "20") int size) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        List<Conversation> conversations = messageService.getUserConversations(
                userPrincipal.getId(), page, size);
        return ResponseEntity.ok(conversations);
    }

    /**
     * Obtiene mensajes de una conversación específica
     */
    @GetMapping("/conversations/{conversationId}")
    public ResponseEntity<List<ChatMessageResponse>> getConversationMessages(
            @PathVariable Long conversationId,
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        // Verificar acceso a la conversación
        messageService.getConversation(conversationId, userPrincipal.getId());
        
        List<Message> messages = messageService.getConversationMessages(
                conversationId, page, size);
        
        List<ChatMessageResponse> responses = messages.stream()
                .map(ChatMessageResponse::fromMessage)
                .toList();
        
        return ResponseEntity.ok(responses);
    }

    /**
     * Marca mensajes como leídos
     */
    @PutMapping("/conversations/{conversationId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long conversationId,
            Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        messageService.markMessagesAsRead(conversationId, userPrincipal.getId());
        return ResponseEntity.ok().build();
    }

    /**
     * Obtiene el número de mensajes no leídos
     */
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        long count = messageService.getUnreadMessagesCount(userPrincipal.getId());
        return ResponseEntity.ok(count);
    }

    /**
     * Busca mensajes por contenido
     */
    @GetMapping("/search")
    public ResponseEntity<List<ChatMessageResponse>> searchMessages(
            @RequestParam String query,
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        List<Message> messages = messageService.searchMessages(
                userPrincipal.getId(), query, page, size);
        
        List<ChatMessageResponse> responses = messages.stream()
                .map(ChatMessageResponse::fromMessage)
                .toList();
        
        return ResponseEntity.ok(responses);
    }

    /**
     * Elimina un mensaje
     */
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(
            @PathVariable Long messageId,
            Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        messageService.deleteMessage(messageId, userPrincipal.getId());
        return ResponseEntity.noContent().build();
    }
}