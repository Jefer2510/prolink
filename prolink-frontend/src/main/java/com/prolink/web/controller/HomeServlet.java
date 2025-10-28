package com.prolink.web.controller;

import com.prolink.web.model.Post;
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
import java.util.List;

/**
 * Servlet para la página de inicio (redirige al dashboard)
 */
@WebServlet(name = "HomeServlet", urlPatterns = {"/home", "/"})
public class HomeServlet extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(HomeServlet.class);
    
    private ApiService apiService;
    
    @Override
    public void init() throws ServletException {
        apiService = new ApiService();
        logger.info("HomeServlet inicializado");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        logger.debug("GET request a home, redirigiendo a dashboard");
        
        // Verificar autenticación
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            logger.debug("Usuario no autenticado, redirigiendo a login");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Redirigir al dashboard
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Redirigir a dashboard para manejar POST requests
        doGet(request, response);
    }
    
    @Override
    public void destroy() {
        if (apiService != null) {
            apiService.close();
        }
        logger.info("HomeServlet destruido");
    }
}