package com.prolink.controller;

import com.prolink.dto.request.ConnectionRequest;
import com.prolink.dto.response.ConnectionResponse;
import com.prolink.dto.response.UserProfileResponse;
import com.prolink.entity.User;
import com.prolink.entity.Connection;
import com.prolink.security.UserPrincipal;
import com.prolink.service.ConnectionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
 * Controlador REST para operaciones de conexiones
 */
@RestController
@RequestMapping("/connections")
@PreAuthorize("hasRole('USER')")
@Tag(name = "Conexiones", description = "Gestión de conexiones entre usuarios")
@SecurityRequirement(name = "bearerAuth")
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;

    /**
     * Envía una solicitud de conexión
     */
    @PostMapping("/request")
    @Operation(summary = "Enviar solicitud de conexión", description = "Envía una solicitud de conexión a otro usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud enviada exitosamente",
            content = @Content(schema = @Schema(implementation = ConnectionResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o solicitud ya existe"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<ConnectionResponse> sendConnectionRequest(
            @Valid @RequestBody @Parameter(description = "Solicitud de conexión") ConnectionRequest connectionRequest,
            Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Connection connection = connectionService.sendConnectionRequest(
                userPrincipal.getId(), 
                connectionRequest.getAddresseeId()
        );
        
        ConnectionResponse response = ConnectionResponse.fromConnection(connection);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Acepta una solicitud de conexión
     */
    @PutMapping("/{connectionId}/accept")
    public ResponseEntity<ConnectionResponse> acceptConnectionRequest(
            @PathVariable Long connectionId,
            Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Connection connection = connectionService.acceptConnectionRequest(
                connectionId, 
                userPrincipal.getId()
        );
        
        ConnectionResponse response = ConnectionResponse.fromConnection(connection);
        return ResponseEntity.ok(response);
    }

    /**
     * Rechaza una solicitud de conexión
     */
    @PutMapping("/{connectionId}/reject")
    public ResponseEntity<ConnectionResponse> rejectConnectionRequest(
            @PathVariable Long connectionId,
            Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Connection connection = connectionService.rejectConnectionRequest(
                connectionId, 
                userPrincipal.getId()
        );
        
        ConnectionResponse response = ConnectionResponse.fromConnection(connection);
        return ResponseEntity.ok(response);
    }

    /**
     * Cancela una conexión existente
     */
    @DeleteMapping("/{connectionId}")
    public ResponseEntity<Void> cancelConnection(
            @PathVariable Long connectionId,
            Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        connectionService.cancelConnection(connectionId, userPrincipal.getId());
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene todas las conexiones del usuario actual
     */
    @GetMapping
    public ResponseEntity<List<ConnectionResponse>> getMyConnections(
            Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        List<Connection> connections = connectionService.getUserConnections(userPrincipal.getId());
        
        List<ConnectionResponse> responses = connections.stream()
                .map(ConnectionResponse::fromConnection)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Obtiene solicitudes de conexión pendientes (recibidas)
     */
    @GetMapping("/pending/received")
    public ResponseEntity<List<ConnectionResponse>> getPendingReceivedRequests(
            Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        List<Connection> connections = connectionService.getPendingReceivedRequests(userPrincipal.getId());
        
        List<ConnectionResponse> responses = connections.stream()
                .map(ConnectionResponse::fromConnection)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Obtiene solicitudes de conexión pendientes (enviadas)
     */
    @GetMapping("/pending/sent")
    public ResponseEntity<List<ConnectionResponse>> getPendingSentRequests(
            Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        List<Connection> connections = connectionService.getPendingSentRequests(userPrincipal.getId());
        
        List<ConnectionResponse> responses = connections.stream()
                .map(ConnectionResponse::fromConnection)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Obtiene el estado de conexión con un usuario específico
     */
    @GetMapping("/status/{userId}")
    public ResponseEntity<String> getConnectionStatus(
            @PathVariable Long userId,
            Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String status = connectionService.getConnectionStatus(userPrincipal.getId(), userId);
        return ResponseEntity.ok(status);
    }

    /**
     * Obtiene sugerencias de usuarios para conectar
     */
    @GetMapping("/suggestions")
    public ResponseEntity<List<UserProfileResponse>> getUserSuggestions(
            Authentication authentication,
            @RequestParam(defaultValue = "10") int limit) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        List<User> suggestions = connectionService.getUserSuggestions(
                userPrincipal.getId(), 
                limit
        );
        
        List<UserProfileResponse> responses = suggestions.stream()
                .map(UserProfileResponse::fromUser)
                .toList();
        return ResponseEntity.ok(responses);
    }
}