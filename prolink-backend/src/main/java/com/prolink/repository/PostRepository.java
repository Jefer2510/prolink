package com.prolink.repository;

import com.prolink.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para operaciones de Post
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Busca posts por autor ordenados por fecha de creación descendente
     */
    List<Post> findByAuthorIdOrderByCreatedAtDesc(Long authorId, Pageable pageable);

    /**
     * Busca posts por contenido (búsqueda insensible a mayúsculas)
     */
    List<Post> findByContentContainingIgnoreCase(String content, Pageable pageable);

    /**
     * Obtiene posts del feed personalizado para un usuario
     * Incluye posts de conexiones y posts de usuarios activos
     */
    @Query("SELECT p FROM Post p " +
           "WHERE p.author.id IN " +
           "   (SELECT CASE " +
           "       WHEN c.requester.id = :userId THEN c.addressee.id " +
           "       ELSE c.requester.id END " +
           "    FROM Connection c " +
           "    WHERE (c.requester.id = :userId OR c.addressee.id = :userId) " +
           "    AND c.status = 'ACCEPTED') " +
           "OR p.author.id = :userId " +
           "ORDER BY p.createdAt DESC")
    List<Post> findFeedPosts(@Param("userId") Long userId, Pageable pageable);

    /**
     * Obtiene posts públicos (de usuarios activos)
     */
    @Query("SELECT p FROM Post p " +
           "WHERE p.author.isActive = true " +
           "ORDER BY p.createdAt DESC")
    List<Post> findPublicPosts(Pageable pageable);

    /**
     * Cuenta posts por autor
     */
    long countByAuthorId(Long authorId);

    /**
     * Busca posts por tipo
     */
    List<Post> findByTypeOrderByCreatedAtDesc(String type, Pageable pageable);
}