package com.prolink.dto.request;

import com.prolink.entity.PostType;
import jakarta.validation.constraints.Size;

/**
 * DTO para actualizar un post existente
 */
public class UpdatePostRequest {

    @Size(max = 1000, message = "El contenido del post no puede exceder 1000 caracteres")
    private String content;

    private PostType type;
    
    private String imageUrl;
    
    private String videoUrl;
    
    private String documentUrl;

    // Constructores
    public UpdatePostRequest() {}

    public UpdatePostRequest(String content, PostType type) {
        this.content = content;
        this.type = type;
    }

    // Getters y setters
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
}