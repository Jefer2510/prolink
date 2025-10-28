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
 * Servlet para manejo de autenticación (login/registro)
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login", "/register"})
public class LoginServlet extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);
    
    private ApiService apiService;
    
    @Override
    public void init() throws ServletException {
        apiService = new ApiService();
        logger.info("LoginServlet inicializado");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Si el usuario ya está autenticado, redirigir al dashboard
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("currentUser") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        
        String path = request.getServletPath();
        logger.debug("GET request to: " + path);
        
        // Manejar mensajes especiales
        String message = request.getParameter("message");
        if ("logout_success".equals(message)) {
            request.setAttribute("success", "Has cerrado sesión exitosamente");
        } else if ("session_expired".equals(message)) {
            request.setAttribute("error", "Tu sesión ha expirado. Por favor, inicia sesión nuevamente");
        } else if ("access_denied".equals(message)) {
            request.setAttribute("error", "Debes iniciar sesión para acceder a esa página");
        }
        
        switch (path) {
            case "/register":
                request.getRequestDispatcher("/WEB-INF/jsp/auth/register.jsp").forward(request, response);
                break;
            case "/login":
            default:
                request.getRequestDispatcher("/WEB-INF/jsp/auth/login.jsp").forward(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        logger.debug("POST request to: " + path);
        
        switch (path) {
            case "/register":
                handleRegister(request, response);
                break;
            case "/login":
            default:
                handleLogin(request, response);
                break;
        }
    }
    
    /**
     * Maneja el proceso de login
     */
    private void handleLogin(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Email y contraseña son requeridos");
            request.getRequestDispatcher("/WEB-INF/jsp/auth/login.jsp").forward(request, response);
            return;
        }
        
        try {
            ApiService.AuthResponse authResponse = apiService.login(email.trim(), password);
            
            if (authResponse != null && authResponse.isSuccess() && authResponse.getUser() != null) {
                // Login exitoso - crear sesión
                User user = authResponse.getUser();
                HttpSession session = request.getSession(true);
                session.setAttribute("currentUser", user);
                session.setAttribute("userEmail", user.getEmail());
                session.setAttribute("authToken", authResponse.getToken());
                session.setMaxInactiveInterval(30 * 60); // 30 minutos
                
                logger.info("Login exitoso para usuario: " + email);
                
                // Redirigir al dashboard
                response.sendRedirect(request.getContextPath() + "/dashboard");
            } else {
                String errorMessage = (authResponse != null) ? authResponse.getMessage() : "Credenciales inválidas";
                request.setAttribute("error", errorMessage);
                request.setAttribute("email", email); // Mantener el email en el formulario
                request.getRequestDispatcher("/WEB-INF/jsp/auth/login.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            logger.error("Error durante login para " + email, e);
            request.setAttribute("error", "Error del servidor. Por favor, inténtalo más tarde.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/jsp/auth/login.jsp").forward(request, response);
        }
    }
    
    /**
     * Maneja el proceso de registro
     */
    private void handleRegister(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String headline = request.getParameter("headline");
        
        // Validaciones básicas
        if (firstName == null || firstName.trim().isEmpty() ||
            lastName == null || lastName.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            
            request.setAttribute("error", "Todos los campos obligatorios deben ser completados");
            preserveFormData(request, firstName, lastName, email, headline);
            request.getRequestDispatcher("/WEB-INF/jsp/auth/register.jsp").forward(request, response);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Las contraseñas no coinciden");
            preserveFormData(request, firstName, lastName, email, headline);
            request.getRequestDispatcher("/WEB-INF/jsp/auth/register.jsp").forward(request, response);
            return;
        }
        
        if (password.length() < 6) {
            request.setAttribute("error", "La contraseña debe tener al menos 6 caracteres");
            preserveFormData(request, firstName, lastName, email, headline);
            request.getRequestDispatcher("/WEB-INF/jsp/auth/register.jsp").forward(request, response);
            return;
        }
        
        try {
            ApiService.AuthResponse authResponse = apiService.register(firstName.trim(), lastName.trim(), email.trim(), password);
            
            if (authResponse != null && authResponse.isSuccess()) {
                logger.info("Registro exitoso para usuario: " + email);
                
                // Después del registro exitoso, hacer login automático
                ApiService.AuthResponse loginResponse = apiService.login(email.trim(), password);
                
                if (loginResponse != null && loginResponse.isSuccess() && loginResponse.getUser() != null) {
                    User user = loginResponse.getUser();
                    HttpSession session = request.getSession(true);
                    session.setAttribute("currentUser", user);
                    session.setAttribute("userEmail", user.getEmail());
                    session.setAttribute("authToken", loginResponse.getToken());
                    session.setMaxInactiveInterval(30 * 60); // 30 minutos
                    
                    // Redirigir al dashboard
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                } else {
                    // Registro exitoso pero login falló - redirigir a login manual
                    request.setAttribute("success", "Registro exitoso. Por favor, inicia sesión.");
                    request.getRequestDispatcher("/WEB-INF/jsp/auth/login.jsp").forward(request, response);
                }
            } else {
                String errorMessage = (authResponse != null) ? authResponse.getMessage() : "Error al registrar usuario. El email podría estar en uso.";
                request.setAttribute("error", errorMessage);
                preserveFormData(request, firstName, lastName, email, headline);
                request.getRequestDispatcher("/WEB-INF/jsp/auth/register.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            logger.error("Error durante registro para " + email, e);
            request.setAttribute("error", "Error del servidor. Por favor, inténtalo más tarde.");
            preserveFormData(request, firstName, lastName, email, headline);
            request.getRequestDispatcher("/WEB-INF/jsp/auth/register.jsp").forward(request, response);
        }
    }
    
    /**
     * Preserva los datos del formulario en caso de error
     */
    private void preserveFormData(HttpServletRequest request, String firstName, String lastName, 
                                 String email, String headline) {
        request.setAttribute("firstName", firstName);
        request.setAttribute("lastName", lastName);
        request.setAttribute("email", email);
        request.setAttribute("headline", headline);
    }
}