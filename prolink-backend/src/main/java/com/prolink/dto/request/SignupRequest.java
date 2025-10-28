package com.prolink.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para peticiones de registro de usuario
 */
public class SignupRequest {
    
    @NotBlank(message = "Username es requerido")
    @Size(min = 3, max = 20, message = "Username debe tener entre 3 y 20 caracteres")
    private String username;
    
    @NotBlank(message = "Email es requerido")
    @Size(max = 100, message = "Email no puede exceder 100 caracteres")
    @Email(message = "Email debe tener formato válido")
    private String email;
    
    @NotBlank(message = "Password es requerido")
    @Size(min = 6, max = 40, message = "Password debe tener entre 6 y 40 caracteres")
    private String password;
    
    @NotBlank(message = "Nombre es requerido")
    @Size(max = 50, message = "Nombre no puede exceder 50 caracteres")
    private String firstName;
    
    @NotBlank(message = "Apellido es requerido")
    @Size(max = 50, message = "Apellido no puede exceder 50 caracteres")
    private String lastName;
    
    @Size(max = 100, message = "Título profesional no puede exceder 100 caracteres")
    private String professionalTitle;
    
    @Size(max = 50, message = "Industria no puede exceder 50 caracteres")
    private String industry;
    
    @Size(max = 100, message = "Localización no puede exceder 100 caracteres")
    private String location;
    
    // Constructors
    public SignupRequest() {}
    
    public SignupRequest(String username, String email, String password, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    // Getters and Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getProfessionalTitle() {
        return professionalTitle;
    }
    
    public void setProfessionalTitle(String professionalTitle) {
        this.professionalTitle = professionalTitle;
    }
    
    public String getIndustry() {
        return industry;
    }
    
    public void setIndustry(String industry) {
        this.industry = industry;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
}