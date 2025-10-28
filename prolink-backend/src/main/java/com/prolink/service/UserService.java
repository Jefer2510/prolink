package com.prolink.service;

import com.prolink.dto.request.UpdateProfileRequest;
import com.prolink.entity.User;
import com.prolink.exception.ResourceNotFoundException;
import com.prolink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para operaciones de usuarios
 */
@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Encuentra un usuario por ID
     */
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
    
    /**
     * Encuentra un usuario por username
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }
    
    /**
     * Actualiza el perfil del usuario
     */
    public User updateProfile(Long userId, UpdateProfileRequest updateRequest) {
        User user = findById(userId);
        
        // Actualizar solo los campos que no son null
        if (updateRequest.getFirstName() != null) {
            user.setFirstName(updateRequest.getFirstName());
        }
        if (updateRequest.getLastName() != null) {
            user.setLastName(updateRequest.getLastName());
        }
        if (updateRequest.getEmail() != null) {
            user.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getBio() != null) {
            user.setBio(updateRequest.getBio());
        }
        if (updateRequest.getProfessionalTitle() != null) {
            user.setProfessionalTitle(updateRequest.getProfessionalTitle());
        }
        if (updateRequest.getIndustry() != null) {
            user.setIndustry(updateRequest.getIndustry());
        }
        if (updateRequest.getLocation() != null) {
            user.setLocation(updateRequest.getLocation());
        }
        
        return userRepository.save(user);
    }
    
    /**
     * Busca usuarios por nombre o email
     */
    public List<User> searchUsers(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                query, query, pageable);
        return userPage.getContent();
    }
    
    /**
     * Obtiene sugerencias de conexión para un usuario
     */
    public List<User> getConnectionSuggestions(Long userId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        // Por ahora, devolvemos usuarios aleatorios que no sean el usuario actual
        // En una implementación real, esto sería más sofisticado basado en industria, conexiones mutuas, etc.
        return userRepository.findUsersForSuggestions(userId, pageable);
    }
    
    /**
     * Verifica si existe un usuario por email
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    /**
     * Verifica si existe un usuario por username
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    /**
     * Guarda un usuario
     */
    public User save(User user) {
        return userRepository.save(user);
    }
}