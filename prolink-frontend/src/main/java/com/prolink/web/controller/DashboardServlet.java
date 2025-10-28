package com.prolink.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.prolink.web.model.User;
import com.prolink.web.model.Post;
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
 * Servlet principal del Dashboard de ProLink
 * Maneja la página principal con feed de posts, creación de posts y actividades
 */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(DashboardServlet.class);
    private ApiService apiService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.apiService = new ApiService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String token = (String) session.getAttribute("token");
        User currentUser = (User) session.getAttribute("user");
        
        if (token == null || currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
            // Cargar el feed de posts
            loadFeed(request, token);
            
            // Cargar sugerencias de conexiones
            loadConnectionSuggestions(request, token);
            
            // Cargar estadísticas del usuario
            loadUserStats(request, currentUser, token);
            
            request.getRequestDispatcher("/WEB-INF/jsp/dashboard/dashboard.jsp")
                   .forward(request, response);
            
        } catch (Exception e) {
            logger.error("Error cargando dashboard", e);
            request.setAttribute("error", "Error cargando el dashboard: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/dashboard/dashboard.jsp")
                   .forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String token = (String) session.getAttribute("token");
        User currentUser = (User) session.getAttribute("user");
        
        if (token == null || currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");
        
        try {
            switch (action != null ? action : "") {
                case "createPost":
                    handleCreatePost(request, response, token);
                    break;
                case "likePost":
                    handleLikePost(request, response, token);
                    break;
                case "commentPost":
                    handleCommentPost(request, response, token);
                    break;
                case "deletePost":
                    handleDeletePost(request, response, token);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/dashboard");
            }
        } catch (Exception e) {
            logger.error("Error procesando acción: " + action, e);
            request.setAttribute("error", "Error: " + e.getMessage());
            doGet(request, response);
        }
    }
    
    private void loadFeed(HttpServletRequest request, String token) {
        try {
            String feedResponse = apiService.getFeed(token);
            
            if (feedResponse != null && !feedResponse.trim().isEmpty()) {
                List<Post> posts = apiService.parseJsonArray(feedResponse, new TypeReference<List<Post>>() {});
                request.setAttribute("posts", posts != null ? posts : new ArrayList<>());
            } else {
                // Datos de ejemplo si no hay respuesta del API
                List<Post> samplePosts = createSamplePosts();
                request.setAttribute("posts", samplePosts);
            }
            
        } catch (Exception e) {
            logger.warn("Error cargando feed, usando datos de ejemplo", e);
            request.setAttribute("posts", createSamplePosts());
        }
    }
    
    private void loadConnectionSuggestions(HttpServletRequest request, String token) {
        try {
            String suggestionsResponse = apiService.getConnectionSuggestions(token);
            
            if (suggestionsResponse != null && !suggestionsResponse.trim().isEmpty()) {
                List<User> suggestions = apiService.parseJsonArray(suggestionsResponse, new TypeReference<List<User>>() {});
                request.setAttribute("suggestions", suggestions != null ? suggestions : new ArrayList<>());
            } else {
                request.setAttribute("suggestions", createSampleSuggestions());
            }
            
        } catch (Exception e) {
            logger.warn("Error cargando sugerencias", e);
            request.setAttribute("suggestions", createSampleSuggestions());
        }
    }
    
    private void loadUserStats(HttpServletRequest request, User currentUser, String token) {
        try {
            // Cargar estadísticas del perfil
            String statsResponse = apiService.getUserStats(currentUser.getId(), token);
            
            if (statsResponse != null && !statsResponse.trim().isEmpty()) {
                UserStats stats = apiService.parseJson(statsResponse, UserStats.class);
                request.setAttribute("userStats", stats);
            } else {
                // Estadísticas por defecto
                UserStats defaultStats = new UserStats();
                defaultStats.connectionsCount = 150;
                defaultStats.profileViews = 45;
                defaultStats.postImpressions = 1250;
                request.setAttribute("userStats", defaultStats);
            }
            
        } catch (Exception e) {
            logger.warn("Error cargando estadísticas de usuario", e);
            UserStats defaultStats = new UserStats();
            defaultStats.connectionsCount = 0;
            defaultStats.profileViews = 0;
            defaultStats.postImpressions = 0;
            request.setAttribute("userStats", defaultStats);
        }
    }
    
    private void handleCreatePost(HttpServletRequest request, HttpServletResponse response, String token) 
            throws IOException, ServletException {
        
        String content = request.getParameter("content");
        String postType = request.getParameter("postType");
        
        if (content == null || content.trim().isEmpty()) {
            request.setAttribute("error", "El contenido del post no puede estar vacío");
            doGet(request, response);
            return;
        }
        
        try {
            CreatePostRequest postRequest = new CreatePostRequest();
            postRequest.content = content.trim();
            postRequest.postType = postType != null ? postType : "TEXT";
            
            String result = apiService.createPost(postRequest, token);
            
            if (result != null && result.contains("success")) {
                request.setAttribute("success", "Post creado exitosamente");
            } else {
                request.setAttribute("error", "Error creando el post");
            }
            
        } catch (Exception e) {
            logger.error("Error creando post", e);
            request.setAttribute("error", "Error creando el post: " + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }
    
    private void handleLikePost(HttpServletRequest request, HttpServletResponse response, String token) 
            throws IOException {
        
        String postId = request.getParameter("postId");
        
        if (postId == null || postId.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de post requerido");
            return;
        }
        
        try {
            String result = apiService.likePost(Long.parseLong(postId), token);
            
            // Responder con JSON para AJAX
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true, \"message\": \"Like actualizado\"}");
            
        } catch (Exception e) {
            logger.error("Error procesando like", e);
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Error procesando like\"}");
        }
    }
    
    private void handleCommentPost(HttpServletRequest request, HttpServletResponse response, String token) 
            throws IOException, ServletException {
        
        String postId = request.getParameter("postId");
        String comment = request.getParameter("comment");
        
        if (postId == null || comment == null || comment.trim().isEmpty()) {
            request.setAttribute("error", "Post ID y comentario son requeridos");
            doGet(request, response);
            return;
        }
        
        try {
            CommentRequest commentRequest = new CommentRequest();
            commentRequest.content = comment.trim();
            
            String result = apiService.commentPost(Long.parseLong(postId), commentRequest, token);
            
            if (result != null && result.contains("success")) {
                request.setAttribute("success", "Comentario agregado exitosamente");
            } else {
                request.setAttribute("error", "Error agregando comentario");
            }
            
        } catch (Exception e) {
            logger.error("Error agregando comentario", e);
            request.setAttribute("error", "Error agregando comentario: " + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }
    
    private void handleDeletePost(HttpServletRequest request, HttpServletResponse response, String token) 
            throws IOException {
        
        String postId = request.getParameter("postId");
        
        if (postId == null || postId.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de post requerido");
            return;
        }
        
        try {
            String result = apiService.deletePost(Long.parseLong(postId), token);
            
            if (result != null && result.contains("success")) {
                request.getSession().setAttribute("success", "Post eliminado exitosamente");
            } else {
                request.getSession().setAttribute("error", "Error eliminando el post");
            }
            
        } catch (Exception e) {
            logger.error("Error eliminando post", e);
            request.getSession().setAttribute("error", "Error eliminando el post: " + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }
    
    // Métodos para crear datos de ejemplo
    private List<Post> createSamplePosts() {
        List<Post> posts = new ArrayList<>();
        
        Post post1 = new Post();
        post1.setId(1L);
        post1.setContent("¡Bienvenidos a ProLink! Esta es una red social profesional construida con Java puro y tecnologías modernas. #ProLink #Java #LinkedInClone");
        post1.setAuthor(createSampleUser("Juan", "Pérez", "Desarrollador Full Stack"));
        post1.setCreatedAt(LocalDateTime.now().minusHours(2));
        post1.setLikesCount(15);
        post1.setCommentsCount(3);
        posts.add(post1);
        
        Post post2 = new Post();
        post2.setId(2L);
        post2.setContent("Acabo de terminar un proyecto increíble usando Spring Boot y React. La combinación perfecta para aplicaciones modernas. ¿Qué tecnologías están usando ustedes?");
        post2.setAuthor(createSampleUser("María", "García", "Software Engineer en TechCorp"));
        post2.setCreatedAt(LocalDateTime.now().minusHours(5));
        post2.setLikesCount(28);
        post2.setCommentsCount(7);
        posts.add(post2);
        
        Post post3 = new Post();
        post3.setId(3L);
        post3.setContent("Tips para una entrevista técnica exitosa:\n1. Practica algoritmos diariamente\n2. Entiende los fundamentos\n3. Comunica tu proceso de pensamiento\n4. Haz preguntas inteligentes\n\n¿Qué otros consejos agregarían?");
        post3.setAuthor(createSampleUser("Carlos", "López", "Tech Lead en StartupXYZ"));
        post3.setCreatedAt(LocalDateTime.now().minusHours(8));
        post3.setLikesCount(42);
        post3.setCommentsCount(12);
        posts.add(post3);
        
        return posts;
    }
    
    private List<User> createSampleSuggestions() {
        List<User> suggestions = new ArrayList<>();
        
        suggestions.add(createSampleUser("Ana", "Rodríguez", "UX/UI Designer"));
        suggestions.add(createSampleUser("Luis", "Martínez", "DevOps Engineer"));
        suggestions.add(createSampleUser("Carmen", "Torres", "Product Manager"));
        suggestions.add(createSampleUser("Diego", "Morales", "Data Scientist"));
        
        return suggestions;
    }
    
    private User createSampleUser(String firstName, String lastName, String headline) {
        User user = new User();
        user.setId((long)(Math.random() * 1000000));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setHeadline(headline);
        user.setEmail(firstName.toLowerCase() + "." + lastName.toLowerCase() + "@prolink.com");
        return user;
    }
    
    // Clases internas para requests
    public static class CreatePostRequest {
        public String content;
        public String postType;
    }
    
    public static class CommentRequest {
        public String content;
    }
    
    public static class UserStats {
        public int connectionsCount;
        public int profileViews;
        public int postImpressions;
    }
}