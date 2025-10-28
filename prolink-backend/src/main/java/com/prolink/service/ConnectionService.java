package com.prolink.service;

import com.prolink.entity.Connection;
import com.prolink.entity.ConnectionStatus;
import com.prolink.entity.User;
import com.prolink.exception.ResourceNotFoundException;
import com.prolink.repository.ConnectionRepository;
import com.prolink.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para operaciones de conexiones
 */
@Service
@Transactional
public class ConnectionService {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Envía una solicitud de conexión
     */
    public Connection sendConnectionRequest(Long requesterId, Long addresseeId) {
        // Validaciones
        if (requesterId.equals(addresseeId)) {
            throw new IllegalArgumentException("No puedes enviarte una solicitud de conexión a ti mismo");
        }

        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario solicitante no encontrado"));

        User addressee = userRepository.findById(addresseeId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario destinatario no encontrado"));

        // Verificar si ya existe una conexión
        Optional<Connection> existingConnection = connectionRepository
                .findByRequesterAndAddressee(requester, addressee)
                .or(() -> connectionRepository.findByRequesterAndAddressee(addressee, requester));

        if (existingConnection.isPresent()) {
            Connection conn = existingConnection.get();
            if (conn.getStatus() == ConnectionStatus.ACCEPTED) {
                throw new IllegalArgumentException("Ya están conectados");
            } else if (conn.getStatus() == ConnectionStatus.PENDING) {
                throw new IllegalArgumentException("Ya existe una solicitud pendiente");
            }
        }

        // Crear nueva solicitud de conexión
        Connection connection = new Connection();
        connection.setRequester(requester);
        connection.setAddressee(addressee);
        connection.setStatus(ConnectionStatus.PENDING);
        connection.setCreatedAt(LocalDateTime.now());
        connection.setUpdatedAt(LocalDateTime.now());

        return connectionRepository.save(connection);
    }

    /**
     * Acepta una solicitud de conexión
     */
    public Connection acceptConnectionRequest(Long connectionId, Long userId) {
        Connection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Conexión no encontrada"));

        // Verificar que el usuario sea el destinatario
        if (!connection.getAddressee().getId().equals(userId)) {
            throw new RuntimeException("No tienes permisos para aceptar esta solicitud");
        }

        // Verificar que esté pendiente
        if (connection.getStatus() != ConnectionStatus.PENDING) {
            throw new IllegalArgumentException("La solicitud no está pendiente");
        }

        connection.setStatus(ConnectionStatus.ACCEPTED);
        connection.setUpdatedAt(LocalDateTime.now());

        return connectionRepository.save(connection);
    }

    /**
     * Rechaza una solicitud de conexión
     */
    public Connection rejectConnectionRequest(Long connectionId, Long userId) {
        Connection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Conexión no encontrada"));

        // Verificar que el usuario sea el destinatario
        if (!connection.getAddressee().getId().equals(userId)) {
            throw new RuntimeException("No tienes permisos para rechazar esta solicitud");
        }

        // Verificar que esté pendiente
        if (connection.getStatus() != ConnectionStatus.PENDING) {
            throw new IllegalArgumentException("La solicitud no está pendiente");
        }

        connection.setStatus(ConnectionStatus.REJECTED);
        connection.setUpdatedAt(LocalDateTime.now());

        return connectionRepository.save(connection);
    }

    /**
     * Cancela/elimina una conexión
     */
    public void cancelConnection(Long connectionId, Long userId) {
        Connection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Conexión no encontrada"));

        // Verificar que el usuario sea parte de la conexión
        if (!connection.getRequester().getId().equals(userId) && 
            !connection.getAddressee().getId().equals(userId)) {
            throw new RuntimeException("No tienes permisos para cancelar esta conexión");
        }

        connectionRepository.delete(connection);
    }

    /**
     * Obtiene todas las conexiones aceptadas de un usuario
     */
    public List<Connection> getUserConnections(Long userId) {
        return connectionRepository.findAcceptedConnectionsByUserId(userId);
    }

    /**
     * Obtiene solicitudes de conexión pendientes recibidas
     */
    public List<Connection> getPendingReceivedRequests(Long userId) {
        return connectionRepository.findPendingReceivedRequests(userId);
    }

    /**
     * Obtiene solicitudes de conexión pendientes enviadas
     */
    public List<Connection> getPendingSentRequests(Long userId) {
        return connectionRepository.findPendingSentRequests(userId);
    }

    /**
     * Obtiene el estado de conexión entre dos usuarios
     */
    public String getConnectionStatus(Long userId1, Long userId2) {
        if (userId1.equals(userId2)) {
            return "SELF";
        }

        User user1 = userRepository.findById(userId1)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        User user2 = userRepository.findById(userId2)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Optional<Connection> connection = connectionRepository
                .findByRequesterAndAddressee(user1, user2)
                .or(() -> connectionRepository.findByRequesterAndAddressee(user2, user1));

        if (connection.isEmpty()) {
            return "NOT_CONNECTED";
        }

        return connection.get().getStatus().toString();
    }

    /**
     * Obtiene sugerencias de usuarios para conectar
     */
    public List<User> getUserSuggestions(Long userId, int limit) {
        List<User> suggestions = connectionRepository.findUserSuggestions(userId);
        return suggestions.stream().limit(limit).toList();
    }

    /**
     * Verifica si dos usuarios están conectados
     */
    public boolean areConnected(Long userId1, Long userId2) {
        return "ACCEPTED".equals(getConnectionStatus(userId1, userId2));
    }

    /**
     * Cuenta las conexiones de un usuario
     */
    public long countUserConnections(Long userId) {
        return connectionRepository.countAcceptedConnectionsByUserId(userId);
    }
}