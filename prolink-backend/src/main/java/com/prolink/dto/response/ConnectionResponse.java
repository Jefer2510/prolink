package com.prolink.dto.response;

import com.prolink.entity.Connection;
import com.prolink.entity.ConnectionStatus;
import com.prolink.entity.User;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para conexiones
 */
public class ConnectionResponse {

    private Long id;
    private ConnectionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Información del solicitante
    private Long requesterId;
    private String requesterUsername;
    private String requesterFirstName;
    private String requesterLastName;
    private String requesterProfilePicture;
    private String requesterHeadline;
    
    // Información del destinatario
    private Long addresseeId;
    private String addresseeUsername;
    private String addresseeFirstName;
    private String addresseeLastName;
    private String addresseeProfilePicture;
    private String addresseeHeadline;

    // Constructor vacío
    public ConnectionResponse() {}

    /**
     * Factory method para crear ConnectionResponse desde Connection entity
     */
    public static ConnectionResponse fromConnection(Connection connection) {
        ConnectionResponse response = new ConnectionResponse();
        
        response.setId(connection.getId());
        response.setStatus(connection.getStatus());
        response.setCreatedAt(connection.getCreatedAt());
        response.setUpdatedAt(connection.getUpdatedAt());
        
        // Información del solicitante
        User requester = connection.getRequester();
        if (requester != null) {
            response.setRequesterId(requester.getId());
            response.setRequesterUsername(requester.getUsername());
            response.setRequesterFirstName(requester.getFirstName());
            response.setRequesterLastName(requester.getLastName());
            response.setRequesterProfilePicture(requester.getProfilePictureUrl());
            response.setRequesterHeadline(requester.getHeadline());
        }
        
        // Información del destinatario
        User addressee = connection.getAddressee();
        if (addressee != null) {
            response.setAddresseeId(addressee.getId());
            response.setAddresseeUsername(addressee.getUsername());
            response.setAddresseeFirstName(addressee.getFirstName());
            response.setAddresseeLastName(addressee.getLastName());
            response.setAddresseeProfilePicture(addressee.getProfilePictureUrl());
            response.setAddresseeHeadline(addressee.getHeadline());
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

    public ConnectionStatus getStatus() {
        return status;
    }

    public void setStatus(ConnectionStatus status) {
        this.status = status;
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

    public Long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public String getRequesterUsername() {
        return requesterUsername;
    }

    public void setRequesterUsername(String requesterUsername) {
        this.requesterUsername = requesterUsername;
    }

    public String getRequesterFirstName() {
        return requesterFirstName;
    }

    public void setRequesterFirstName(String requesterFirstName) {
        this.requesterFirstName = requesterFirstName;
    }

    public String getRequesterLastName() {
        return requesterLastName;
    }

    public void setRequesterLastName(String requesterLastName) {
        this.requesterLastName = requesterLastName;
    }

    public String getRequesterProfilePicture() {
        return requesterProfilePicture;
    }

    public void setRequesterProfilePicture(String requesterProfilePicture) {
        this.requesterProfilePicture = requesterProfilePicture;
    }

    public String getRequesterHeadline() {
        return requesterHeadline;
    }

    public void setRequesterHeadline(String requesterHeadline) {
        this.requesterHeadline = requesterHeadline;
    }

    public Long getAddresseeId() {
        return addresseeId;
    }

    public void setAddresseeId(Long addresseeId) {
        this.addresseeId = addresseeId;
    }

    public String getAddresseeUsername() {
        return addresseeUsername;
    }

    public void setAddresseeUsername(String addresseeUsername) {
        this.addresseeUsername = addresseeUsername;
    }

    public String getAddresseeFirstName() {
        return addresseeFirstName;
    }

    public void setAddresseeFirstName(String addresseeFirstName) {
        this.addresseeFirstName = addresseeFirstName;
    }

    public String getAddresseeLastName() {
        return addresseeLastName;
    }

    public void setAddresseeLastName(String addresseeLastName) {
        this.addresseeLastName = addresseeLastName;
    }

    public String getAddresseeProfilePicture() {
        return addresseeProfilePicture;
    }

    public void setAddresseeProfilePicture(String addresseeProfilePicture) {
        this.addresseeProfilePicture = addresseeProfilePicture;
    }

    public String getAddresseeHeadline() {
        return addresseeHeadline;
    }

    public void setAddresseeHeadline(String addresseeHeadline) {
        this.addresseeHeadline = addresseeHeadline;
    }
}