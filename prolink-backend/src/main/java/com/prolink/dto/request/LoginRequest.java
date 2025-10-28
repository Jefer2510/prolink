package com.prolink.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para peticiones de login
 */
public class LoginRequest {
    
    @NotBlank(message = "Username o email es requerido")
    private String usernameOrEmail;
    
    @NotBlank(message = "Password es requerido")
    private String password;
    
    // Constructors
    public LoginRequest() {}
    
    public LoginRequest(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }
    
    // Getters and Setters
    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }
    
    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}