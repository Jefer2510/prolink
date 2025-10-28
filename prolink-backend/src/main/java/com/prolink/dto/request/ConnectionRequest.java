package com.prolink.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 * DTO para solicitar una conexión
 */
public class ConnectionRequest {

    @NotNull(message = "El ID del destinatario es requerido")
    private Long addresseeId;

    private String message; // Mensaje opcional al enviar solicitud

    // Constructores
    public ConnectionRequest() {}

    public ConnectionRequest(Long addresseeId) {
        this.addresseeId = addresseeId;
    }

    public ConnectionRequest(Long addresseeId, String message) {
        this.addresseeId = addresseeId;
        this.message = message;
    }

    // Getters y setters
    public Long getAddresseeId() {
        return addresseeId;
    }

    public void setAddresseeId(Long addresseeId) {
        this.addresseeId = addresseeId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}