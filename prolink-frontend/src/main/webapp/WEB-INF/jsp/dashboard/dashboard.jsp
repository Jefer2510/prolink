<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ProLink - Inicio</title>
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
        .card {
            border: none;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            margin-bottom: 1rem;
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
        .medium-avatar {
            width: 50px;
            height: 50px;
            background: linear-gradient(135deg, #0a66c2, #004182);
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.2rem;
            font-weight: bold;
            border-radius: 50%;
        }
        .large-avatar {
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
        .btn-linkedin {
            background: linear-gradient(135deg, #0a66c2, #004182);
            border: none;
            color: white;
        }
        .btn-linkedin:hover {
            background: linear-gradient(135deg, #004182, #0a66c2);
            color: white;
        }
        .btn-outline-linkedin {
            border: 2px solid #0a66c2;
            color: #0a66c2;
            background: transparent;
        }
        .btn-outline-linkedin:hover {
            background: #0a66c2;
            color: white;
        }
        .navbar {
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .profile-card {
            text-align: center;
            position: relative;
            background: linear-gradient(135deg, #0a66c2, #004182);
            color: white;
            border-radius: 10px 10px 0 0;
        }
        .profile-card-body {
            padding: 1rem;
            background: white;
            color: #333;
            border-radius: 0 0 10px 10px;
        }
        .stats-item {
            text-align: center;
            padding: 0.5rem;
            border-right: 1px solid #e9ecef;
        }
        .stats-item:last-child {
            border-right: none;
        }
        .stats-number {
            font-size: 1.5rem;
            font-weight: bold;
            color: #0a66c2;
        }
        .stats-label {
            font-size: 0.8rem;
            color: #666;
        }
        .post-card {
            transition: all 0.2s;
        }
        .post-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.15);
        }
        .post-actions {
            border-top: 1px solid #e9ecef;
            padding: 0.75rem 0;
        }
        .post-action-btn {
            border: none;
            background: transparent;
            color: #666;
            padding: 0.5rem 1rem;
            border-radius: 5px;
            transition: all 0.2s;
            flex-grow: 1;
        }
        .post-action-btn:hover {
            background: #f8f9fa;
            color: #0a66c2;
        }
        .post-action-btn.active {
            color: #0a66c2;
        }
        .content-area {
            margin-top: 2rem;
        }
        .suggestion-card {
            transition: all 0.2s;
        }
        .suggestion-card:hover {
            transform: translateY(-2px);
        }
        .create-post-box {
            background: white;
            border-radius: 10px;
            padding: 1rem;
            border: 1px solid #ddd;
            cursor: pointer;
            transition: all 0.2s;
        }
        .create-post-box:hover {
            border-color: #0a66c2;
        }
        .sidebar-sticky {
            position: sticky;
            top: 100px;
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
            
            <!-- Search Bar -->
            <div class="d-none d-md-flex flex-grow-1 mx-4">
                <form class="d-flex w-100" action="${pageContext.request.contextPath}/search" method="get">
                    <div class="input-group">
                        <span class="input-group-text bg-white border-end-0">
                            <i class="fas fa-search text-muted"></i>
                        </span>
                        <input class="form-control border-start-0" type="search" name="q" 
                               placeholder="Buscar profesionales, empresas..." style="max-width: 400px;">
                    </div>
                </form>
            </div>
            
            <div class="navbar-nav ms-auto d-flex flex-row">
                <div class="nav-item me-3">
                    <a class="nav-link active" href="${pageContext.request.contextPath}/dashboard">
                        <i class="fas fa-home"></i> <span class="d-none d-md-inline">Inicio</span>
                    </a>
                </div>
                <div class="nav-item me-3">
                    <a class="nav-link" href="${pageContext.request.contextPath}/connections">
                        <i class="fas fa-users"></i> <span class="d-none d-md-inline">Mi Red</span>
                    </a>
                </div>
                <div class="nav-item me-3">
                    <a class="nav-link position-relative" href="${pageContext.request.contextPath}/messages">
                        <i class="fas fa-comments"></i> <span class="d-none d-md-inline">Mensajes</span>
                        <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger" style="font-size: 0.6rem;">
                            3
                        </span>
                    </a>
                </div>
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

        <div class="row">
            <!-- Left Sidebar - Profile Card -->
            <div class="col-lg-3">
                <div class="sidebar-sticky">
                    <!-- Profile Summary -->
                    <div class="card">
                        <div class="profile-card">
                            <div class="p-4">
                                <div class="large-avatar mx-auto mb-3">
                                    ${currentUser.firstName.charAt(0)}${currentUser.lastName.charAt(0)}
                                </div>
                                <h6 class="mb-1">${currentUser.firstName} ${currentUser.lastName}</h6>
                                <small>
                                    <c:choose>
                                        <c:when test="${not empty currentUser.headline}">
                                            ${currentUser.headline}
                                        </c:when>
                                        <c:otherwise>
                                            Profesional en ProLink
                                        </c:otherwise>
                                    </c:choose>
                                </small>
                            </div>
                        </div>
                        <div class="profile-card-body">
                            <div class="row text-center">
                                <div class="col-4 stats-item">
                                    <div class="stats-number">${userStats.connectionsCount}</div>
                                    <div class="stats-label">Conexiones</div>
                                </div>
                                <div class="col-4 stats-item">
                                    <div class="stats-number">${userStats.profileViews}</div>
                                    <div class="stats-label">Vistas</div>
                                </div>
                                <div class="col-4 stats-item">
                                    <div class="stats-number">${userStats.postImpressions}</div>
                                    <div class="stats-label">Impresiones</div>
                                </div>
                            </div>
                            <div class="mt-3">
                                <a href="${pageContext.request.contextPath}/profile" class="btn btn-outline-linkedin btn-sm w-100">
                                    <i class="fas fa-user me-2"></i>Ver mi perfil
                                </a>
                            </div>
                        </div>
                    </div>

                    <!-- Quick Actions -->
                    <div class="card">
                        <div class="card-body">
                            <h6 class="card-title">Acciones rápidas</h6>
                            <div class="d-grid gap-2">
                                <a href="${pageContext.request.contextPath}/connections" class="btn btn-outline-secondary btn-sm">
                                    <i class="fas fa-user-plus me-2"></i>Buscar personas
                                </a>
                                <a href="${pageContext.request.contextPath}/messages" class="btn btn-outline-secondary btn-sm">
                                    <i class="fas fa-comments me-2"></i>Enviar mensaje
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Main Content - Feed -->
            <div class="col-lg-6">
                <!-- Create Post Box -->
                <div class="card mb-3">
                    <div class="card-body">
                        <div class="create-post-box" data-bs-toggle="modal" data-bs-target="#createPostModal">
                            <div class="d-flex align-items-center">
                                <div class="medium-avatar me-3">
                                    ${currentUser.firstName.charAt(0)}${currentUser.lastName.charAt(0)}
                                </div>
                                <div class="flex-grow-1">
                                    <div class="form-control border-0 bg-light" style="cursor: pointer;">
                                        ¿En qué estás pensando, ${currentUser.firstName}?
                                    </div>
                                </div>
                            </div>
                            <hr>
                            <div class="d-flex justify-content-around">
                                <button type="button" class="btn btn-light flex-fill me-2">
                                    <i class="fas fa-image text-primary me-2"></i>Foto
                                </button>
                                <button type="button" class="btn btn-light flex-fill me-2">
                                    <i class="fas fa-video text-success me-2"></i>Video
                                </button>
                                <button type="button" class="btn btn-light flex-fill">
                                    <i class="fas fa-calendar text-warning me-2"></i>Evento
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Feed Posts -->
                <div id="feedContainer">
                    <c:choose>
                        <c:when test="${not empty posts}">
                            <c:forEach var="post" items="${posts}">
                                <div class="card post-card mb-3">
                                    <div class="card-body">
                                        <!-- Post Header -->
                                        <div class="d-flex align-items-start mb-3">
                                            <div class="medium-avatar me-3">
                                                ${post.author.firstName.charAt(0)}${post.author.lastName.charAt(0)}
                                            </div>
                                            <div class="flex-grow-1">
                                                <h6 class="mb-0">${post.author.firstName} ${post.author.lastName}</h6>
                                                <small class="text-muted">
                                                    <c:choose>
                                                        <c:when test="${not empty post.author.headline}">
                                                            ${post.author.headline}
                                                        </c:when>
                                                        <c:otherwise>
                                                            Profesional en ProLink
                                                        </c:otherwise>
                                                    </c:choose>
                                                </small>
                                                <br>
                                                <small class="text-muted">
                                                    <i class="fas fa-globe-americas me-1"></i>
                                                    ${post.formattedCreatedAt}
                                                </small>
                                            </div>
                                            <div class="dropdown">
                                                <button class="btn btn-sm btn-outline-secondary dropdown-toggle" 
                                                        data-bs-toggle="dropdown">
                                                    <i class="fas fa-ellipsis-h"></i>
                                                </button>
                                                <ul class="dropdown-menu">
                                                    <li><a class="dropdown-item" href="#">
                                                        <i class="fas fa-bookmark me-2"></i>Guardar
                                                    </a></li>
                                                    <c:if test="${post.canEdit(currentUser)}">
                                                        <li><a class="dropdown-item" href="#">
                                                            <i class="fas fa-edit me-2"></i>Editar
                                                        </a></li>
                                                        <li>
                                                            <form action="${pageContext.request.contextPath}/dashboard" method="post" class="m-0">
                                                                <input type="hidden" name="action" value="deletePost">
                                                                <input type="hidden" name="postId" value="${post.id}">
                                                                <button type="submit" class="dropdown-item text-danger"
                                                                        onclick="return confirm('¿Estás seguro de eliminar este post?')">
                                                                    <i class="fas fa-trash me-2"></i>Eliminar
                                                                </button>
                                                            </form>
                                                        </li>
                                                    </c:if>
                                                </ul>
                                            </div>
                                        </div>

                                        <!-- Post Content -->
                                        <div class="mb-3">
                                            <p class="mb-0">${post.content}</p>
                                        </div>

                                        <!-- Post Actions -->
                                        <div class="post-actions">
                                            <div class="d-flex justify-content-between align-items-center mb-2">
                                                <div class="text-muted small">
                                                    <c:if test="${post.likesCount > 0}">
                                                        <i class="fas fa-thumbs-up text-primary me-1"></i>
                                                        ${post.likesCount} Me gusta
                                                    </c:if>
                                                </div>
                                                <div class="text-muted small">
                                                    <c:if test="${post.commentsCount > 0}">
                                                        ${post.commentsCount} comentarios
                                                    </c:if>
                                                </div>
                                            </div>
                                            
                                            <div class="d-flex">
                                                <button class="post-action-btn ${post.liked ? 'active' : ''}" 
                                                        onclick="likePost(${post.id})">
                                                    <i class="fas fa-thumbs-up me-2"></i>Me gusta
                                                </button>
                                                <button class="post-action-btn" 
                                                        onclick="toggleCommentBox(${post.id})">
                                                    <i class="fas fa-comment me-2"></i>Comentar
                                                </button>
                                                <button class="post-action-btn">
                                                    <i class="fas fa-share me-2"></i>Compartir
                                                </button>
                                            </div>

                                            <!-- Comment Box -->
                                            <div id="commentBox${post.id}" class="mt-3" style="display: none;">
                                                <form action="${pageContext.request.contextPath}/dashboard" method="post">
                                                    <input type="hidden" name="action" value="commentPost">
                                                    <input type="hidden" name="postId" value="${post.id}">
                                                    <div class="d-flex">
                                                        <div class="small-avatar me-2">
                                                            ${currentUser.firstName.charAt(0)}${currentUser.lastName.charAt(0)}
                                                        </div>
                                                        <div class="flex-grow-1">
                                                            <textarea class="form-control" name="comment" rows="2" 
                                                                      placeholder="Escribe un comentario..." required></textarea>
                                                            <div class="text-end mt-2">
                                                                <button type="button" class="btn btn-sm btn-secondary me-2"
                                                                        onclick="toggleCommentBox(${post.id})">Cancelar</button>
                                                                <button type="submit" class="btn btn-sm btn-linkedin">Comentar</button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="card">
                                <div class="card-body text-center py-5">
                                    <i class="fas fa-newspaper fa-3x text-muted mb-3"></i>
                                    <h5 class="text-muted">¡Comienza a conectar!</h5>
                                    <p class="text-muted">No hay posts en tu feed aún. Conecta con más profesionales para ver su contenido.</p>
                                    <a href="${pageContext.request.contextPath}/connections" class="btn btn-linkedin">
                                        <i class="fas fa-user-plus me-2"></i>Buscar personas
                                    </a>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Load More Button -->
                <div class="text-center mt-4">
                    <button class="btn btn-outline-secondary" onclick="loadMorePosts()">
                        <i class="fas fa-plus me-2"></i>Cargar más posts
                    </button>
                </div>
            </div>

            <!-- Right Sidebar - Suggestions -->
            <div class="col-lg-3">
                <div class="sidebar-sticky">
                    <!-- Connection Suggestions -->
                    <div class="card">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <h6 class="card-title mb-0">Personas que podrías conocer</h6>
                                <a href="${pageContext.request.contextPath}/connections" class="small text-decoration-none">Ver todas</a>
                            </div>
                            
                            <c:choose>
                                <c:when test="${not empty suggestions}">
                                    <c:forEach var="suggestion" items="${suggestions}" varStatus="status">
                                        <c:if test="${status.index < 3}">
                                            <div class="suggestion-card border rounded p-2 mb-2">
                                                <div class="d-flex align-items-center">
                                                    <div class="small-avatar me-2">
                                                        ${suggestion.firstName.charAt(0)}${suggestion.lastName.charAt(0)}
                                                    </div>
                                                    <div class="flex-grow-1">
                                                        <h6 class="mb-0 small">${suggestion.firstName} ${suggestion.lastName}</h6>
                                                        <small class="text-muted">${suggestion.headline}</small>
                                                    </div>
                                                </div>
                                                <div class="mt-2">
                                                    <form action="${pageContext.request.contextPath}/connections" method="post" class="d-inline">
                                                        <input type="hidden" name="action" value="sendRequest">
                                                        <input type="hidden" name="receiverId" value="${suggestion.id}">
                                                        <button type="submit" class="btn btn-outline-linkedin btn-sm w-100">
                                                            <i class="fas fa-user-plus me-1"></i>Conectar
                                                        </button>
                                                    </form>
                                                </div>
                                            </div>
                                        </c:if>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <div class="text-center py-3">
                                        <i class="fas fa-users fa-2x text-muted mb-2"></i>
                                        <p class="text-muted small">No hay sugerencias disponibles</p>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <!-- LinkedIn Premium Ad -->
                    <div class="card">
                        <div class="card-body">
                            <div class="d-flex align-items-center mb-2">
                                <i class="fas fa-crown text-warning me-2"></i>
                                <h6 class="mb-0">ProLink Premium</h6>
                            </div>
                            <p class="small text-muted mb-3">Descubre quién ve tu perfil y accede a herramientas exclusivas</p>
                            <button class="btn btn-warning btn-sm w-100">
                                Prueba gratis
                            </button>
                        </div>
                    </div>

                    <!-- Recent Activity -->
                    <div class="card">
                        <div class="card-body">
                            <h6 class="card-title">Actividad reciente</h6>
                            <div class="small text-muted">
                                <div class="mb-2">
                                    <i class="fas fa-eye text-primary me-2"></i>
                                    Tu perfil ha sido visto 12 veces esta semana
                                </div>
                                <div class="mb-2">
                                    <i class="fas fa-user-plus text-success me-2"></i>
                                    3 nuevas conexiones este mes
                                </div>
                                <div>
                                    <i class="fas fa-chart-line text-info me-2"></i>
                                    Tu último post tuvo 25 impresiones
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Create Post Modal -->
    <div class="modal fade" id="createPostModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <form action="${pageContext.request.contextPath}/dashboard" method="post">
                    <input type="hidden" name="action" value="createPost">
                    <div class="modal-header">
                        <h5 class="modal-title">Crear post</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <div class="d-flex align-items-center mb-3">
                            <div class="medium-avatar me-3">
                                ${currentUser.firstName.charAt(0)}${currentUser.lastName.charAt(0)}
                            </div>
                            <div>
                                <h6 class="mb-0">${currentUser.firstName} ${currentUser.lastName}</h6>
                                <small class="text-muted">
                                    <i class="fas fa-globe-americas me-1"></i>Cualquier persona
                                </small>
                            </div>
                        </div>
                        <textarea class="form-control border-0" name="content" rows="5" 
                                  placeholder="¿En qué estás pensando?" required 
                                  style="resize: none; font-size: 1.1rem;" maxlength="3000"></textarea>
                        <div class="text-end mt-2">
                            <small class="text-muted"><span id="charCount">0</span>/3000</small>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-linkedin" id="publishBtn" disabled>
                            <i class="fas fa-paper-plane me-2"></i>Publicar
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Custom JavaScript -->
    <script>
        // Character counter for post
        document.addEventListener('DOMContentLoaded', function() {
            const textarea = document.querySelector('#createPostModal textarea[name="content"]');
            const charCount = document.getElementById('charCount');
            const publishBtn = document.getElementById('publishBtn');
            
            if (textarea) {
                textarea.addEventListener('input', function() {
                    const length = this.value.length;
                    charCount.textContent = length;
                    publishBtn.disabled = length === 0 || length > 3000;
                });
            }
        });
        
        // Like post functionality
        function likePost(postId) {
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
                    // Update UI - toggle button state
                    const button = event.target.closest('button');
                    button.classList.toggle('active');
                    
                    // Reload page to update counts
                    setTimeout(() => location.reload(), 500);
                } else {
                    alert('Error procesando like: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error procesando like');
            });
        }
        
        // Toggle comment box
        function toggleCommentBox(postId) {
            const commentBox = document.getElementById('commentBox' + postId);
            if (commentBox) {
                if (commentBox.style.display === 'none') {
                    commentBox.style.display = 'block';
                    commentBox.querySelector('textarea').focus();
                } else {
                    commentBox.style.display = 'none';
                    commentBox.querySelector('textarea').value = '';
                }
            }
        }
        
        // Load more posts (placeholder)
        function loadMorePosts() {
            // TODO: Implement pagination
            alert('Función de carga de más posts en desarrollo');
        }
        
        // Auto-hide alerts after 5 seconds
        document.addEventListener('DOMContentLoaded', function() {
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(alert => {
                setTimeout(() => {
                    const bsAlert = new bootstrap.Alert(alert);
                    bsAlert.close();
                }, 5000);
            });
        });
    </script>
</body>
</html>