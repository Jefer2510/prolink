package com.prolink.controller;

import com.prolink.dto.request.CreatePostRequest;
import com.prolink.dto.request.UpdatePostRequest;
import com.prolink.dto.response.PostResponse;
import com.prolink.entity.Post;
import com.prolink.security.UserPrincipal;
import com.prolink.service.PostService;
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
 * Controlador REST para operaciones de posts
 */
@RestController
@RequestMapping("/posts")
@Tag(name = "Posts", description = "Gestión de publicaciones de usuarios")
@SecurityRequirement(name = "bearerAuth")
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * Crea un nuevo post
     */
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Crear publicación", description = "Crea una nueva publicación para el usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Publicación creada exitosamente",
            content = @Content(schema = @Schema(implementation = PostResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<PostResponse> createPost(
            @Valid @RequestBody @Parameter(description = "Datos de la nueva publicación") CreatePostRequest createRequest,
            Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Post post = postService.createPost(userPrincipal.getId(), createRequest);
        
        PostResponse response = PostResponse.fromPost(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Obtiene un post por ID
     */
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long postId) {
        Post post = postService.findById(postId);
        PostResponse response = PostResponse.fromPost(post);
        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza un post
     */
    @PutMapping("/{postId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody UpdatePostRequest updateRequest,
            Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Post post = postService.updatePost(postId, userPrincipal.getId(), updateRequest);
        
        PostResponse response = PostResponse.fromPost(post);
        return ResponseEntity.ok(response);
    }

    /**
     * Elimina un post
     */
    @DeleteMapping("/{postId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        postService.deletePost(postId, userPrincipal.getId());
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene el feed de posts del usuario autenticado
     */
    @GetMapping("/feed")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<PostResponse>> getFeed(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        List<Post> posts = postService.getFeed(userPrincipal.getId(), page, size);
        
        List<PostResponse> responses = posts.stream()
                .map(PostResponse::fromPost)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Obtiene los posts de un usuario específico
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponse>> getUserPosts(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<Post> posts = postService.getPostsByUser(userId, page, size);
        
        List<PostResponse> responses = posts.stream()
                .map(PostResponse::fromPost)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Da like a un post
     */
    @PostMapping("/{postId}/like")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> likePost(
            @PathVariable Long postId,
            Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        postService.likePost(postId, userPrincipal.getId());
        return ResponseEntity.ok().build();
    }

    /**
     * Quita el like de un post
     */
    @DeleteMapping("/{postId}/like")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> unlikePost(
            @PathVariable Long postId,
            Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        postService.unlikePost(postId, userPrincipal.getId());
        return ResponseEntity.ok().build();
    }
}