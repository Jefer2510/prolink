<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ProLink - Perfil de ${profileUser.firstName} ${profileUser.lastName}</title>
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
        .profile-card, .section-card {
            background: white;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            margin-bottom: 1rem;
        }
        .profile-header {
            background: linear-gradient(135deg, #0a66c2, #004182);
            border-radius: 10px 10px 0 0;
            height: 200px;
            position: relative;
        }
        .profile-avatar {
            width: 120px;
            height: 120px;
            background: white;
            color: #0a66c2;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 3rem;
            font-weight: bold;
            border-radius: 50%;
            border: 4px solid white;
            position: absolute;
            bottom: -60px;
            left: 30px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.2);
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
        .navbar {
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .content-area {
            margin-top: 2rem;
        }
        .profile-info {
            margin-top: 80px;
            padding: 20px 30px;
        }
        .stat-item {
            text-align: center;
            padding: 1rem;
            border-right: 1px solid #eee;
        }
        .stat-item:last-child {
            border-right: none;
        }
        .stat-number {
            font-size: 1.5rem;
            font-weight: bold;
            color: #0a66c2;
        }
        .section-header {
            background-color: #f8f9fa;
            padding: 1rem 1.5rem;
            border-bottom: 1px solid #dee2e6;
            border-radius: 10px 10px 0 0;
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
                <div class="nav-item me-3">
                    <a class="nav-link" href="${pageContext.request.contextPath}/dashboard">
                        <i class="fas fa-home"></i> <span class="d-none d-md-inline">Inicio</span>
                    </a>
                </div>
                <div class="nav-item me-3">
                    <a class="nav-link" href="${pageContext.request.contextPath}/connections">
                        <i class="fas fa-users"></i> <span class="d-none d-md-inline">Mi Red</span>
                    </a>
                </div>
                <div class="nav-item me-3">
                    <a class="nav-link" href="${pageContext.request.contextPath}/messages">
                        <i class="fas fa-comments"></i> <span class="d-none d-md-inline">Mensajes</span>
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
            <!-- Perfil principal -->
            <div class="col-lg-8">
                <!-- Header del perfil -->
                <div class="profile-card">
                    <div class="profile-header">
                        <div class="profile-avatar">
                            ${profileUser.firstName.charAt(0)}${profileUser.lastName.charAt(0)}
                        </div>
                    </div>
                    <div class="profile-info">
                        <div class="row">
                            <div class="col-md-8">
                                <h2 class="mb-1">${profileUser.firstName} ${profileUser.lastName}</h2>
                                <p class="text-muted mb-2">
                                    <c:choose>
                                        <c:when test="${not empty profileUser.headline}">
                                            ${profileUser.headline}
                                        </c:when>
                                        <c:otherwise>
                                            Profesional en ProLink
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                                <p class="text-muted small mb-3">
                                    <i class="fas fa-map-marker-alt me-1"></i>
                                    <c:choose>
                                        <c:when test="${not empty profileUser.location}">
                                            ${profileUser.location}
                                        </c:when>
                                        <c:otherwise>
                                            Ubicación no especificada
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                            </div>
                            <div class="col-md-4 text-end">
                                <c:if test="${isOwnProfile}">
                                    <button class="btn btn-linkedin mb-2" data-bs-toggle="modal" data-bs-target="#editProfileModal">
                                        <i class="fas fa-edit me-2"></i>Editar Perfil
                                    </button>
                                    <br>
                                    <button class="btn btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#changePasswordModal">
                                        <i class="fas fa-key me-2"></i>Cambiar Contraseña
                                    </button>
                                </c:if>
                                <c:if test="${not isOwnProfile}">
                                    <button class="btn btn-linkedin mb-2">
                                        <i class="fas fa-user-plus me-2"></i>Conectar
                                    </button>
                                    <br>
                                    <button class="btn btn-outline-secondary">
                                        <i class="fas fa-envelope me-2"></i>Mensaje
                                    </button>
                                </c:if>
                            </div>
                        </div>
                        
                        <!-- Estadísticas -->
                        <div class="row mt-4 border-top pt-3">
                            <div class="col-4 stat-item">
                                <div class="stat-number">${postCount}</div>
                                <div class="text-muted small">Publicaciones</div>
                            </div>
                            <div class="col-4 stat-item">
                                <div class="stat-number">${connectionCount}</div>
                                <div class="text-muted small">Conexiones</div>
                            </div>
                            <div class="col-4 stat-item">
                                <div class="stat-number">0</div>
                                <div class="text-muted small">Seguidores</div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Acerca de -->
                <div class="section-card">
                    <div class="section-header">
                        <h5 class="mb-0">Acerca de</h5>
                    </div>
                    <div class="card-body">
                        <p class="mb-0">
                            <c:choose>
                                <c:when test="${not empty profileUser.summary}">
                                    ${profileUser.summary}
                                </c:when>
                                <c:otherwise>
                                    <c:if test="${isOwnProfile}">
                                        <em class="text-muted">Agrega una descripción sobre ti para que otros puedan conocerte mejor.</em>
                                    </c:if>
                                    <c:if test="${not isOwnProfile}">
                                        <em class="text-muted">${profileUser.firstName} aún no ha agregado una descripción.</em>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                </div>

                <!-- Actividad reciente -->
                <div class="section-card">
                    <div class="section-header">
                        <h5 class="mb-0">Actividad reciente</h5>
                    </div>
                    <div class="card-body">
                        <div class="text-center py-4">
                            <i class="fas fa-stream fa-3x text-muted mb-3"></i>
                            <p class="text-muted">No hay actividad reciente para mostrar</p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Sidebar -->
            <div class="col-lg-4">
                <!-- Información de contacto -->
                <div class="section-card">
                    <div class="section-header">
                        <h6 class="mb-0">Información de contacto</h6>
                    </div>
                    <div class="card-body">
                        <div class="mb-3">
                            <small class="text-muted">Email</small>
                            <div>${profileUser.email}</div>
                        </div>
                        <c:if test="${isOwnProfile}">
                            <div class="mb-3">
                                <small class="text-muted">Perfil creado</small>
                                <div>
                                    <c:choose>
                                        <c:when test="${not empty profileUser.createdAt}">
                                            <fmt:formatDate value="${profileUser.createdAt}" pattern="MMMM yyyy"/>
                                        </c:when>
                                        <c:otherwise>
                                            Recientemente
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </c:if>
                    </div>
                </div>

                <!-- Sugerencias de personas -->
                <div class="section-card">
                    <div class="section-header">
                        <h6 class="mb-0">Personas que podrías conocer</h6>
                    </div>
                    <div class="card-body">
                        <div class="text-center py-3">
                            <i class="fas fa-users fa-2x text-muted mb-2"></i>
                            <p class="text-muted mb-0 small">Conecta con más profesionales</p>
                            <a href="${pageContext.request.contextPath}/connections" class="btn btn-outline-primary btn-sm mt-2">
                                Ver sugerencias
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal para editar perfil -->
    <c:if test="${isOwnProfile}">
        <div class="modal fade" id="editProfileModal" tabindex="-1" aria-labelledby="editProfileModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <form action="${pageContext.request.contextPath}/profile" method="post">
                        <input type="hidden" name="action" value="updateProfile">
                        <div class="modal-header">
                            <h5 class="modal-title" id="editProfileModalLabel">Editar Perfil</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="firstName" class="form-label">Nombre *</label>
                                        <input type="text" class="form-control" id="firstName" name="firstName" 
                                               value="${profileUser.firstName}" required>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="lastName" class="form-label">Apellido *</label>
                                        <input type="text" class="form-control" id="lastName" name="lastName" 
                                               value="${profileUser.lastName}" required>
                                    </div>
                                </div>
                            </div>
                            <div class="mb-3">
                                <label for="email" class="form-label">Email *</label>
                                <input type="email" class="form-control" id="email" name="email" 
                                       value="${profileUser.email}" required>
                            </div>
                            <div class="mb-3">
                                <label for="headline" class="form-label">Título profesional</label>
                                <input type="text" class="form-control" id="headline" name="headline" 
                                       value="${profileUser.headline}" 
                                       placeholder="ej. Desarrollador Full Stack en TechCorp">
                            </div>
                            <div class="mb-3">
                                <label for="location" class="form-label">Ubicación</label>
                                <input type="text" class="form-control" id="location" name="location" 
                                       placeholder="ej. Madrid, España">
                            </div>
                            <div class="mb-3">
                                <label for="summary" class="form-label">Acerca de</label>
                                <textarea class="form-control" id="summary" name="summary" rows="4" 
                                          placeholder="Cuéntanos sobre ti, tu experiencia y tus intereses profesionales..."></textarea>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                            <button type="submit" class="btn btn-linkedin">Guardar Cambios</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Modal para cambiar contraseña -->
        <div class="modal fade" id="changePasswordModal" tabindex="-1" aria-labelledby="changePasswordModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="${pageContext.request.contextPath}/profile" method="post">
                        <input type="hidden" name="action" value="changePassword">
                        <div class="modal-header">
                            <h5 class="modal-title" id="changePasswordModalLabel">Cambiar Contraseña</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <div class="mb-3">
                                <label for="currentPassword" class="form-label">Contraseña actual *</label>
                                <input type="password" class="form-control" id="currentPassword" name="currentPassword" required>
                            </div>
                            <div class="mb-3">
                                <label for="newPassword" class="form-label">Nueva contraseña *</label>
                                <input type="password" class="form-control" id="newPassword" name="newPassword" 
                                       minlength="6" required>
                                <div class="form-text">Mínimo 6 caracteres</div>
                            </div>
                            <div class="mb-3">
                                <label for="confirmPassword" class="form-label">Confirmar nueva contraseña *</label>
                                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" 
                                       minlength="6" required>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                            <button type="submit" class="btn btn-linkedin">Cambiar Contraseña</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </c:if>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Custom JavaScript -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Validación de confirmación de contraseña
            const newPassword = document.getElementById('newPassword');
            const confirmPassword = document.getElementById('confirmPassword');
            
            if (newPassword && confirmPassword) {
                function validatePassword() {
                    if (confirmPassword.value !== newPassword.value) {
                        confirmPassword.setCustomValidity('Las contraseñas no coinciden');
                    } else {
                        confirmPassword.setCustomValidity('');
                    }
                }
                
                newPassword.addEventListener('change', validatePassword);
                confirmPassword.addEventListener('keyup', validatePassword);
            }
        });
    </script>
</body>
</html>