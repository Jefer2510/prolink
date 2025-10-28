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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet para manejo del sistema de mensajería y chat
 * Gestiona conversaciones, mensajes y chat en tiempo real
 */
@WebServlet(name = "MessagesServlet", urlPatterns = {"/messages", "/messages/*"})
public class MessagesServlet extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(MessagesServlet.class);
    
    private ApiService apiService;
    
    @Override
    public void init() throws ServletException {
        apiService = new ApiService();
        logger.info("MessagesServlet inicializado");
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
                // Mostrar lista de conversaciones
                showConversationsPage(request, response, currentUser);
            } else if (pathInfo.startsWith("/chat/")) {
                // Mostrar chat con usuario específico
                String[] pathParts = pathInfo.split("/");
                if (pathParts.length > 2) {
                    try {
                        Long userId = Long.parseLong(pathParts[2]);
                        showChatPage(request, response, currentUser, userId);
                    } catch (NumberFormatException e) {
                        logger.warn("ID de usuario inválido: " + pathParts[2]);
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de usuario inválido");
                        return;
                    }
                }
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Página no encontrada");
            }
        } catch (Exception e) {
            logger.error("Error en MessagesServlet", e);
            request.setAttribute("error", "Error al cargar los mensajes");
            showConversationsPage(request, response, currentUser);
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
            case "sendMessage":
                handleSendMessage(request, response);
                break;
            case "markAsRead":
                handleMarkAsRead(request, response);
                break;
            case "deleteConversation":
                handleDeleteConversation(request, response);
                break;
            case "searchConversations":
                handleSearchConversations(request, response);
                break;
            default:
                logger.warn("Acción desconocida: " + action);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
        }
    }
    
    /**
     * Muestra la página principal de conversaciones
     */
    private void showConversationsPage(HttpServletRequest request, HttpServletResponse response, User currentUser) 
            throws ServletException, IOException {
        
        try {
            // TODO: Obtener conversaciones del usuario desde la API
            List<Conversation> conversations = getSampleConversations(currentUser);
            
            request.setAttribute("currentUser", currentUser);
            request.setAttribute("conversations", conversations);
            request.setAttribute("pageType", "conversations");
            
        } catch (Exception e) {
            logger.error("Error al cargar conversaciones para usuario: " + currentUser.getEmail(), e);
            request.setAttribute("conversations", new ArrayList<>());
        }
        
        request.getRequestDispatcher("/WEB-INF/jsp/dashboard/messages.jsp").forward(request, response);
    }
    
    /**
     * Muestra la página de chat con un usuario específico
     */
    private void showChatPage(HttpServletRequest request, HttpServletResponse response, 
                             User currentUser, Long otherUserId) 
            throws ServletException, IOException {
        
        try {
            // TODO: Obtener información del otro usuario
            User otherUser = getSampleUser(otherUserId);
            
            // TODO: Obtener mensajes de la conversación desde la API
            List<Message> messages = getSampleMessages(currentUser.getId(), otherUserId);
            
            request.setAttribute("currentUser", currentUser);
            request.setAttribute("otherUser", otherUser);
            request.setAttribute("messages", messages);
            request.setAttribute("pageType", "chat");
            
        } catch (Exception e) {
            logger.error("Error al cargar chat para usuarios: " + currentUser.getId() + " - " + otherUserId, e);
            request.setAttribute("messages", new ArrayList<>());
            // Crear usuario por defecto si no se encuentra
            User defaultUser = new User();
            defaultUser.setId(otherUserId);
            defaultUser.setFirstName("Usuario");
            defaultUser.setLastName("Desconocido");
            request.setAttribute("otherUser", defaultUser);
        }
        
        request.getRequestDispatcher("/WEB-INF/jsp/dashboard/messages.jsp").forward(request, response);
    }
    
    /**
     * Maneja el envío de un nuevo mensaje
     */
    private void handleSendMessage(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        String receiverIdStr = request.getParameter("receiverId");
        String messageContent = request.getParameter("content");
        
        try {
            Long receiverId = Long.parseLong(receiverIdStr);
            
            if (messageContent == null || messageContent.trim().isEmpty()) {
                request.setAttribute("error", "El mensaje no puede estar vacío");
                showChatPage(request, response, currentUser, receiverId);
                return;
            }
            
            // TODO: Enviar mensaje a través de la API
            logger.info("Enviando mensaje de usuario {} a usuario {}: {}", 
                       currentUser.getId(), receiverId, messageContent);
            
            // TODO: También enviar a través de WebSocket para tiempo real
            
            request.setAttribute("success", "Mensaje enviado");
            showChatPage(request, response, currentUser, receiverId);
            
        } catch (NumberFormatException e) {
            logger.error("ID de receptor inválido: " + receiverIdStr, e);
            request.setAttribute("error", "ID de receptor inválido");
            showConversationsPage(request, response, currentUser);
        } catch (Exception e) {
            logger.error("Error al enviar mensaje", e);
            request.setAttribute("error", "Error al enviar el mensaje");
            showConversationsPage(request, response, currentUser);
        }
    }
    
    /**
     * Marca una conversación como leída
     */
    private void handleMarkAsRead(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String conversationIdStr = request.getParameter("conversationId");
        
        try {
            Long conversationId = Long.parseLong(conversationIdStr);
            
            // TODO: Marcar como leída a través de la API
            logger.info("Marcando conversación {} como leída", conversationId);
            
            // Responder con JSON para AJAX
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true}");
            
        } catch (NumberFormatException e) {
            logger.error("ID de conversación inválido: " + conversationIdStr, e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"ID inválido\"}");
        } catch (Exception e) {
            logger.error("Error al marcar como leída", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error interno\"}");
        }
    }
    
    /**
     * Elimina una conversación
     */
    private void handleDeleteConversation(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String conversationIdStr = request.getParameter("conversationId");
        
        try {
            Long conversationId = Long.parseLong(conversationIdStr);
            
            // TODO: Eliminar conversación a través de la API
            logger.info("Eliminando conversación {}", conversationId);
            
            request.setAttribute("success", "Conversación eliminada");
            
        } catch (NumberFormatException e) {
            logger.error("ID de conversación inválido: " + conversationIdStr, e);
            request.setAttribute("error", "ID de conversación inválido");
        } catch (Exception e) {
            logger.error("Error al eliminar conversación", e);
            request.setAttribute("error", "Error al eliminar la conversación");
        }
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        showConversationsPage(request, response, currentUser);
    }
    
    /**
     * Busca conversaciones
     */
    private void handleSearchConversations(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String searchTerm = request.getParameter("searchTerm");
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        try {
            List<Conversation> conversations;
            
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                // TODO: Buscar conversaciones a través de la API
                conversations = getSampleConversations(currentUser); // Por ahora usar datos de muestra
                request.setAttribute("searchTerm", searchTerm.trim());
            } else {
                conversations = getSampleConversations(currentUser);
            }
            
            request.setAttribute("conversations", conversations);
            
        } catch (Exception e) {
            logger.error("Error al buscar conversaciones", e);
            request.setAttribute("error", "Error al realizar la búsqueda");
            request.setAttribute("conversations", new ArrayList<>());
        }
        
        request.setAttribute("currentUser", currentUser);
        request.setAttribute("pageType", "conversations");
        request.getRequestDispatcher("/WEB-INF/jsp/dashboard/messages.jsp").forward(request, response);
    }
    
    /**
     * Obtiene conversaciones de muestra (temporal)
     */
    private List<Conversation> getSampleConversations(User currentUser) {
        List<Conversation> conversations = new ArrayList<>();
        
        // Crear conversaciones de muestra
        Conversation conv1 = new Conversation();
        conv1.setId(1L);
        conv1.setOtherUser(createSampleUser(101L, "Ana", "García", "Desarrolladora Frontend"));
        conv1.setLastMessage("Hola, ¿cómo estás?");
        conv1.setLastMessageTime(LocalDateTime.now().minusMinutes(30));
        conv1.setUnreadCount(2);
        conversations.add(conv1);
        
        Conversation conv2 = new Conversation();
        conv2.setId(2L);
        conv2.setOtherUser(createSampleUser(102L, "Carlos", "López", "Product Manager"));
        conv2.setLastMessage("Perfecto, nos vemos mañana");
        conv2.setLastMessageTime(LocalDateTime.now().minusHours(2));
        conv2.setUnreadCount(0);
        conversations.add(conv2);
        
        Conversation conv3 = new Conversation();
        conv3.setId(3L);
        conv3.setOtherUser(createSampleUser(103L, "María", "Rodríguez", "UX/UI Designer"));
        conv3.setLastMessage("Gracias por la información");
        conv3.setLastMessageTime(LocalDateTime.now().minusHours(24));
        conv3.setUnreadCount(0);
        conversations.add(conv3);
        
        return conversations;
    }
    
    /**
     * Obtiene mensajes de muestra (temporal)
     */
    private List<Message> getSampleMessages(Long currentUserId, Long otherUserId) {
        List<Message> messages = new ArrayList<>();
        
        Message msg1 = new Message();
        msg1.setId(1L);
        msg1.setSenderId(otherUserId);
        msg1.setReceiverId(currentUserId);
        msg1.setContent("¡Hola! ¿Cómo estás?");
        msg1.setSentAt(LocalDateTime.now().minusHours(2));
        msg1.setRead(true);
        messages.add(msg1);
        
        Message msg2 = new Message();
        msg2.setId(2L);
        msg2.setSenderId(currentUserId);
        msg2.setReceiverId(otherUserId);
        msg2.setContent("¡Hola! Todo bien, gracias. ¿Y tú?");
        msg2.setSentAt(LocalDateTime.now().minusHours(1).minusMinutes(30));
        msg2.setRead(true);
        messages.add(msg2);
        
        Message msg3 = new Message();
        msg3.setId(3L);
        msg3.setSenderId(otherUserId);
        msg3.setReceiverId(currentUserId);
        msg3.setContent("Muy bien también. ¿Tienes tiempo para una llamada esta tarde?");
        msg3.setSentAt(LocalDateTime.now().minusMinutes(30));
        msg3.setRead(false);
        messages.add(msg3);
        
        return messages;
    }
    
    /**
     * Crea un usuario de muestra
     */
    private User createSampleUser(Long id, String firstName, String lastName, String headline) {
        User user = new User();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setHeadline(headline);
        user.setEmail(firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com");
        return user;
    }
    
    /**
     * Obtiene un usuario de muestra por ID
     */
    private User getSampleUser(Long userId) {
        switch (userId.intValue()) {
            case 101:
                return createSampleUser(101L, "Ana", "García", "Desarrolladora Frontend en TechCorp");
            case 102:
                return createSampleUser(102L, "Carlos", "López", "Product Manager en StartupXYZ");
            case 103:
                return createSampleUser(103L, "María", "Rodríguez", "UX/UI Designer");
            default:
                return createSampleUser(userId, "Usuario", "Desconocido", "Profesional en ProLink");
        }
    }
    
    // Clases internas para estructuras de datos temporales
    public static class Conversation {
        private Long id;
        private User otherUser;
        private String lastMessage;
        private LocalDateTime lastMessageTime;
        private int unreadCount;
        
        // Getters y setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public User getOtherUser() { return otherUser; }
        public void setOtherUser(User otherUser) { this.otherUser = otherUser; }
        public String getLastMessage() { return lastMessage; }
        public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }
        public LocalDateTime getLastMessageTime() { return lastMessageTime; }
        public void setLastMessageTime(LocalDateTime lastMessageTime) { this.lastMessageTime = lastMessageTime; }
        public int getUnreadCount() { return unreadCount; }
        public void setUnreadCount(int unreadCount) { this.unreadCount = unreadCount; }
        
        public String getFormattedTime() {
            if (lastMessageTime == null) return "";
            
            LocalDateTime now = LocalDateTime.now();
            long hoursAgo = java.time.Duration.between(lastMessageTime, now).toHours();
            
            if (hoursAgo < 1) {
                return "Hace " + java.time.Duration.between(lastMessageTime, now).toMinutes() + " min";
            } else if (hoursAgo < 24) {
                return "Hace " + hoursAgo + "h";
            } else {
                return lastMessageTime.format(DateTimeFormatter.ofPattern("dd/MM"));
            }
        }
    }
    
    public static class Message {
        private Long id;
        private Long senderId;
        private Long receiverId;
        private String content;
        private LocalDateTime sentAt;
        private boolean read;
        
        // Getters y setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getSenderId() { return senderId; }
        public void setSenderId(Long senderId) { this.senderId = senderId; }
        public Long getReceiverId() { return receiverId; }
        public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public LocalDateTime getSentAt() { return sentAt; }
        public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
        public boolean isRead() { return read; }
        public void setRead(boolean read) { this.read = read; }
        
        public String getFormattedTime() {
            if (sentAt == null) return "";
            return sentAt.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
    }
}