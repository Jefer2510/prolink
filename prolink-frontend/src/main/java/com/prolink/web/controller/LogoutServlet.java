package com.prolink.web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Servlet para manejar el cierre de sesión de usuarios.
 * Invalida la sesión actual y redirige al usuario a la página de login.
 *
 * @author ProLink Team
 * @version 1.0
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(LogoutServlet.class.getName());
    
    /**
     * Maneja las peticiones GET y POST para cerrar sesión.
     * Ambos métodos ejecutan la misma lógica de cierre de sesión.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleLogout(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleLogout(request, response);
    }
    
    /**
     * Lógica común para manejar el cierre de sesión.
     * 
     * @param request  La petición HTTP
     * @param response La respuesta HTTP
     * @throws IOException Si hay error en la redirección
     */
    private void handleLogout(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            // Obtener información del usuario para logging
            Object currentUser = session.getAttribute("currentUser");
            String userInfo = "Usuario desconocido";
            
            if (currentUser != null) {
                userInfo = currentUser.toString();
            }
            
            logger.info("Cerrando sesión para: " + userInfo);
            
            // Invalidar la sesión completamente
            session.invalidate();
            
            logger.info("Sesión invalidada exitosamente");
        } else {
            logger.warning("Intento de logout sin sesión activa");
        }
        
        // Establecer headers para prevenir caché
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        
        // Redirigir a la página de login con mensaje de confirmación
        String contextPath = request.getContextPath();
        String loginUrl = contextPath + "/auth/login?message=logout_success";
        
        logger.info("Redirigiendo a: " + loginUrl);
        response.sendRedirect(loginUrl);
    }
}