package com.prolink.controller;

import com.prolink.dto.request.LoginRequest;
import com.prolink.dto.request.SignupRequest;
import com.prolink.dto.response.JwtResponse;
import com.prolink.dto.response.MessageResponse;
import com.prolink.entity.Role;
import com.prolink.entity.User;
import com.prolink.repository.UserRepository;
import com.prolink.security.JwtUtils;
import com.prolink.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Controlador REST para autenticación (login/registro)
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Endpoints para login, registro y manejo de JWT tokens")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * Endpoint de salud para probar conectividad
     */
    @GetMapping("/health")
    @Operation(summary = "Health Check", description = "Verifica que el controlador de autenticación esté funcionando")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Servicio funcionando correctamente",
            content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(new MessageResponse("Auth controller is working!"));
    }
    
    /**
     * Endpoint para login de usuarios
     */
    @PostMapping("/login")
    @Operation(summary = "Login de usuario", description = "Autentica usuario y devuelve JWT token")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Buscar usuario por username o email
            User user = userRepository.findByUsernameOrEmail(
                loginRequest.getUsernameOrEmail(), 
                loginRequest.getUsernameOrEmail()
            ).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            // Verificar que el usuario esté activo
            if (!user.isActive()) {
                return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Usuario inactivo. Contacte al administrador."));
            }
            
            // Autenticar
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    user.getUsername(), // Usar username para la autenticación
                    loginRequest.getPassword()
                )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            
            // Actualizar último login
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
            
            logger.info("Usuario autenticado exitosamente: {}", user.getUsername());
            
            return ResponseEntity.ok(new JwtResponse(
                jwt,
                userPrincipal.getId(),
                userPrincipal.getUsername(),
                userPrincipal.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().name()
            ));
            
        } catch (Exception e) {
            logger.error("Error en autenticación: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(MessageResponse.error("Credenciales inválidas"));
        }
    }
    
    /**
     * Endpoint para registro de nuevos usuarios
     */
    @PostMapping("/register")
    @Operation(summary = "Registro de usuario", description = "Registra nuevo usuario en el sistema")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        try {
            // Validar username único
            if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Error: Username ya está en uso"));
            }
            
            // Validar email único
            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Error: Email ya está registrado"));
            }
            
            // Crear nuevo usuario
            User user = new User();
            user.setUsername(signUpRequest.getUsername());
            user.setEmail(signUpRequest.getEmail());
            user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
            user.setFirstName(signUpRequest.getFirstName());
            user.setLastName(signUpRequest.getLastName());
            user.setProfessionalTitle(signUpRequest.getProfessionalTitle());
            user.setIndustry(signUpRequest.getIndustry());
            user.setLocation(signUpRequest.getLocation());
            
            // Asignar rol por defecto
            user.setRole(Role.USER);
            
            // Activar usuario por defecto
            user.setActive(true);
            
            // Establecer fechas
            LocalDateTime now = LocalDateTime.now();
            user.setCreatedAt(now);
            user.setUpdatedAt(now);
            
            userRepository.save(user);
            
            logger.info("Usuario registrado exitosamente: {}", user.getUsername());
            
            return ResponseEntity.ok(MessageResponse.success("Usuario registrado exitosamente"));
            
        } catch (Exception e) {
            logger.error("Error en registro: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(MessageResponse.error("Error en el registro: " + e.getMessage()));
        }
    }
    
    /**
     * Endpoint para verificar si username está disponible
     */
    @GetMapping("/check-username/{username}")
    @Operation(summary = "Verificar username", description = "Verifica si un username está disponible")
    public ResponseEntity<?> checkUsernameAvailability(@PathVariable("username") String username) {
        boolean isAvailable = !userRepository.existsByUsername(username);
        return ResponseEntity.ok(new UsernameAvailabilityResponse(isAvailable));
    }
    
    /**
     * Endpoint para verificar si email está disponible
     */
    @GetMapping("/check-email/{email}")
    @Operation(summary = "Verificar email", description = "Verifica si un email está disponible")
    public ResponseEntity<?> checkEmailAvailability(@PathVariable("email") String email) {
        boolean isAvailable = !userRepository.existsByEmail(email);
        return ResponseEntity.ok(new EmailAvailabilityResponse(isAvailable));
    }
    
    /**
     * DTO para respuesta de disponibilidad de username
     */
    public static class UsernameAvailabilityResponse {
        private boolean available;
        
        public UsernameAvailabilityResponse(boolean available) {
            this.available = available;
        }
        
        public boolean isAvailable() {
            return available;
        }
        
        public void setAvailable(boolean available) {
            this.available = available;
        }
    }
    
    /**
     * DTO para respuesta de disponibilidad de email
     */
    public static class EmailAvailabilityResponse {
        private boolean available;
        
        public EmailAvailabilityResponse(boolean available) {
            this.available = available;
        }
        
        public boolean isAvailable() {
            return available;
        }
        
        public void setAvailable(boolean available) {
            this.available = available;
        }
    }
}