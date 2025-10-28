package com.prolink.repository;

import com.prolink.entity.Like;
import com.prolink.entity.Post;
import com.prolink.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para operaciones de Like
 */
@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    /**
     * Busca un like específico por usuario y post
     */
    Optional<Like> findByUserAndPost(User user, Post post);

    /**
     * Busca todos los likes de un usuario
     */
    List<Like> findByUserId(Long userId);

    /**
     * Busca todos los likes de un post
     */
    List<Like> findByPostId(Long postId);

    /**
     * Cuenta likes por post
     */
    long countByPostId(Long postId);

    /**
     * Verifica si un usuario dio like a un post
     */
    boolean existsByUserIdAndPostId(Long userId, Long postId);

    /**
     * Obtiene posts que le gustaron a un usuario
     */
    @Query("SELECT l.post FROM Like l WHERE l.user.id = :userId ORDER BY l.createdAt DESC")
    List<Post> findLikedPostsByUserId(@Param("userId") Long userId);

    /**
     * Elimina todos los likes de un post
     */
    void deleteByPostId(Long postId);

    /**
     * Elimina todos los likes de un usuario
     */
    void deleteByUserId(Long userId);
}