package com.prolink.dto.response;

import com.prolink.entity.Post;
import com.prolink.entity.PostType;
import com.prolink.entity.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de respuesta para posts
 */
public class PostResponse {

    private Long id;
    private String content;
    private PostType type;
    private String imageUrl;
    private String videoUrl;
    private String documentUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Información del autor
    private Long authorId;
    private String authorUsername;
    private String authorFirstName;
    private String authorLastName;
    private String authorProfilePicture;
    
    // Métricas del post
    private Integer likesCount;
    private Integer commentsCount;
    
    // Estado del usuario actual (si está autenticado)
    private Boolean isLikedByCurrentUser;

    // Constructor vacío
    public PostResponse() {}

    /**
     * Factory method para crear PostResponse desde Post entity
     */
    public static PostResponse fromPost(Post post) {
        PostResponse response = new PostResponse();
        
        response.setId(post.getId());
        response.setContent(post.getContent());
        response.setType(post.getType());
        response.setImageUrl(post.getImageUrl());
        response.setVideoUrl(post.getVideoUrl());
        response.setDocumentUrl(post.getDocumentUrl());
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());
        
        // Información del autor
        User author = post.getAuthor();
        if (author != null) {
            response.setAuthorId(author.getId());
            response.setAuthorUsername(author.getUsername());
            response.setAuthorFirstName(author.getFirstName());
            response.setAuthorLastName(author.getLastName());
            response.setAuthorProfilePicture(author.getProfilePictureUrl());
        }
        
        // Métricas
        response.setLikesCount(post.getLikes() != null ? post.getLikes().size() : 0);
        response.setCommentsCount(post.getComments() != null ? post.getComments().size() : 0);
        
        return response;
    }

    /**
     * Factory method con información del usuario actual
     */
    public static PostResponse fromPost(Post post, Long currentUserId) {
        PostResponse response = fromPost(post);
        
        // Verificar si el usuario actual dio like
        if (currentUserId != null && post.getLikes() != null) {
            response.setIsLikedByCurrentUser(
                post.getLikes().stream()
                    .anyMatch(like -> like.getUser().getId().equals(currentUserId))
            );
        }
        
        return response;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PostType getType() {
        return type;
    }

    public void setType(PostType type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public String getAuthorProfilePicture() {
        return authorProfilePicture;
    }

    public void setAuthorProfilePicture(String authorProfilePicture) {
        this.authorProfilePicture = authorProfilePicture;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public Integer getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Boolean getIsLikedByCurrentUser() {
        return isLikedByCurrentUser;
    }

    public void setIsLikedByCurrentUser(Boolean isLikedByCurrentUser) {
        this.isLikedByCurrentUser = isLikedByCurrentUser;
    }
}