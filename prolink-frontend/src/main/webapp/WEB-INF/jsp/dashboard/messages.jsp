<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ProLink - Mensajes</title>
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
        .messages-container {
            background: white;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            height: calc(100vh - 150px);
            display: flex;
        }
        .conversations-sidebar {
            width: 350px;
            border-right: 1px solid #e9ecef;
            display: flex;
            flex-direction: column;
        }
        .chat-area {
            flex-grow: 1;
            display: flex;
            flex-direction: column;
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
        .conversation-item {
            padding: 1rem;
            border-bottom: 1px solid #f1f3f4;
            cursor: pointer;
            transition: all 0.2s;
        }
        .conversation-item:hover {
            background-color: #f8f9fa;
        }
        .conversation-item.active {
            background-color: #e3f2fd;
            border-left: 3px solid #0a66c2;
        }
        .messages-list {
            flex-grow: 1;
            overflow-y: auto;
            padding: 1rem;
            max-height: calc(100vh - 300px);
        }
        .message-item {
            margin-bottom: 1rem;
            display: flex;
            align-items: flex-start;
        }
        .message-item.own {
            justify-content: flex-end;
        }
        .message-bubble {
            max-width: 70%;
            padding: 0.75rem 1rem;
            border-radius: 18px;
            word-wrap: break-word;
        }
        .message-bubble.own {
            background: linear-gradient(135deg, #0a66c2, #004182);
            color: white;
            margin-left: auto;
        }
        .message-bubble.other {
            background: #f1f3f4;
            color: #333;
        }
        .message-input-area {
            border-top: 1px solid #e9ecef;
            padding: 1rem;
        }
        .search-box {
            border: 1px solid #ddd;
            border-radius: 20px;
            padding: 0.5rem 1rem;
        }
        .search-box:focus {
            border-color: #0a66c2;
            box-shadow: 0 0 0 0.2rem rgba(10, 102, 194, 0.25);
        }
        .unread-badge {
            background: #dc3545;
            color: white;
            border-radius: 50%;
            padding: 0.25rem 0.5rem;
            font-size: 0.75rem;
            min-width: 20px;
            text-align: center;
        }
        .online-indicator {
            width: 12px;
            height: 12px;
            background: #28a745;
            border-radius: 50%;
            border: 2px solid white;
            position: absolute;
            bottom: 2px;
            right: 2px;
        }
        .empty-state {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            height: 100%;
            color: #6c757d;
        }
        .chat-header {
            padding: 1rem;
            border-bottom: 1px solid #e9ecef;
            background-color: #f8f9fa;
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
                    <a class="nav-link active" href="${pageContext.request.contextPath}/messages">
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

    <div class="container-fluid content-area">
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

        <div class="messages-container">
            <!-- Sidebar de conversaciones -->
            <div class="conversations-sidebar">
                <!-- Header del sidebar -->
                <div class="p-3 border-bottom">
                    <h5 class="mb-3">Mensajes</h5>
                    <form action="${pageContext.request.contextPath}/messages" method="post">
                        <input type="hidden" name="action" value="searchConversations">
                        <div class="input-group">
                            <input type="text" class="form-control search-box" name="searchTerm" 
                                   placeholder="Buscar conversaciones..." value="${searchTerm}">
                            <button type="submit" class="btn btn-outline-secondary">
                                <i class="fas fa-search"></i>
                            </button>
                        </div>
                    </form>
                </div>

                <!-- Lista de conversaciones -->
                <div class="flex-grow-1 overflow-auto">
                    <c:choose>
                        <c:when test="${not empty conversations}">
                            <c:forEach var="conversation" items="${conversations}">
                                <div class="conversation-item ${conversation.id eq otherUser.id ? 'active' : ''}"
                                     onclick="window.location.href='${pageContext.request.contextPath}/messages/chat/${conversation.otherUser.id}'">
                                    <div class="d-flex align-items-center">
                                        <div class="position-relative me-3">
                                            <div class="medium-avatar">
                                                ${conversation.otherUser.firstName.charAt(0)}${conversation.otherUser.lastName.charAt(0)}
                                            </div>
                                            <div class="online-indicator"></div>
                                        </div>
                                        <div class="flex-grow-1">
                                            <div class="d-flex justify-content-between align-items-start">
                                                <h6 class="mb-1">${conversation.otherUser.firstName} ${conversation.otherUser.lastName}</h6>
                                                <small class="text-muted">${conversation.formattedTime}</small>
                                            </div>
                                            <p class="mb-1 text-muted small">${conversation.lastMessage}</p>
                                            <c:if test="${conversation.unreadCount > 0}">
                                                <span class="unread-badge">${conversation.unreadCount}</span>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state p-4">
                                <i class="fas fa-comments fa-3x mb-3"></i>
                                <h6 class="text-muted">No hay conversaciones</h6>
                                <p class="text-muted small">Inicia una conversación desde el perfil de un usuario</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Área de chat -->
            <div class="chat-area">
                <c:choose>
                    <c:when test="${pageType eq 'chat' && not empty otherUser}">
                        <!-- Header del chat -->
                        <div class="chat-header">
                            <div class="d-flex align-items-center">
                                <div class="medium-avatar me-3">
                                    ${otherUser.firstName.charAt(0)}${otherUser.lastName.charAt(0)}
                                </div>
                                <div class="flex-grow-1" data-other-user="true" 
                                     data-first-name="${otherUser.firstName}" 
                                     data-last-name="${otherUser.lastName}">
                                    <div class="d-flex justify-content-between align-items-start">
                                        <h6 class="mb-0">${otherUser.firstName} ${otherUser.lastName}</h6>
                                        <div class="text-end">
                                            <div id="connectionStatus" class="small text-muted">
                                                <i class="fas fa-circle text-success me-1"></i>En línea
                                            </div>
                                        </div>
                                    </div>
                                    <small class="text-muted">
                                        <c:choose>
                                            <c:when test="${not empty otherUser.headline}">
                                                ${otherUser.headline}
                                            </c:when>
                                            <c:otherwise>
                                                Profesional en ProLink
                                            </c:otherwise>
                                        </c:choose>
                                    </small>
                                    <div id="typingIndicator" class="small text-muted" style="display: none;">
                                        <i class="fas fa-ellipsis-h"></i> Escribiendo...
                                    </div>
                                </div>
                                <div class="dropdown">
                                    <button class="btn btn-outline-secondary btn-sm dropdown-toggle" 
                                            data-bs-toggle="dropdown">
                                        <i class="fas fa-ellipsis-v"></i>
                                    </button>
                                    <ul class="dropdown-menu">
                                        <li>
                                            <a class="dropdown-item" 
                                               href="${pageContext.request.contextPath}/profile/${otherUser.id}">
                                                <i class="fas fa-user me-2"></i>Ver perfil
                                            </a>
                                        </li>
                                        <li><hr class="dropdown-divider"></li>
                                        <li>
                                            <form action="${pageContext.request.contextPath}/messages" method="post">
                                                <input type="hidden" name="action" value="deleteConversation">
                                                <input type="hidden" name="conversationId" value="${otherUser.id}">
                                                <button type="submit" class="dropdown-item text-danger"
                                                        onclick="return confirm('¿Estás seguro de que quieres eliminar esta conversación?')">
                                                    <i class="fas fa-trash me-2"></i>Eliminar conversación
                                                </button>
                                            </form>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>

                        <!-- Lista de mensajes -->
                        <div class="messages-list" id="messagesList">
                            <c:choose>
                                <c:when test="${not empty messages}">
                                    <c:forEach var="message" items="${messages}">
                                        <div class="message-item ${message.senderId eq currentUser.id ? 'own' : 'other'}">
                                            <c:if test="${message.senderId ne currentUser.id}">
                                                <div class="small-avatar me-2">
                                                    ${otherUser.firstName.charAt(0)}${otherUser.lastName.charAt(0)}
                                                </div>
                                            </c:if>
                                            <div class="message-bubble ${message.senderId eq currentUser.id ? 'own' : 'other'}">
                                                <p class="mb-1">${message.content}</p>
                                                <small class="opacity-75">${message.formattedTime}</small>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <div class="empty-state">
                                        <i class="fas fa-comment-dots fa-3x mb-3"></i>
                                        <h6 class="text-muted">Inicia la conversación</h6>
                                        <p class="text-muted small">Envía tu primer mensaje a ${otherUser.firstName}</p>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <!-- Área de entrada de mensaje -->
                        <div class="message-input-area">
                            <form action="${pageContext.request.contextPath}/messages" method="post" id="messageForm">
                                <input type="hidden" name="action" value="sendMessage">
                                <input type="hidden" name="receiverId" value="${otherUser.id}">
                                <input type="hidden" data-user-id="${currentUser.id}" value="${currentUser.id}">
                                <div class="input-group">
                                    <input type="text" class="form-control" name="content" 
                                           placeholder="Escribe un mensaje..." 
                                           maxlength="1000" required id="messageInput">
                                    <button type="submit" class="btn btn-linkedin">
                                        <i class="fas fa-paper-plane"></i>
                                    </button>
                                </div>
                            </form>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <!-- Estado vacío - no hay chat seleccionado -->
                        <div class="empty-state">
                            <i class="fas fa-comments fa-4x mb-4"></i>
                            <h4 class="text-muted">Selecciona una conversación</h4>
                            <p class="text-muted">Elige una conversación de la lista para comenzar a chatear</p>
                            <a href="${pageContext.request.contextPath}/connections" class="btn btn-linkedin mt-3">
                                <i class="fas fa-users me-2"></i>Buscar personas
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- WebSocket Chat -->
    <script src="${pageContext.request.contextPath}/js/websocket-chat.js"></script>
    
    <!-- Custom JavaScript -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Auto-scroll al final de los mensajes
            const messagesList = document.getElementById('messagesList');
            if (messagesList) {
                messagesList.scrollTop = messagesList.scrollHeight;
            }
            
            // Envío de mensaje con Enter
            const messageInput = document.getElementById('messageInput');
            const messageForm = document.getElementById('messageForm');
            
            if (messageInput && messageForm) {
                messageInput.addEventListener('keypress', function(e) {
                    if (e.key === 'Enter' && !e.shiftKey) {
                        e.preventDefault();
                        messageForm.submit();
                    }
                });
                
                // Focus automático en el input
                messageInput.focus();
            }
            
            // Marcar mensajes como leídos
            const currentConversationId = '${otherUser.id}';
            if (currentConversationId && currentConversationId !== '') {
                // Simular marcado como leído después de 2 segundos
                setTimeout(() => {
                    fetch('${pageContext.request.contextPath}/messages', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: 'action=markAsRead&conversationId=' + currentConversationId
                    });
                }, 2000);
            }
            
            // Actualización en tiempo real (simulada)
            // TODO: Implementar WebSocket para actualizaciones en tiempo real
            
            // Confirmación de eliminación
            const deleteButtons = document.querySelectorAll('[onclick*="confirm"]');
            deleteButtons.forEach(button => {
                button.addEventListener('click', function(e) {
                    if (!confirm('¿Estás seguro de que quieres eliminar esta conversación?')) {
                        e.preventDefault();
                        return false;
                    }
                });
            });
        });
        
        // Función para scroll automático (será útil con WebSocket)
        function scrollToBottom() {
            const messagesList = document.getElementById('messagesList');
            if (messagesList) {
                messagesList.scrollTop = messagesList.scrollHeight;
            }
        }
        
        // Función para agregar nuevo mensaje (será útil con WebSocket)
        function addNewMessage(message, isOwn) {
            const messagesList = document.getElementById('messagesList');
            if (!messagesList) return;
            
            const messageDiv = document.createElement('div');
            messageDiv.className = 'message-item ' + (isOwn ? 'own' : 'other');
            
            messageDiv.innerHTML = `
                ${!isOwn ? '<div class="small-avatar me-2">' + '${otherUser.firstName.charAt(0)}${otherUser.lastName.charAt(0)}' + '</div>' : ''}
                <div class="message-bubble ${isOwn ? 'own' : 'other'}">
                    <p class="mb-1">${message.content}</p>
                    <small class="opacity-75">${new Date().toLocaleTimeString('es-ES', {hour: '2-digit', minute: '2-digit'})}</small>
                </div>
            `;
            
            messagesList.appendChild(messageDiv);
            scrollToBottom();
        }
    </script>
</body>
</html>