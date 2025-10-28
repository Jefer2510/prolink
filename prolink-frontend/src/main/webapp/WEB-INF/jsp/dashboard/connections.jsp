<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ProLink - Mi Red</title>
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
        .content-card, .user-card {
            background: white;
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
        .navbar {
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .content-area {
            margin-top: 2rem;
        }
        .nav-pills .nav-link {
            border-radius: 20px;
            margin-right: 0.5rem;
        }
        .nav-pills .nav-link.active {
            background: linear-gradient(135deg, #0a66c2, #004182);
        }
        .user-item {
            border-bottom: 1px solid #eee;
            padding: 1.5rem;
        }
        .user-item:last-child {
            border-bottom: none;
        }
        .stat-badge {
            background: #f1f3f4;
            color: #5f6368;
            padding: 0.25rem 0.75rem;
            border-radius: 15px;
            font-size: 0.875rem;
        }
        .search-box {
            background: white;
            border-radius: 25px;
            border: 1px solid #ddd;
            padding: 0.5rem 1rem;
        }
        .search-box:focus {
            border-color: #0a66c2;
            box-shadow: 0 0 0 0.2rem rgba(10, 102, 194, 0.25);
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
                    <a class="nav-link active" href="${pageContext.request.contextPath}/connections">
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
            <!-- Sidebar -->
            <div class="col-lg-3">
                <!-- Estadísticas de red -->
                <div class="content-card">
                    <div class="card-body text-center">
                        <h5 class="card-title">Tu Red</h5>
                        <div class="mb-3">
                            <div class="h3 text-primary">${connections.size()}</div>
                            <small class="text-muted">Conexiones</small>
                        </div>
                        <div class="mb-3">
                            <div class="h5 text-secondary">${pendingRequests.size()}</div>
                            <small class="text-muted">Solicitudes pendientes</small>
                        </div>
                        <a href="${pageContext.request.contextPath}/profile" class="btn btn-outline-primary btn-sm w-100">
                            Ver mi perfil
                        </a>
                    </div>
                </div>

                <!-- Enlaces rápidos -->
                <div class="content-card">
                    <div class="card-body">
                        <h6 class="card-title">Enlaces rápidos</h6>
                        <ul class="list-unstyled">
                            <li class="mb-2">
                                <a href="${pageContext.request.contextPath}/connections" class="text-decoration-none">
                                    <i class="fas fa-users me-2 text-primary"></i>Todas las conexiones
                                </a>
                            </li>
                            <li class="mb-2">
                                <a href="${pageContext.request.contextPath}/connections/suggestions" class="text-decoration-none">
                                    <i class="fas fa-user-plus me-2 text-primary"></i>Sugerencias
                                </a>
                            </li>
                            <li class="mb-2">
                                <a href="${pageContext.request.contextPath}/connections/pending" class="text-decoration-none">
                                    <i class="fas fa-clock me-2 text-primary"></i>Solicitudes pendientes
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>

            <!-- Contenido principal -->
            <div class="col-lg-9">
                <!-- Cabecera con pestañas -->
                <div class="content-card">
                    <div class="card-body">
                        <!-- Barra de búsqueda -->
                        <form action="${pageContext.request.contextPath}/connections" method="post" class="mb-4">
                            <input type="hidden" name="action" value="searchUsers">
                            <div class="input-group">
                                <input type="text" class="form-control search-box" name="searchTerm" 
                                       placeholder="Buscar personas por nombre o email..." 
                                       value="${searchTerm}">
                                <button type="submit" class="btn btn-linkedin">
                                    <i class="fas fa-search"></i>
                                </button>
                            </div>
                        </form>

                        <!-- Pestañas de navegación -->
                        <ul class="nav nav-pills mb-4">
                            <li class="nav-item">
                                <a class="nav-link ${activeTab eq 'connections' || empty activeTab ? 'active' : ''}" 
                                   href="${pageContext.request.contextPath}/connections">
                                    Conexiones (${connections.size()})
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link ${activeTab eq 'suggestions' ? 'active' : ''}" 
                                   href="${pageContext.request.contextPath}/connections/suggestions">
                                    Sugerencias (${suggestions.size()})
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link ${activeTab eq 'pending' ? 'active' : ''}" 
                                   href="${pageContext.request.contextPath}/connections/pending">
                                    Pendientes 
                                    <c:if test="${pendingRequests.size() > 0}">
                                        <span class="badge bg-danger">${pendingRequests.size()}</span>
                                    </c:if>
                                </a>
                            </li>
                        </ul>

                        <!-- Contenido según la pestaña activa -->
                        <c:choose>
                            <!-- Resultados de búsqueda -->
                            <c:when test="${activeTab eq 'search' && not empty searchResults}">
                                <h5 class="mb-3">Resultados de búsqueda para "${searchTerm}"</h5>
                                <c:forEach var="user" items="${searchResults}">
                                    <div class="user-item d-flex align-items-center">
                                        <div class="large-avatar me-3">
                                            ${user.firstName.charAt(0)}${user.lastName.charAt(0)}
                                        </div>
                                        <div class="flex-grow-1">
                                            <h6 class="mb-1">${user.firstName} ${user.lastName}</h6>
                                            <p class="mb-1 text-muted">
                                                <c:choose>
                                                    <c:when test="${not empty user.headline}">
                                                        ${user.headline}
                                                    </c:when>
                                                    <c:otherwise>
                                                        Profesional en ProLink
                                                    </c:otherwise>
                                                </c:choose>
                                            </p>
                                            <small class="text-muted">${user.email}</small>
                                        </div>
                                        <div>
                                            <form action="${pageContext.request.contextPath}/connections" method="post" style="display: inline;">
                                                <input type="hidden" name="action" value="sendRequest">
                                                <input type="hidden" name="userId" value="${user.id}">
                                                <button type="submit" class="btn btn-outline-primary btn-sm">
                                                    <i class="fas fa-user-plus me-1"></i>Conectar
                                                </button>
                                            </form>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>

                            <!-- Resultados de búsqueda vacíos -->
                            <c:when test="${activeTab eq 'search' && empty searchResults && not empty searchTerm}">
                                <div class="text-center py-5">
                                    <i class="fas fa-search fa-3x text-muted mb-3"></i>
                                    <h5 class="text-muted">No se encontraron resultados</h5>
                                    <p class="text-muted">Intenta con otros términos de búsqueda</p>
                                </div>
                            </c:when>

                            <!-- Pestaña de conexiones -->
                            <c:when test="${activeTab eq 'connections' || empty activeTab}">
                                <c:choose>
                                    <c:when test="${not empty connections}">
                                        <c:forEach var="connection" items="${connections}">
                                            <div class="user-item d-flex align-items-center">
                                                <div class="large-avatar me-3">
                                                    ${connection.firstName.charAt(0)}${connection.lastName.charAt(0)}
                                                </div>
                                                <div class="flex-grow-1">
                                                    <h6 class="mb-1">
                                                        <a href="${pageContext.request.contextPath}/profile/${connection.id}" class="text-decoration-none">
                                                            ${connection.firstName} ${connection.lastName}
                                                        </a>
                                                    </h6>
                                                    <p class="mb-1 text-muted">
                                                        <c:choose>
                                                            <c:when test="${not empty connection.headline}">
                                                                ${connection.headline}
                                                            </c:when>
                                                            <c:otherwise>
                                                                Profesional en ProLink
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </p>
                                                    <span class="stat-badge">Conectado</span>
                                                </div>
                                                <div class="dropdown">
                                                    <button class="btn btn-outline-secondary btn-sm dropdown-toggle" 
                                                            data-bs-toggle="dropdown">
                                                        <i class="fas fa-ellipsis-h"></i>
                                                    </button>
                                                    <ul class="dropdown-menu">
                                                        <li>
                                                            <a class="dropdown-item" 
                                                               href="${pageContext.request.contextPath}/messages?user=${connection.id}">
                                                                <i class="fas fa-envelope me-2"></i>Enviar mensaje
                                                            </a>
                                                        </li>
                                                        <li><hr class="dropdown-divider"></li>
                                                        <li>
                                                            <form action="${pageContext.request.contextPath}/connections" method="post">
                                                                <input type="hidden" name="action" value="removeConnection">
                                                                <input type="hidden" name="userId" value="${connection.id}">
                                                                <button type="submit" class="dropdown-item text-danger"
                                                                        onclick="return confirm('¿Estás seguro de que quieres eliminar esta conexión?')">
                                                                    <i class="fas fa-trash me-2"></i>Eliminar conexión
                                                                </button>
                                                            </form>
                                                        </li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="text-center py-5">
                                            <i class="fas fa-users fa-3x text-muted mb-3"></i>
                                            <h5 class="text-muted">Aún no tienes conexiones</h5>
                                            <p class="text-muted">Busca y conecta con otros profesionales para expandir tu red</p>
                                            <a href="${pageContext.request.contextPath}/connections/suggestions" class="btn btn-linkedin">
                                                Ver sugerencias
                                            </a>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>

                            <!-- Pestaña de sugerencias -->
                            <c:when test="${activeTab eq 'suggestions'}">
                                <c:choose>
                                    <c:when test="${not empty suggestions}">
                                        <c:forEach var="suggestion" items="${suggestions}">
                                            <div class="user-item d-flex align-items-center">
                                                <div class="large-avatar me-3">
                                                    ${suggestion.firstName.charAt(0)}${suggestion.lastName.charAt(0)}
                                                </div>
                                                <div class="flex-grow-1">
                                                    <h6 class="mb-1">
                                                        <a href="${pageContext.request.contextPath}/profile/${suggestion.id}" class="text-decoration-none">
                                                            ${suggestion.firstName} ${suggestion.lastName}
                                                        </a>
                                                    </h6>
                                                    <p class="mb-1 text-muted">
                                                        <c:choose>
                                                            <c:when test="${not empty suggestion.headline}">
                                                                ${suggestion.headline}
                                                            </c:when>
                                                            <c:otherwise>
                                                                Profesional en ProLink
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </p>
                                                    <small class="text-muted">Sugerido para ti</small>
                                                </div>
                                                <div>
                                                    <form action="${pageContext.request.contextPath}/connections" method="post" style="display: inline;">
                                                        <input type="hidden" name="action" value="sendRequest">
                                                        <input type="hidden" name="userId" value="${suggestion.id}">
                                                        <button type="submit" class="btn btn-linkedin btn-sm">
                                                            <i class="fas fa-user-plus me-1"></i>Conectar
                                                        </button>
                                                    </form>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="text-center py-5">
                                            <i class="fas fa-lightbulb fa-3x text-muted mb-3"></i>
                                            <h5 class="text-muted">No hay sugerencias disponibles</h5>
                                            <p class="text-muted">Actualiza tu perfil para recibir mejores sugerencias</p>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>

                            <!-- Pestaña de solicitudes pendientes -->
                            <c:when test="${activeTab eq 'pending'}">
                                <c:choose>
                                    <c:when test="${not empty pendingRequests}">
                                        <c:forEach var="request" items="${pendingRequests}">
                                            <div class="user-item d-flex align-items-center">
                                                <div class="large-avatar me-3">
                                                    ${request.firstName.charAt(0)}${request.lastName.charAt(0)}
                                                </div>
                                                <div class="flex-grow-1">
                                                    <h6 class="mb-1">
                                                        <a href="${pageContext.request.contextPath}/profile/${request.id}" class="text-decoration-none">
                                                            ${request.firstName} ${request.lastName}
                                                        </a>
                                                    </h6>
                                                    <p class="mb-1 text-muted">
                                                        <c:choose>
                                                            <c:when test="${not empty request.headline}">
                                                                ${request.headline}
                                                            </c:when>
                                                            <c:otherwise>
                                                                Profesional en ProLink
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </p>
                                                    <span class="stat-badge">Solicitud pendiente</span>
                                                </div>
                                                <div>
                                                    <form action="${pageContext.request.contextPath}/connections" method="post" style="display: inline;" class="me-2">
                                                        <input type="hidden" name="action" value="acceptRequest">
                                                        <input type="hidden" name="userId" value="${request.id}">
                                                        <button type="submit" class="btn btn-success btn-sm">
                                                            <i class="fas fa-check me-1"></i>Aceptar
                                                        </button>
                                                    </form>
                                                    <form action="${pageContext.request.contextPath}/connections" method="post" style="display: inline;">
                                                        <input type="hidden" name="action" value="rejectRequest">
                                                        <input type="hidden" name="userId" value="${request.id}">
                                                        <button type="submit" class="btn btn-outline-secondary btn-sm">
                                                            <i class="fas fa-times me-1"></i>Rechazar
                                                        </button>
                                                    </form>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="text-center py-5">
                                            <i class="fas fa-inbox fa-3x text-muted mb-3"></i>
                                            <h5 class="text-muted">No tienes solicitudes pendientes</h5>
                                            <p class="text-muted">Las solicitudes de conexión aparecerán aquí</p>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Custom JavaScript -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Agregar confirmaciones para acciones importantes
            const removeButtons = document.querySelectorAll('button[onclick*="confirm"]');
            removeButtons.forEach(button => {
                button.addEventListener('click', function(e) {
                    if (!confirm('¿Estás seguro de que quieres realizar esta acción?')) {
                        e.preventDefault();
                        return false;
                    }
                });
            });
        });
    </script>
</body>
</html>