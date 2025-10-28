package com.prolink.repository;

import com.prolink.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para operaciones CRUD de la entidad User
 * Proporciona métodos de consulta personalizados para usuarios
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Busca usuario por username
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Busca usuario por email
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Busca usuario por username o email
     */
    Optional<User> findByUsernameOrEmail(String username, String email);
    
    /**
     * Verifica si existe username
     */
    boolean existsByUsername(String username);
    
    /**
     * Verifica si existe email
     */
    boolean existsByEmail(String email);
    
    /**
     * Busca usuarios activos por nombre o apellido (para búsquedas)
     */
    @Query("SELECT u FROM User u WHERE u.isActive = true AND " +
           "(LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<User> findActiveUsersByName(@Param("searchTerm") String searchTerm);
    
    /**
     * Busca usuarios activos por industria
     */
    @Query("SELECT u FROM User u WHERE u.isActive = true AND " +
           "LOWER(u.industry) LIKE LOWER(CONCAT('%', :industry, '%'))")
    List<User> findByIndustry(@Param("industry") String industry);
    
    /**
     * Busca usuarios activos por localización
     */
    @Query("SELECT u FROM User u WHERE u.isActive = true AND " +
           "LOWER(u.location) LIKE LOWER(CONCAT('%', :location, '%'))")
    List<User> findByLocation(@Param("location") String location);
    
    /**
     * Obtiene usuarios más activos (por número de posts)
     */
    @Query("SELECT u FROM User u LEFT JOIN u.posts p WHERE u.isActive = true " +
           "GROUP BY u ORDER BY COUNT(p) DESC")
    List<User> findMostActiveUsers();
    
    /**
     * Busca usuarios por username o email (búsqueda con paginación)
     */
    @Query("SELECT u FROM User u WHERE u.isActive = true AND " +
           "(LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%')))")
    org.springframework.data.domain.Page<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            @Param("query") String query, @Param("query") String query2, 
            org.springframework.data.domain.Pageable pageable);
    
    /**
     * Obtiene sugerencias de usuarios para conectar
     */
    @Query("SELECT u FROM User u WHERE u.id != :userId AND u.isActive = true ORDER BY u.createdAt DESC")
    List<User> findUsersForSuggestions(@Param("userId") Long userId, 
            org.springframework.data.domain.Pageable pageable);
}