package com.prolink.web.controller;

import com.prolink.web.model.User;
import com.prolink.web.service.ApiService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Servlet para manejo de perfiles de usuario
 * Permite ver y editar información del perfil
 */
@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile", "/profile/*"})
public class ProfileServlet extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(ProfileServlet.class);
    
    private ApiService apiService;
    
    @Override
    public void init() throws ServletException {
        apiService = new ApiService();
        logger.info("ProfileServlet inicializado");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Verificar autenticación
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login?message=access_denied");
            return;
        }
        
        User currentUser = (User) session.getAttribute("currentUser");
        String pathInfo = request.getPathInfo();
        
        try {
            if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("")) {
                // Mostrar perfil del usuario actual
                showProfile(request, response, currentUser, currentUser);
            } else {
                // Obtener ID del usuario del path
                String[] pathParts = pathInfo.split("/");
                if (pathParts.length > 1) {
                    try {
                        Long userId = Long.parseLong(pathParts[1]);
                        // TODO: Obtener usuario por ID desde la API
                        // Por ahora, mostrar el perfil del usuario actual
                        showProfile(request, response, currentUser, currentUser);
                    } catch (NumberFormatException e) {
                        logger.warn("ID de usuario inválido: " + pathParts[1]);
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de usuario inválido");
                        return;
                    }
                } else {
                    showProfile(request, response, currentUser, currentUser);
                }
            }
        } catch (Exception e) {
            logger.error("Error al mostrar perfil", e);
            request.setAttribute("error", "Error al cargar el perfil");
            request.getRequestDispatcher("/WEB-INF/jsp/dashboard/profile.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Verificar autenticación
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login?message=access_denied");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("updateProfile".equals(action)) {
            handleUpdateProfile(request, response);
        } else if ("changePassword".equals(action)) {
            handleChangePassword(request, response);
        } else {
            logger.warn("Acción desconocida: " + action);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
        }
    }
    
    /**
     * Muestra el perfil de un usuario
     */
    private void showProfile(HttpServletRequest request, HttpServletResponse response, 
                           User currentUser, User profileUser) 
            throws ServletException, IOException {
        
        request.setAttribute("currentUser", currentUser);
        request.setAttribute("profileUser", profileUser);
        request.setAttribute("isOwnProfile", currentUser.getId().equals(profileUser.getId()));
        
        // TODO: Obtener estadísticas del usuario (posts, conexiones, etc.)
        // Por ahora, valores por defecto
        request.setAttribute("postCount", 0);
        request.setAttribute("connectionCount", 0);
        
        request.getRequestDispatcher("/WEB-INF/jsp/dashboard/profile.jsp").forward(request, response);
    }
    
    /**
     * Maneja la actualización del perfil
     */
    private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String headline = request.getParameter("headline");
        String summary = request.getParameter("summary");
        String location = request.getParameter("location");
        
        // Validaciones básicas
        if (firstName == null || firstName.trim().isEmpty() ||
            lastName == null || lastName.trim().isEmpty() ||
            email == null || email.trim().isEmpty()) {
            
            request.setAttribute("error", "Nombre, apellido y email son obligatorios");
            showProfile(request, response, currentUser, currentUser);
            return;
        }
        
        try {
            // TODO: Implementar actualización de perfil en la API
            // Por ahora, actualizar datos en la sesión
            currentUser.setFirstName(firstName.trim());
            currentUser.setLastName(lastName.trim());
            currentUser.setEmail(email.trim());
            currentUser.setHeadline(headline != null ? headline.trim() : null);
            // TODO: Agregar summary y location al modelo User
            
            session.setAttribute("currentUser", currentUser);
            
            request.setAttribute("success", "Perfil actualizado exitosamente");
            showProfile(request, response, currentUser, currentUser);
            
        } catch (Exception e) {
            logger.error("Error al actualizar perfil para usuario: " + currentUser.getEmail(), e);
            request.setAttribute("error", "Error al actualizar el perfil. Por favor, inténtalo más tarde.");
            showProfile(request, response, currentUser, currentUser);
        }
    }
    
    /**
     * Maneja el cambio de contraseña
     */
    private void handleChangePassword(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Validaciones
        if (currentPassword == null || currentPassword.trim().isEmpty() ||
            newPassword == null || newPassword.trim().isEmpty() ||
            confirmPassword == null || confirmPassword.trim().isEmpty()) {
            
            request.setAttribute("error", "Todos los campos de contraseña son obligatorios");
            showProfile(request, response, currentUser, currentUser);
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Las contraseñas nuevas no coinciden");
            showProfile(request, response, currentUser, currentUser);
            return;
        }
        
        if (newPassword.length() < 6) {
            request.setAttribute("error", "La nueva contraseña debe tener al menos 6 caracteres");
            showProfile(request, response, currentUser, currentUser);
            return;
        }
        
        try {
            // TODO: Implementar cambio de contraseña en la API
            // Por ahora, simular éxito
            request.setAttribute("success", "Contraseña actualizada exitosamente");
            showProfile(request, response, currentUser, currentUser);
            
        } catch (Exception e) {
            logger.error("Error al cambiar contraseña para usuario: " + currentUser.getEmail(), e);
            request.setAttribute("error", "Error al cambiar la contraseña. Por favor, inténtalo más tarde.");
            showProfile(request, response, currentUser, currentUser);
        }
    }
}