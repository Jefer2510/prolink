package com.prolink.controller;

import com.prolink.dto.request.UpdateProfileRequest;
import com.prolink.dto.response.UserProfileResponse;
import com.prolink.entity.User;
import com.prolink.security.UserPrincipal;
import com.prolink.service.UserService;
import jakarta.validation.Valid;
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
 * Controlador REST para operaciones de usuarios
 */
@RestController
@RequestMapping("/users")
@Tag(name = "Usuarios", description = "Gestión de perfiles de usuario")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Obtiene el perfil del usuario autenticado
     */
    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Obtener perfil actual", description = "Obtiene la información del perfil del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfil obtenido exitosamente",
            content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<UserProfileResponse> getCurrentUserProfile(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userService.findById(userPrincipal.getId());
        
        UserProfileResponse profile = UserProfileResponse.fromUser(user);
        return ResponseEntity.ok(profile);
    }

    /**
     * Actualiza el perfil del usuario autenticado
     */
    @PutMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Actualizar perfil", description = "Actualiza la información del perfil del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<UserProfileResponse> updateProfile(
            @Valid @RequestBody @Parameter(description = "Datos actualizados del perfil") UpdateProfileRequest updateRequest,
            Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User updatedUser = userService.updateProfile(userPrincipal.getId(), updateRequest);
        
        UserProfileResponse profile = UserProfileResponse.fromUser(updatedUser);
        return ResponseEntity.ok(profile);
    }

    /**
     * Obtiene un perfil público de usuario por ID
     */
    @GetMapping("/{userId}")
    @Operation(summary = "Obtener perfil por ID", description = "Obtiene el perfil público de un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfil obtenido exitosamente",
            content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<UserProfileResponse> getUserById(
            @PathVariable @Parameter(description = "ID del usuario", example = "1") Long userId) {
        User user = userService.findById(userId);
        UserProfileResponse profile = UserProfileResponse.fromUser(user);
        return ResponseEntity.ok(profile);
    }

    /**
     * Busca usuarios por nombre o email
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Buscar usuarios", description = "Busca usuarios por nombre o email con paginación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente",
            content = @Content(schema = @Schema(type = "array", implementation = UserProfileResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<List<UserProfileResponse>> searchUsers(
            @RequestParam @Parameter(description = "Término de búsqueda", example = "John Doe") String query,
            @RequestParam(defaultValue = "0") @Parameter(description = "Número de página", example = "0") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "Tamaño de página", example = "10") int size) {
        List<User> users = userService.searchUsers(query, page, size);
        List<UserProfileResponse> profiles = users.stream()
                .map(UserProfileResponse::fromUser)
                .toList();
        return ResponseEntity.ok(profiles);
    }

    /**
     * Obtiene las sugerencias de conexión para el usuario autenticado
     */
    @GetMapping("/suggestions")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UserProfileResponse>> getConnectionSuggestions(
            Authentication authentication,
            @RequestParam(defaultValue = "10") int limit) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        List<User> suggestions = userService.getConnectionSuggestions(userPrincipal.getId(), limit);
        List<UserProfileResponse> profiles = suggestions.stream()
                .map(UserProfileResponse::fromUser)
                .toList();
        return ResponseEntity.ok(profiles);
    }
}