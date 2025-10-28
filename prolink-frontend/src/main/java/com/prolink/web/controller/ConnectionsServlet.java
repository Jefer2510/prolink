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
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet para manejo de conexiones y red de contactos
 * Permite ver conexiones, sugerencias y gestionar solicitudes
 */
@WebServlet(name = "ConnectionsServlet", urlPatterns = {"/connections", "/connections/*"})
public class ConnectionsServlet extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(ConnectionsServlet.class);
    
    private ApiService apiService;
    
    @Override
    public void init() throws ServletException {
        apiService = new ApiService();
        logger.info("ConnectionsServlet inicializado");
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
                // Mostrar página principal de conexiones
                showConnectionsPage(request, response, currentUser);
            } else if (pathInfo.equals("/suggestions")) {
                // Mostrar sugerencias de conexiones
                showSuggestionsPage(request, response, currentUser);
            } else if (pathInfo.equals("/pending")) {
                // Mostrar solicitudes pendientes
                showPendingRequestsPage(request, response, currentUser);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Página no encontrada");
            }
        } catch (Exception e) {
            logger.error("Error en ConnectionsServlet", e);
            request.setAttribute("error", "Error al cargar las conexiones");
            showConnectionsPage(request, response, currentUser);
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
        
        switch (action != null ? action : "") {
            case "sendRequest":
                handleSendRequest(request, response);
                break;
            case "acceptRequest":
                handleAcceptRequest(request, response);
                break;
            case "rejectRequest":
                handleRejectRequest(request, response);
                break;
            case "removeConnection":
                handleRemoveConnection(request, response);
                break;
            case "searchUsers":
                handleSearchUsers(request, response);
                break;
            default:
                logger.warn("Acción desconocida: " + action);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
        }
    }
    
    /**
     * Muestra la página principal de conexiones
     */
    private void showConnectionsPage(HttpServletRequest request, HttpServletResponse response, User currentUser) 
            throws ServletException, IOException {
        
        try {
            // TODO: Obtener conexiones del usuario desde la API
            List<User> connections = new ArrayList<>(); // Por ahora vacía
            
            // TODO: Obtener solicitudes pendientes
            List<User> pendingRequests = new ArrayList<>();
            
            // TODO: Obtener algunas sugerencias
            List<User> suggestions = getSampleSuggestions();
            
            request.setAttribute("currentUser", currentUser);
            request.setAttribute("connections", connections);
            request.setAttribute("pendingRequests", pendingRequests);
            request.setAttribute("suggestions", suggestions);
            request.setAttribute("activeTab", "connections");
            
        } catch (Exception e) {
            logger.error("Error al cargar conexiones para usuario: " + currentUser.getEmail(), e);
            request.setAttribute("connections", new ArrayList<>());
            request.setAttribute("pendingRequests", new ArrayList<>());
            request.setAttribute("suggestions", new ArrayList<>());
        }
        
        request.getRequestDispatcher("/WEB-INF/jsp/dashboard/connections.jsp").forward(request, response);
    }
    
    /**
     * Muestra la página de sugerencias
     */
    private void showSuggestionsPage(HttpServletRequest request, HttpServletResponse response, User currentUser) 
            throws ServletException, IOException {
        
        try {
            // TODO: Obtener sugerencias desde la API
            List<User> suggestions = getSampleSuggestions();
            
            request.setAttribute("currentUser", currentUser);
            request.setAttribute("suggestions", suggestions);
            request.setAttribute("activeTab", "suggestions");
            
        } catch (Exception e) {
            logger.error("Error al cargar sugerencias para usuario: " + currentUser.getEmail(), e);
            request.setAttribute("suggestions", new ArrayList<>());
        }
        
        request.getRequestDispatcher("/WEB-INF/jsp/dashboard/connections.jsp").forward(request, response);
    }
    
    /**
     * Muestra la página de solicitudes pendientes
     */
    private void showPendingRequestsPage(HttpServletRequest request, HttpServletResponse response, User currentUser) 
            throws ServletException, IOException {
        
        try {
            // TODO: Obtener solicitudes pendientes desde la API
            List<User> pendingRequests = new ArrayList<>();
            
            request.setAttribute("currentUser", currentUser);
            request.setAttribute("pendingRequests", pendingRequests);
            request.setAttribute("activeTab", "pending");
            
        } catch (Exception e) {
            logger.error("Error al cargar solicitudes pendientes para usuario: " + currentUser.getEmail(), e);
            request.setAttribute("pendingRequests", new ArrayList<>());
        }
        
        request.getRequestDispatcher("/WEB-INF/jsp/dashboard/connections.jsp").forward(request, response);
    }
    
    /**
     * Maneja el envío de solicitud de conexión
     */
    private void handleSendRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String userIdStr = request.getParameter("userId");
        
        try {
            Long userId = Long.parseLong(userIdStr);
            
            // TODO: Enviar solicitud de conexión a través de la API
            logger.info("Enviando solicitud de conexión a usuario: " + userId);
            
            // Simular éxito por ahora
            request.setAttribute("success", "Solicitud de conexión enviada");
            
        } catch (NumberFormatException e) {
            logger.error("ID de usuario inválido: " + userIdStr, e);
            request.setAttribute("error", "ID de usuario inválido");
        } catch (Exception e) {
            logger.error("Error al enviar solicitud de conexión", e);
            request.setAttribute("error", "Error al enviar la solicitud");
        }
        
        // Redirigir de vuelta a la página de conexiones
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        showConnectionsPage(request, response, currentUser);
    }
    
    /**
     * Maneja la aceptación de solicitud de conexión
     */
    private void handleAcceptRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String userIdStr = request.getParameter("userId");
        
        try {
            Long userId = Long.parseLong(userIdStr);
            
            // TODO: Aceptar solicitud a través de la API
            logger.info("Aceptando solicitud de conexión de usuario: " + userId);
            
            request.setAttribute("success", "Solicitud de conexión aceptada");
            
        } catch (NumberFormatException e) {
            logger.error("ID de usuario inválido: " + userIdStr, e);
            request.setAttribute("error", "ID de usuario inválido");
        } catch (Exception e) {
            logger.error("Error al aceptar solicitud de conexión", e);
            request.setAttribute("error", "Error al aceptar la solicitud");
        }
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        showPendingRequestsPage(request, response, currentUser);
    }
    
    /**
     * Maneja el rechazo de solicitud de conexión
     */
    private void handleRejectRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String userIdStr = request.getParameter("userId");
        
        try {
            Long userId = Long.parseLong(userIdStr);
            
            // TODO: Rechazar solicitud a través de la API
            logger.info("Rechazando solicitud de conexión de usuario: " + userId);
            
            request.setAttribute("success", "Solicitud de conexión rechazada");
            
        } catch (NumberFormatException e) {
            logger.error("ID de usuario inválido: " + userIdStr, e);
            request.setAttribute("error", "ID de usuario inválido");
        } catch (Exception e) {
            logger.error("Error al rechazar solicitud de conexión", e);
            request.setAttribute("error", "Error al rechazar la solicitud");
        }
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        showPendingRequestsPage(request, response, currentUser);
    }
    
    /**
     * Maneja la eliminación de una conexión existente
     */
    private void handleRemoveConnection(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String userIdStr = request.getParameter("userId");
        
        try {
            Long userId = Long.parseLong(userIdStr);
            
            // TODO: Eliminar conexión a través de la API
            logger.info("Eliminando conexión con usuario: " + userId);
            
            request.setAttribute("success", "Conexión eliminada");
            
        } catch (NumberFormatException e) {
            logger.error("ID de usuario inválido: " + userIdStr, e);
            request.setAttribute("error", "ID de usuario inválido");
        } catch (Exception e) {
            logger.error("Error al eliminar conexión", e);
            request.setAttribute("error", "Error al eliminar la conexión");
        }
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        showConnectionsPage(request, response, currentUser);
    }
    
    /**
     * Maneja la búsqueda de usuarios
     */
    private void handleSearchUsers(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String searchTerm = request.getParameter("searchTerm");
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        try {
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                // Obtener el token de la sesión
                String token = (String) session.getAttribute("authToken");
                
                if (token != null) {
                    // Buscar usuarios a través de la API
                    List<User> searchResults = apiService.searchUsers(token, searchTerm.trim());
                    request.setAttribute("searchResults", searchResults);
                } else {
                    // Si no hay token, crear lista vacía
                    request.setAttribute("searchResults", new ArrayList<>());
                    request.setAttribute("error", "Sesión expirada. Por favor, inicia sesión nuevamente.");
                }
                
                request.setAttribute("searchTerm", searchTerm.trim());
            }
            
        } catch (Exception e) {
            logger.error("Error al buscar usuarios con término: " + searchTerm, e);
            request.setAttribute("error", "Error al realizar la búsqueda");
            request.setAttribute("searchResults", new ArrayList<>());
        }
        
        request.setAttribute("currentUser", currentUser);
        request.setAttribute("activeTab", "search");
        
        request.getRequestDispatcher("/WEB-INF/jsp/dashboard/connections.jsp").forward(request, response);
    }
    
    /**
     * Obtiene sugerencias de muestra (temporal)
     */
    private List<User> getSampleSuggestions() {
        List<User> suggestions = new ArrayList<>();
        
        // Crear algunos usuarios de muestra
        User user1 = new User();
        user1.setId(101L);
        user1.setFirstName("Ana");
        user1.setLastName("García");
        user1.setEmail("ana.garcia@example.com");
        user1.setHeadline("Desarrolladora Frontend en TechCorp");
        suggestions.add(user1);
        
        User user2 = new User();
        user2.setId(102L);
        user2.setFirstName("Carlos");
        user2.setLastName("López");
        user2.setEmail("carlos.lopez@example.com");
        user2.setHeadline("Product Manager en StartupXYZ");
        suggestions.add(user2);
        
        User user3 = new User();
        user3.setId(103L);
        user3.setFirstName("María");
        user3.setLastName("Rodríguez");
        user3.setEmail("maria.rodriguez@example.com");
        user3.setHeadline("UX/UI Designer");
        suggestions.add(user3);
        
        return suggestions;
    }
}