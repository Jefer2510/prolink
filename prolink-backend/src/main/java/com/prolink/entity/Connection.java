package com.prolink.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "connections", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"requester_id", "addressee_id"})
})
public class Connection {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "connection_seq")
    @SequenceGenerator(name = "connection_seq", sequenceName = "CONNECTION_SEQ", allocationSize = 1)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConnectionStatus status = ConnectionStatus.PENDING;
    
    @Column(length = 500)
    private String message;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;
    
    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addressee_id", nullable = false)
    private User addressee;
    
    // Constructores
    public Connection() {}
    
    public Connection(User requester, User addressee) {
        this.requester = requester;
        this.addressee = addressee;
    }
    
    public Connection(User requester, User addressee, String message) {
        this.requester = requester;
        this.addressee = addressee;
        this.message = message;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public ConnectionStatus getStatus() { return status; }
    public void setStatus(ConnectionStatus status) { 
        this.status = status;
        if (status == ConnectionStatus.ACCEPTED && acceptedAt == null) {
            this.acceptedAt = LocalDateTime.now();
        }
    }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public LocalDateTime getAcceptedAt() { return acceptedAt; }
    public void setAcceptedAt(LocalDateTime acceptedAt) { this.acceptedAt = acceptedAt; }
    
    public User getRequester() { return requester; }
    public void setRequester(User requester) { this.requester = requester; }
    
    public User getAddressee() { return addressee; }
    public void setAddressee(User addressee) { this.addressee = addressee; }
    
    // Métodos de utilidad
    public void accept() {
        this.status = ConnectionStatus.ACCEPTED;
        this.acceptedAt = LocalDateTime.now();
    }
    
    public void reject() {
        this.status = ConnectionStatus.REJECTED;
    }
    
    public void block() {
        this.status = ConnectionStatus.BLOCKED;
    }
    
    public boolean isPending() {
        return status == ConnectionStatus.PENDING;
    }
    
    public boolean isAccepted() {
        return status == ConnectionStatus.ACCEPTED;
    }
    
    public boolean isRejected() {
        return status == ConnectionStatus.REJECTED;
    }
    
    public boolean isBlocked() {
        return status == ConnectionStatus.BLOCKED;
    }
    
    @Override
    public String toString() {
        return "Connection{" +
                "id=" + id +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", acceptedAt=" + acceptedAt +
                '}';
    }
}