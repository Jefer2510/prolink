package com.prolink.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "likes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "post_id"}),
    @UniqueConstraint(columnNames = {"user_id", "comment_id"})
})
public class Like {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "like_seq")
    @SequenceGenerator(name = "like_seq", sequenceName = "LIKE_SEQ", allocationSize = 1)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LikeType type = LikeType.LIKE;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;
    
    // Constructores
    public Like() {}
    
    public Like(User user, Post post) {
        this.user = user;
        this.post = post;
    }
    
    public Like(User user, Comment comment) {
        this.user = user;
        this.comment = comment;
    }
    
    public Like(User user, Post post, LikeType type) {
        this.user = user;
        this.post = post;
        this.type = type;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public LikeType getType() { return type; }
    public void setType(LikeType type) { this.type = type; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }
    
    public Comment getComment() { return comment; }
    public void setComment(Comment comment) { this.comment = comment; }
    
    @Override
    public String toString() {
        return "Like{" +
                "id=" + id +
                ", type=" + type +
                ", createdAt=" + createdAt +
                '}';
    }
}