package com.prolink.config;

import com.prolink.security.JwtUtils;
import com.prolink.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuración de WebSocket para chat en tiempo real
 * Habilita STOMP sobre WebSocket para mensajería
 */
@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * Configuración del message broker
     * Define los prefijos de destino para envío y suscripción
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Habilita un broker simple en memoria para enviar mensajes
        config.enableSimpleBroker("/topic", "/queue", "/user");
        
        // Prefijo para mensajes del cliente al servidor
        config.setApplicationDestinationPrefixes("/app");
        
        // Prefijo para mensajes específicos del usuario
        config.setUserDestinationPrefix("/user");
    }

    /**
     * Registra los endpoints STOMP para conexión WebSocket
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint principal para conexiones WebSocket
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS(); // Fallback para navegadores que no soportan WebSocket
        
        // Endpoint adicional sin SockJS
        registry.addEndpoint("/websocket")
                .setAllowedOriginPatterns("*");
    }

    /**
     * Configuración de interceptores de canal para autenticación JWT
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = 
                    MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    // Obtener token JWT del header
                    String authToken = accessor.getFirstNativeHeader("Authorization");
                    
                    if (StringUtils.hasText(authToken) && authToken.startsWith("Bearer ")) {
                        String jwt = authToken.substring(7);
                        
                        try {
                            if (jwtUtils.validateJwtToken(jwt)) {
                                String username = jwtUtils.getUsernameFromJwtToken(jwt);
                                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                                
                                UsernamePasswordAuthenticationToken authentication = 
                                    new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities());
                                
                                accessor.setUser(authentication);
                            }
                        } catch (Exception e) {
                            System.err.println("Error autenticando WebSocket: " + e.getMessage());
                        }
                    }
                }
                
                return message;
            }
        });
    }
}