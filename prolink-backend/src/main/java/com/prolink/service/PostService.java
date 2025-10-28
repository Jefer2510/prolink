package com.prolink.service;

import com.prolink.dto.request.CreatePostRequest;
import com.prolink.dto.request.UpdatePostRequest;
import com.prolink.entity.*;
import com.prolink.exception.ResourceNotFoundException;
import com.prolink.repository.LikeRepository;
import com.prolink.repository.PostRepository;
import com.prolink.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para operaciones de posts
 */
@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;

    /**
     * Crea un nuevo post
     */
    public Post createPost(Long userId, CreatePostRequest createRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Post post = new Post();
        post.setAuthor(user);
        post.setContent(createRequest.getContent());
        post.setType(createRequest.getType());
        post.setImageUrl(createRequest.getImageUrl());
        post.setVideoUrl(createRequest.getVideoUrl());
        post.setDocumentUrl(createRequest.getDocumentUrl());
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        return postRepository.save(post);
    }

    /**
     * Busca un post por ID
     */
    public Post findById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado"));
    }

    /**
     * Actualiza un post existente
     */
    public Post updatePost(Long postId, Long userId, UpdatePostRequest updateRequest) {
        Post post = findById(postId);

        // Verificar que el usuario sea el autor del post
        if (!post.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("No tienes permisos para editar este post");
        }

        // Actualizar campos si se proporcionan
        if (updateRequest.getContent() != null) {
            post.setContent(updateRequest.getContent());
        }
        if (updateRequest.getType() != null) {
            post.setType(updateRequest.getType());
        }
        if (updateRequest.getImageUrl() != null) {
            post.setImageUrl(updateRequest.getImageUrl());
        }
        if (updateRequest.getVideoUrl() != null) {
            post.setVideoUrl(updateRequest.getVideoUrl());
        }
        if (updateRequest.getDocumentUrl() != null) {
            post.setDocumentUrl(updateRequest.getDocumentUrl());
        }

        post.setUpdatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    /**
     * Elimina un post
     */
    public void deletePost(Long postId, Long userId) {
        Post post = findById(postId);

        // Verificar que el usuario sea el autor del post
        if (!post.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("No tienes permisos para eliminar este post");
        }

        postRepository.delete(post);
    }

    /**
     * Obtiene el feed de posts para un usuario
     * (posts de conexiones + posts públicos)
     */
    public List<Post> getFeed(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findFeedPosts(userId, pageable);
    }

    /**
     * Obtiene los posts de un usuario específico
     */
    public List<Post> getPostsByUser(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findByAuthorIdOrderByCreatedAtDesc(userId, pageable);
    }

    /**
     * Da like a un post
     */
    public void likePost(Long postId, Long userId) {
        Post post = findById(postId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // Verificar si ya existe el like
        Optional<Like> existingLike = likeRepository.findByUserAndPost(user, post);
        if (existingLike.isPresent()) {
            return; // Ya existe el like
        }

        // Crear nuevo like
        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        like.setType(LikeType.LIKE);
        like.setCreatedAt(LocalDateTime.now());

        likeRepository.save(like);
    }

    /**
     * Quita el like de un post
     */
    public void unlikePost(Long postId, Long userId) {
        Post post = findById(postId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Optional<Like> existingLike = likeRepository.findByUserAndPost(user, post);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
        }
    }

    /**
     * Obtiene posts públicos recientes
     */
    public List<Post> getPublicPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findPublicPosts(pageable);
    }

    /**
     * Busca posts por contenido
     */
    public List<Post> searchPosts(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findByContentContainingIgnoreCase(query, pageable);
    }
}