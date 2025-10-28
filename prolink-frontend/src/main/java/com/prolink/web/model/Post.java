package com.prolink.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

/**
 * Modelo de Post para el frontend
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Post {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("content")
    private String content;
    
    @JsonProperty("imageUrl")
    private String imageUrl;
    
    @JsonProperty("videoUrl")
    private String videoUrl;
    
    @JsonProperty("postType")
    private String postType;
    
    @JsonProperty("author")
    private User author;
    
    @JsonProperty("likesCount")
    private int likesCount;
    
    @JsonProperty("commentsCount")
    private int commentsCount;
    
    @JsonProperty("sharesCount")
    private int sharesCount;
    
    @JsonProperty("isLikedByCurrentUser")
    private boolean isLikedByCurrentUser;
    
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
    
    // Constructor por defecto
    public Post() {}
    
    // Constructor con parámetros básicos
    public Post(String content, User author) {
        this.content = content;
        this.author = author;
        this.postType = "TEXT";
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters y Setters
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
    
    public String getPostType() {
        return postType;
    }
    
    public void setPostType(String postType) {
        this.postType = postType;
    }
    
    public User getAuthor() {
        return author;
    }
    
    public void setAuthor(User author) {
        this.author = author;
    }
    
    public int getLikesCount() {
        return likesCount;
    }
    
    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }
    
    public int getCommentsCount() {
        return commentsCount;
    }
    
    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }
    
    public int getSharesCount() {
        return sharesCount;
    }
    
    public void setSharesCount(int sharesCount) {
        this.sharesCount = sharesCount;
    }
    
    public boolean isLikedByCurrentUser() {
        return isLikedByCurrentUser;
    }
    
    public void setLikedByCurrentUser(boolean likedByCurrentUser) {
        isLikedByCurrentUser = likedByCurrentUser;
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
    
    // Métodos de utilidad
    public boolean hasMedia() {
        return (imageUrl != null && !imageUrl.isEmpty()) || 
               (videoUrl != null && !videoUrl.isEmpty());
    }
    
    public boolean isImagePost() {
        return imageUrl != null && !imageUrl.isEmpty();
    }
    
    public boolean isVideoPost() {
        return videoUrl != null && !videoUrl.isEmpty();
    }
    
    public String getFormattedContent() {
        if (content == null) return "";
        // Formatear contenido para mostrar en JSP (escapar HTML, agregar enlaces, etc.)
        return content.replace("\n", "<br>");
    }
    
    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", postType='" + postType + '\'' +
                ", author=" + (author != null ? author.getFullName() : "null") +
                ", likesCount=" + likesCount +
                ", createdAt=" + createdAt +
                '}';
    }
}