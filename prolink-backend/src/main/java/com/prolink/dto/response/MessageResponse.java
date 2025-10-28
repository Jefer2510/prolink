package com.prolink.dto.response;

/**
 * DTO genérico para respuestas de la API
 */
public class MessageResponse {
    
    private String message;
    private boolean success;
    
    // Constructors
    public MessageResponse() {}
    
    public MessageResponse(String message) {
        this.message = message;
        this.success = true;
    }
    
    public MessageResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
    
    // Static factory methods
    public static MessageResponse success(String message) {
        return new MessageResponse(message, true);
    }
    
    public static MessageResponse error(String message) {
        return new MessageResponse(message, false);
    }
    
    // Getters and Setters
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
}