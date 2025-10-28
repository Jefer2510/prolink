<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es" data-user-id="${currentUser.id}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ProLink - Dashboard</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <style>
        body {
            background-color: #f3f2ef;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .navbar-brand {
            color: #0a66c2 !important;
            font-weight: bold;
            font-size: 1.5rem;
        }
        .profile-card, .post-card, .suggestions-card {
            background: white;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            margin-bottom: 1rem;
        }
        .profile-avatar {
            width: 80px;
            height: 80px;
            background: linear-gradient(135deg, #0a66c2, #004182);
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 2rem;
            font-weight: bold;
            border-radius: 50%;
        }
        .small-avatar {
            width: 40px;
            height: 40px;
            background: linear-gradient(135deg, #0a66c2, #004182);
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1rem;
            font-weight: bold;
            border-radius: 50%;
        }
        .btn-linkedin {
            background: linear-gradient(135deg, #0a66c2, #004182);
            border: none;
            color: white;
        }
        .btn-linkedin:hover {
            background: linear-gradient(135deg, #004182, #0a66c2);
            color: white;
        }
        .post-actions button {
            border: none;
            background: transparent;
            padding: 0.5rem 1rem;
            border-radius: 5px;
            transition: all 0.2s;
        }
        .post-actions button:hover {
            background-color: #f3f2ef;
        }
        .create-post-textarea {
            border: none;
            resize: none;
            outline: none;
        }
        .create-post-textarea::placeholder {
            color: #666;
        }
        .suggestion-item {
            border-bottom: 1px solid #eee;
            padding: 1rem;
        }
        .suggestion-item:last-child {
            border-bottom: none;
        }
        .navbar {
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .content-area {
            margin-top: 2rem;
        }
    </style>
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar navbar-expand-lg navbar-light bg-white sticky-top">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/dashboard">
                <i class="fab fa-linkedin me-2"></i>ProLink
            </a>
            
            <div class="navbar-nav ms-auto d-flex flex-row">
                <div class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" role="button" 
                       data-bs-toggle="dropdown" aria-expanded="false">
                        <div class="small-avatar me-2">
                            ${currentUser.firstName.charAt(0)}${currentUser.lastName.charAt(0)}
                        </div>
                        <span class="d-none d-md-inline">${currentUser.firstName}</span>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile">
                            <i class="fas fa-user me-2"></i>Mi Perfil</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/messages">
                            <i class="fas fa-comments me-2"></i>Mensajes</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/connections">
                            <i class="fas fa-users me-2"></i>Mi Red</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout">
                            <i class="fas fa-sign-out-alt me-2"></i>Cerrar Sesión</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </nav>

    <div class="container content-area">
        <!-- Mostrar alertas -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-circle me-2"></i>${error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <c:if test="${not empty success}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="fas fa-check-circle me-2"></i>${success}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <c:if test="${not backendHealthy}">
            <div class="alert alert-warning alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-triangle me-2"></i>Problemas de conectividad con el servidor. Algunas funciones pueden no estar disponibles.
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <div class="row">
            <!-- Sidebar izquierdo - Perfil del usuario -->
            <div class="col-lg-3">
                <div class="profile-card">
                    <div class="card-body text-center">
                        <div class="profile-avatar mx-auto mb-3">
                            ${currentUser.firstName.charAt(0)}${currentUser.lastName.charAt(0)}
                        </div>
                        <h5 class="card-title mb-1">${currentUser.firstName} ${currentUser.lastName}</h5>
                        <p class="card-text text-muted small mb-3">
                            <c:choose>
                                <c:when test="${not empty currentUser.headline}">
                                    ${currentUser.headline}
                                </c:when>
                                <c:otherwise>
                                    Profesional en ProLink
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <a href="${pageContext.request.contextPath}/profile" class="btn btn-outline-primary btn-sm">
                            Ver Perfil Completo
                        </a>
                    </div>
                </div>

                <!-- Links rápidos -->
                <div class="profile-card">
                    <div class="card-body">
                        <h6 class="card-title mb-3">Accesos Rápidos</h6>
                        <ul class="list-unstyled">
                            <li class="mb-2">
                                <a href="${pageContext.request.contextPath}/messages" class="text-decoration-none">
                                    <i class="fas fa-comments me-2 text-primary"></i>Mensajes
                                </a>
                            </li>
                            <li class="mb-2">
                                <a href="${pageContext.request.contextPath}/connections" class="text-decoration-none">
                                    <i class="fas fa-users me-2 text-primary"></i>Mi Red
                                </a>
                            </li>
                            <li class="mb-2">
                                <a href="#" class="text-decoration-none">
                                    <i class="fas fa-bell me-2 text-primary"></i>Notificaciones
                                </a>
                            </li>
                            <li class="mb-2">
                                <a href="#" class="text-decoration-none">
                                    <i class="fas fa-bookmark me-2 text-primary"></i>Elementos guardados
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>

            <!-- Contenido central - Posts y crear post -->
            <div class="col-lg-6">
                <!-- Formulario para crear post -->
                <div class="post-card">
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/dashboard" method="post" id="createPostForm">
                            <input type="hidden" name="action" value="createPost">
                            <div class="d-flex mb-3">
                                <div class="small-avatar me-3">
                                    ${currentUser.firstName.charAt(0)}${currentUser.lastName.charAt(0)}
                                </div>
                                <div class="flex-grow-1">
                                    <textarea class="form-control create-post-textarea" 
                                              name="content" 
                                              rows="3" 
                                              placeholder="¿Qué estás pensando, ${currentUser.firstName}?"
                                              maxlength="1000"
                                              required></textarea>
                                </div>
                            </div>
                            <div class="d-flex justify-content-between align-items-center">
                                <div class="btn-group" role="group">
                                    <input type="radio" class="btn-check" name="postType" id="text" value="TEXT" checked>
                                    <label class="btn btn-outline-secondary btn-sm" for="text">
                                        <i class="fas fa-font me-1"></i>Texto
                                    </label>
                                    
                                    <input type="radio" class="btn-check" name="postType" id="image" value="IMAGE">
                                    <label class="btn btn-outline-secondary btn-sm" for="image">
                                        <i class="fas fa-image me-1"></i>Imagen
                                    </label>
                                    
                                    <input type="radio" class="btn-check" name="postType" id="article" value="ARTICLE">
                                    <label class="btn btn-outline-secondary btn-sm" for="article">
                                        <i class="fas fa-newspaper me-1"></i>Artículo
                                    </label>
                                </div>
                                <button type="submit" class="btn btn-linkedin">
                                    <i class="fas fa-paper-plane me-2"></i>Publicar
                                </button>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Lista de posts -->
                <c:choose>
                    <c:when test="${not empty posts}">
                        <c:forEach var="post" items="${posts}">
                            <div class="post-card">
                                <div class="card-body">
                                    <!-- Header del post -->
                                    <div class="d-flex mb-3">
                                        <div class="small-avatar me-3">
                                            <c:choose>
                                                <c:when test="${not empty post.author}">
                                                    ${post.author.firstName.charAt(0)}${post.author.lastName.charAt(0)}
                                                </c:when>
                                                <c:otherwise>
                                                    U
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div class="flex-grow-1">
                                            <h6 class="mb-0">
                                                <c:choose>
                                                    <c:when test="${not empty post.author}">
                                                        ${post.author.firstName} ${post.author.lastName}
                                                    </c:when>
                                                    <c:otherwise>
                                                        Usuario ProLink
                                                    </c:otherwise>
                                                </c:choose>
                                            </h6>
                                            <small class="text-muted">
                                                <c:choose>
                                                    <c:when test="${not empty post.author.headline}">
                                                        ${post.author.headline}
                                                    </c:when>
                                                    <c:otherwise>
                                                        Profesional
                                                    </c:otherwise>
                                                </c:choose>
                                            </small>
                                            <br>
                                            <small class="text-muted">
                                                <c:choose>
                                                    <c:when test="${not empty post.createdAt}">
                                                        <fmt:formatDate value="${post.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        Hace un momento
                                                    </c:otherwise>
                                                </c:choose>
                                                <i class="fas fa-globe-americas ms-1" title="Público"></i>
                                            </small>
                                        </div>
                                        <div class="dropdown">
                                            <button class="btn btn-sm" data-bs-toggle="dropdown">
                                                <i class="fas fa-ellipsis-h"></i>
                                            </button>
                                            <ul class="dropdown-menu">
                                                <li><a class="dropdown-item" href="#">Guardar post</a></li>
                                                <li><a class="dropdown-item" href="#">Reportar</a></li>
                                            </ul>
                                        </div>
                                    </div>

                                    <!-- Contenido del post -->
                                    <div class="mb-3">
                                        <p class="card-text">${post.formattedContent}</p>
                                        
                                        <!-- Mostrar imagen si existe -->
                                        <c:if test="${post.imagePost and not empty post.imageUrl}">
                                            <img src="${post.imageUrl}" class="img-fluid rounded" alt="Imagen del post">
                                        </c:if>
                                        
                                        <!-- Mostrar video si existe -->
                                        <c:if test="${post.videoPost and not empty post.videoUrl}">
                                            <video controls class="img-fluid rounded">
                                                <source src="${post.videoUrl}" type="video/mp4">
                                                Tu navegador no soporta videos.
                                            </video>
                                        </c:if>
                                    </div>

                                    <!-- Estadísticas del post -->
                                    <c:if test="${post.likesCount > 0 or post.commentsCount > 0}">
                                        <div class="d-flex justify-content-between align-items-center mb-2 pb-2 border-bottom">
                                            <small class="text-muted">
                                                <c:if test="${post.likesCount > 0}">
                                                    <i class="fas fa-thumbs-up text-primary me-1"></i>
                                                    ${post.likesCount} Me gusta
                                                </c:if>
                                            </small>
                                            <small class="text-muted">
                                                <c:if test="${post.commentsCount > 0}">
                                                    ${post.commentsCount} comentarios
                                                </c:if>
                                            </small>
                                        </div>
                                    </c:if>

                                    <!-- Acciones del post -->
                                    <div class="post-actions d-flex justify-content-around">
                                        <button type="button" class="like-btn flex-fill" data-post-id="${post.id}">
                                            <i class="fas fa-thumbs-up me-2 ${post.likedByCurrentUser ? 'text-primary' : ''}"></i>
                                            Me gusta
                                        </button>
                                        <button type="button" class="comment-btn flex-fill" data-post-id="${post.id}">
                                            <i class="fas fa-comment me-2"></i>Comentar
                                        </button>
                                        <button type="button" class="share-btn flex-fill" data-post-id="${post.id}">
                                            <i class="fas fa-share me-2"></i>Compartir
                                        </button>
                                        <button type="button" class="send-btn flex-fill" data-post-id="${post.id}">
                                            <i class="fas fa-paper-plane me-2"></i>Enviar
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="post-card">
                            <div class="card-body text-center py-5">
                                <i class="fas fa-stream fa-3x text-muted mb-3"></i>
                                <h5 class="text-muted">¡Bienvenido a ProLink!</h5>
                                <p class="text-muted">Aún no hay publicaciones en tu timeline.</p>
                                <p class="text-muted">Comienza conectándote con otros profesionales.</p>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Sidebar derecho - Sugerencias -->
            <div class="col-lg-3">
                <div class="suggestions-card">
                    <div class="card-header bg-white border-0">
                        <h6 class="mb-0">Personas que podrías conocer</h6>
                    </div>
                    <div class="card-body p-0">
                        <c:choose>
                            <c:when test="${not empty suggestions}">
                                <c:forEach var="suggestion" items="${suggestions}" varStatus="status">
                                    <div class="suggestion-item">
                                        <div class="d-flex align-items-center">
                                            <div class="small-avatar me-3">
                                                ${suggestion.firstName.charAt(0)}${suggestion.lastName.charAt(0)}
                                            </div>
                                            <div class="flex-grow-1">
                                                <h6 class="mb-0">${suggestion.firstName} ${suggestion.lastName}</h6>
                                                <small class="text-muted">
                                                    <c:choose>
                                                        <c:when test="${not empty suggestion.headline}">
                                                            ${suggestion.headline}
                                                        </c:when>
                                                        <c:otherwise>
                                                            Profesional en ProLink
                                                        </c:otherwise>
                                                    </c:choose>
                                                </small>
                                            </div>
                                        </div>
                                        <div class="mt-2">
                                            <button class="btn btn-outline-primary btn-sm connect-btn w-100" 
                                                    data-user-id="${suggestion.id}">
                                                <i class="fas fa-user-plus me-1"></i>Conectar
                                            </button>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="p-3 text-center">
                                    <i class="fas fa-users fa-2x text-muted mb-2"></i>
                                    <p class="text-muted mb-0">No hay sugerencias disponibles</p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <c:if test="${not empty suggestions}">
                        <div class="card-footer bg-white border-0">
                            <a href="${pageContext.request.contextPath}/connections" class="text-decoration-none">
                                Ver más sugerencias
                            </a>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Custom JavaScript -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Manejo de likes
            document.querySelectorAll('.like-btn').forEach(button => {
                button.addEventListener('click', function() {
                    const postId = this.dataset.postId;
                    
                    fetch('${pageContext.request.contextPath}/dashboard', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: 'action=likePost&postId=' + postId
                    })
                    .then(response => response.json())
                    .then(data => {
                        if (data.success) {
                            const icon = this.querySelector('i');
                            icon.classList.toggle('text-primary');
                            console.log('Like procesado exitosamente');
                        } else {
                            console.error('Error al procesar like:', data.message);
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                    });
                });
            });
            
            // Manejo de botones de conectar
            document.querySelectorAll('.connect-btn').forEach(button => {
                button.addEventListener('click', function() {
                    const userId = this.dataset.userId;
                    console.log('Conectar con usuario:', userId);
                    // TODO: Implementar conexión
                    this.innerHTML = '<i class="fas fa-check me-1"></i>Enviado';
                    this.classList.remove('btn-outline-primary');
                    this.classList.add('btn-secondary');
                    this.disabled = true;
                });
            });
            
            // Contador de caracteres para el textarea
            const textarea = document.querySelector('textarea[name="content"]');
            if (textarea) {
                const createCounter = () => {
                    const counter = document.createElement('small');
                    counter.className = 'text-muted';
                    counter.style.float = 'right';
                    textarea.parentNode.appendChild(counter);
                    return counter;
                };
                
                const counter = createCounter();
                
                textarea.addEventListener('input', function() {
                    const remaining = 1000 - this.value.length;
                    counter.textContent = remaining + ' caracteres restantes';
                    
                    if (remaining < 100) {
                        counter.classList.add('text-warning');
                    } else {
                        counter.classList.remove('text-warning');
                    }
                    
                    if (remaining < 0) {
                        counter.classList.remove('text-warning');
                        counter.classList.add('text-danger');
                    } else {
                        counter.classList.remove('text-danger');
                    }
                });
            }
            
            // Auto-resize textarea
            if (textarea) {
                textarea.addEventListener('input', function() {
                    this.style.height = 'auto';
                    this.style.height = this.scrollHeight + 'px';
                });
            }
        });
    </script>
</body>
</html>