package com.prolink.repository;

import com.prolink.entity.Connection;
import com.prolink.entity.ConnectionStatus;
import com.prolink.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para operaciones de Connection
 */
@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {

    /**
     * Busca una conexión específica entre dos usuarios
     */
    Optional<Connection> findByRequesterAndAddressee(User requester, User addressee);

    /**
     * Busca conexiones por estado
     */
    List<Connection> findByStatus(ConnectionStatus status);

    /**
     * Obtiene todas las conexiones aceptadas de un usuario
     */
    @Query("SELECT c FROM Connection c " +
           "WHERE (c.requester.id = :userId OR c.addressee.id = :userId) " +
           "AND c.status = 'ACCEPTED' " +
           "ORDER BY c.updatedAt DESC")
    List<Connection> findAcceptedConnectionsByUserId(@Param("userId") Long userId);

    /**
     * Cuenta las conexiones aceptadas de un usuario
     */
    @Query("SELECT COUNT(c) FROM Connection c " +
           "WHERE (c.requester.id = :userId OR c.addressee.id = :userId) " +
           "AND c.status = 'ACCEPTED'")
    long countAcceptedConnectionsByUserId(@Param("userId") Long userId);

    /**
     * Obtiene solicitudes pendientes recibidas por un usuario
     */
    @Query("SELECT c FROM Connection c " +
           "WHERE c.addressee.id = :userId " +
           "AND c.status = 'PENDING' " +
           "ORDER BY c.createdAt DESC")
    List<Connection> findPendingReceivedRequests(@Param("userId") Long userId);

    /**
     * Obtiene solicitudes pendientes enviadas por un usuario
     */
    @Query("SELECT c FROM Connection c " +
           "WHERE c.requester.id = :userId " +
           "AND c.status = 'PENDING' " +
           "ORDER BY c.createdAt DESC")
    List<Connection> findPendingSentRequests(@Param("userId") Long userId);

    /**
     * Obtiene sugerencias de usuarios que no están conectados
     */
    @Query("SELECT u FROM User u " +
           "WHERE u.id != :userId " +
           "AND u.id NOT IN (" +
           "    SELECT CASE " +
           "        WHEN c.requester.id = :userId THEN c.addressee.id " +
           "        ELSE c.requester.id " +
           "    END " +
           "    FROM Connection c " +
           "    WHERE (c.requester.id = :userId OR c.addressee.id = :userId)" +
           ") " +
           "ORDER BY u.createdAt DESC")
    List<User> findUserSuggestions(@Param("userId") Long userId);

    /**
     * Verifica si existe una conexión entre dos usuarios
     */
    @Query("SELECT COUNT(c) > 0 FROM Connection c " +
           "WHERE ((c.requester.id = :userId1 AND c.addressee.id = :userId2) " +
           "OR (c.requester.id = :userId2 AND c.addressee.id = :userId1)) " +
           "AND c.status = 'ACCEPTED'")
    boolean existsAcceptedConnectionBetweenUsers(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    /**
     * Elimina todas las conexiones de un usuario
     */
    @Query("DELETE FROM Connection c " +
           "WHERE c.requester.id = :userId OR c.addressee.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);

    /**
     * Busca conexiones por requester
     */
    List<Connection> findByRequesterId(Long requesterId);

    /**
     * Busca conexiones por addressee
     */
    List<Connection> findByAddresseeId(Long addresseeId);
}