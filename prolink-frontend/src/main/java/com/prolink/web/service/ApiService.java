package com.prolink.web.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.prolink.web.model.Post;
import com.prolink.web.model.User;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio para comunicarse con el backend REST API
 */
public class ApiService {
    
    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);
    
    private static final String BACKEND_URL = "http://localhost:8080/api/v1";
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    public ApiService() {
        this.httpClient = HttpClients.createDefault();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }
    
    /**
     * Respuesta de autenticación
     */
    public static class AuthResponse {
        private boolean success;
        private String message;
        private String token;
        private User user;
        
        public AuthResponse() {}
        
        public AuthResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        // Getters y setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public User getUser() { return user; }
        public void setUser(User user) { this.user = user; }
    }
    
    /**
     * Login de usuario
     */
    public AuthResponse login(String email, String password) {
        try {
            Map<String, String> loginRequest = new HashMap<>();
            loginRequest.put("email", email);
            loginRequest.put("password", password);
            
            String jsonBody = objectMapper.writeValueAsString(loginRequest);
            
            HttpPost request = new HttpPost(BACKEND_URL + "/auth/signin");
            request.setHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
            
            String response = executeRequest(request);
            
            if (response != null) {
                Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);
                
                AuthResponse authResponse = new AuthResponse();
                authResponse.setSuccess(true);
                authResponse.setToken((String) responseMap.get("token"));
                
                // Convertir user data
                if (responseMap.containsKey("user")) {
                    Map<String, Object> userData = (Map<String, Object>) responseMap.get("user");
                    User user = objectMapper.convertValue(userData, User.class);
                    authResponse.setUser(user);
                }
                
                return authResponse;
            }
            
        } catch (Exception e) {
            logger.error("Error en login: ", e);
        }
        
        return new AuthResponse(false, "Error de autenticación");
    }
    
    /**
     * Registro de usuario
     */
    public AuthResponse register(String firstName, String lastName, String email, String password) {
        try {
            Map<String, String> registerRequest = new HashMap<>();
            registerRequest.put("firstName", firstName);
            registerRequest.put("lastName", lastName);
            registerRequest.put("email", email);
            registerRequest.put("password", password);
            
            String jsonBody = objectMapper.writeValueAsString(registerRequest);
            
            HttpPost request = new HttpPost(BACKEND_URL + "/auth/signup");
            request.setHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
            
            String response = executeRequest(request);
            
            if (response != null) {
                Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);
                
                AuthResponse authResponse = new AuthResponse();
                authResponse.setSuccess(true);
                authResponse.setMessage("Usuario registrado exitosamente");
                
                return authResponse;
            }
            
        } catch (Exception e) {
            logger.error("Error en registro: ", e);
        }
        
        return new AuthResponse(false, "Error en el registro");
    }
    
    /**
     * Obtener timeline de posts
     */
    public List<Post> getTimeline(String token) {
        try {
            HttpGet request = new HttpGet(BACKEND_URL + "/posts");
            request.setHeader("Authorization", "Bearer " + token);
            request.setHeader("Content-Type", "application/json");
            
            String response = executeRequest(request);
            
            if (response != null) {
                return objectMapper.readValue(response, new TypeReference<List<Post>>() {});
            }
            
        } catch (Exception e) {
            logger.error("Error al obtener timeline: ", e);
        }
        
        return Collections.emptyList();
    }
    
    /**
     * Crear nuevo post
     */
    public boolean createPost(String token, String content, String postType) {
        try {
            Map<String, String> postRequest = new HashMap<>();
            postRequest.put("content", content);
            postRequest.put("postType", postType != null ? postType : "TEXT");
            
            String jsonBody = objectMapper.writeValueAsString(postRequest);
            
            HttpPost request = new HttpPost(BACKEND_URL + "/posts");
            request.setHeader("Authorization", "Bearer " + token);
            request.setHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
            
            String response = executeRequest(request);
            return response != null;
            
        } catch (Exception e) {
            logger.error("Error al crear post: ", e);
            return false;
        }
    }
    
    /**
     * Obtener perfil de usuario
     */
    public User getUserProfile(String token) {
        try {
            HttpGet request = new HttpGet(BACKEND_URL + "/users/profile");
            request.setHeader("Authorization", "Bearer " + token);
            request.setHeader("Content-Type", "application/json");
            
            String response = executeRequest(request);
            
            if (response != null) {
                return objectMapper.readValue(response, User.class);
            }
            
        } catch (Exception e) {
            logger.error("Error al obtener perfil: ", e);
        }
        
        return null;
    }
    
    /**
     * Buscar usuarios
     */
    public List<User> searchUsers(String token, String query) {
        try {
            HttpGet request = new HttpGet(BACKEND_URL + "/users/search?query=" + query);
            request.setHeader("Authorization", "Bearer " + token);
            request.setHeader("Content-Type", "application/json");
            
            String response = executeRequest(request);
            
            if (response != null) {
                return objectMapper.readValue(response, new TypeReference<List<User>>() {});
            }
            
        } catch (Exception e) {
            logger.error("Error al buscar usuarios: ", e);
        }
        
        return Collections.emptyList();
    }
    
    /**
     * Verificar salud del backend
     */
    public boolean isBackendHealthy() {
        try {
            HttpGet request = new HttpGet(BACKEND_URL + "/auth/health");
            String response = executeRequest(request);
            return response != null;
        } catch (Exception e) {
            logger.error("Backend no disponible: ", e);
            return false;
        }
    }
    
    /**
     * Ejecutar petición HTTP
     */
    private String executeRequest(ClassicHttpRequest request) {
        try {
            return httpClient.execute(request, response -> {
                int status = response.getCode();
                HttpEntity entity = response.getEntity();
                
                if (status >= 200 && status < 300) {
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    logger.warn("HTTP request failed with status: " + status);
                    return null;
                }
            });
        } catch (IOException e) {
            logger.error("Error ejecutando petición HTTP: ", e);
            return null;
        }
    }
    
    /**
     * Obtener feed de posts
     */
    public String getFeed(String token) {
        HttpGet request = new HttpGet(BACKEND_URL + "/posts/feed");
        if (token != null) {
            request.setHeader("Authorization", "Bearer " + token);
        }
        return executeRequest(request);
    }
    
    /**
     * Obtener sugerencias de conexiones
     */
    public String getConnectionSuggestions(String token) {
        HttpGet request = new HttpGet(BACKEND_URL + "/connections/suggestions");
        if (token != null) {
            request.setHeader("Authorization", "Bearer " + token);
        }
        return executeRequest(request);
    }
    
    /**
     * Obtener estadísticas del usuario
     */
    public String getUserStats(Long userId, String token) {
        HttpGet request = new HttpGet(BACKEND_URL + "/users/" + userId + "/stats");
        if (token != null) {
            request.setHeader("Authorization", "Bearer " + token);
        }
        return executeRequest(request);
    }
    
    /**
     * Crear un post
     */
    public String createPost(Object postRequest, String token) {
        try {
            HttpPost post = new HttpPost(BACKEND_URL + "/posts");
            if (token != null) {
                post.setHeader("Authorization", "Bearer " + token);
            }
            String json = objectMapper.writeValueAsString(postRequest);
            post.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
            return executeRequest(post);
        } catch (Exception e) {
            logger.error("Error creando post: ", e);
            return null;
        }
    }
    
    /**
     * Dar like a un post
     */
    public String likePost(Long postId, String token) {
        HttpPost post = new HttpPost(BACKEND_URL + "/posts/" + postId + "/like");
        if (token != null) {
            post.setHeader("Authorization", "Bearer " + token);
        }
        return executeRequest(post);
    }
    
    /**
     * Comentar un post
     */
    public String commentPost(Long postId, Object commentRequest, String token) {
        try {
            HttpPost post = new HttpPost(BACKEND_URL + "/posts/" + postId + "/comments");
            if (token != null) {
                post.setHeader("Authorization", "Bearer " + token);
            }
            String json = objectMapper.writeValueAsString(commentRequest);
            post.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
            return executeRequest(post);
        } catch (Exception e) {
            logger.error("Error comentando post: ", e);
            return null;
        }
    }
    
    /**
     * Eliminar un post
     */
    public String deletePost(Long postId, String token) {
        try {
            // Usar POST con parámetro para simular DELETE
            HttpPost post = new HttpPost(BACKEND_URL + "/posts/" + postId + "/delete");
            if (token != null) {
                post.setHeader("Authorization", "Bearer " + token);
            }
            return executeRequest(post);
        } catch (Exception e) {
            logger.error("Error eliminando post: ", e);
            return null;
        }
    }
    
    /**
     * Método auxiliar para parsear JSON array
     */
    public <T> List<T> parseJsonArray(String json, TypeReference<List<T>> typeRef) {
        try {
            if (json == null || json.trim().isEmpty()) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(json, typeRef);
        } catch (Exception e) {
            logger.error("Error parseando JSON array: " + json, e);
            return new ArrayList<>();
        }
    }
    
    /**
     * Método auxiliar para parsear JSON object
     */
    public <T> T parseJson(String json, Class<T> clazz) {
        try {
            if (json == null || json.trim().isEmpty()) {
                return null;
            }
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            logger.error("Error parseando JSON: " + json, e);
            return null;
        }
    }
    
    /**
     * Cerrar recursos
     */
    public void close() {
        try {
            httpClient.close();
        } catch (IOException e) {
            logger.error("Error cerrando cliente HTTP: ", e);
        }
    }
}