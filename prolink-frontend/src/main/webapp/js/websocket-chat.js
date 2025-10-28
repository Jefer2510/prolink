// WebSocket Chat Manager para ProLink
class ChatWebSocket {
    constructor() {
        this.socket = null;
        this.isConnected = false;
        this.reconnectInterval = 5000; // 5 segundos
        this.maxReconnectAttempts = 10;
        this.reconnectAttempts = 0;
        this.currentConversationId = null;
        this.messageHandlers = [];
        this.statusHandlers = [];
        
        this.init();
    }
    
    init() {
        // Obtener información del usuario actual desde la página
        this.currentUserId = this.getCurrentUserId();
        if (this.currentUserId) {
            this.connect();
        }
    }
    
    getCurrentUserId() {
        // Extraer el ID del usuario desde el contexto de la página
        const userElement = document.querySelector('[data-user-id]');
        if (userElement) {
            return userElement.getAttribute('data-user-id');
        }
        
        // Alternativa: extraer desde la URL o variable de sesión
        const pathParts = window.location.pathname.split('/');
        const messagesIndex = pathParts.indexOf('messages');
        if (messagesIndex > -1 && pathParts[messagesIndex + 2]) {
            return pathParts[messagesIndex + 2];
        }
        
        return null;
    }
    
    connect() {
        try {
            // Construir URL del WebSocket
            const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
            const host = window.location.hostname;
            const port = '8080'; // Puerto del backend
            const wsUrl = `${protocol}//${host}:${port}/ws/chat`;
            
            console.log('Conectando a WebSocket:', wsUrl);
            
            this.socket = new WebSocket(wsUrl);
            
            this.socket.onopen = (event) => {
                console.log('WebSocket conectado');
                this.isConnected = true;
                this.reconnectAttempts = 0;
                
                // Registrar usuario en el servidor
                this.sendMessage({
                    type: 'REGISTER',
                    senderId: this.currentUserId
                });
                
                // Notificar a los handlers
                this.statusHandlers.forEach(handler => handler('connected'));
            };
            
            this.socket.onmessage = (event) => {
                try {
                    const message = JSON.parse(event.data);
                    console.log('Mensaje recibido:', message);
                    this.handleIncomingMessage(message);
                } catch (error) {
                    console.error('Error procesando mensaje:', error);
                }
            };
            
            this.socket.onclose = (event) => {
                console.log('WebSocket desconectado:', event.code, event.reason);
                this.isConnected = false;
                
                // Notificar a los handlers
                this.statusHandlers.forEach(handler => handler('disconnected'));
                
                // Intentar reconectar si no fue una desconexión intencional
                if (event.code !== 1000 && this.reconnectAttempts < this.maxReconnectAttempts) {
                    setTimeout(() => this.reconnect(), this.reconnectInterval);
                }
            };
            
            this.socket.onerror = (error) => {
                console.error('Error de WebSocket:', error);
                this.statusHandlers.forEach(handler => handler('error'));
            };
            
        } catch (error) {
            console.error('Error conectando WebSocket:', error);
        }
    }
    
    reconnect() {
        this.reconnectAttempts++;
        console.log(`Reintentando conexión ${this.reconnectAttempts}/${this.maxReconnectAttempts}`);
        this.connect();
    }
    
    disconnect() {
        if (this.socket && this.isConnected) {
            this.socket.close(1000, 'Desconexión intencional');
        }
    }
    
    sendMessage(messageData) {
        if (this.socket && this.isConnected) {
            try {
                this.socket.send(JSON.stringify(messageData));
                return true;
            } catch (error) {
                console.error('Error enviando mensaje:', error);
                return false;
            }
        }
        
        console.warn('WebSocket no conectado, mensaje no enviado');
        return false;
    }
    
    sendChatMessage(receiverId, content) {
        const messageData = {
            type: 'CHAT',
            senderId: this.currentUserId,
            receiverId: receiverId,
            content: content,
            timestamp: new Date().toISOString()
        };
        
        return this.sendMessage(messageData);
    }
    
    joinConversation(conversationId) {
        this.currentConversationId = conversationId;
        
        const joinData = {
            type: 'JOIN_CONVERSATION',
            senderId: this.currentUserId,
            conversationId: conversationId
        };
        
        this.sendMessage(joinData);
    }
    
    leaveConversation() {
        if (this.currentConversationId) {
            const leaveData = {
                type: 'LEAVE_CONVERSATION',
                senderId: this.currentUserId,
                conversationId: this.currentConversationId
            };
            
            this.sendMessage(leaveData);
            this.currentConversationId = null;
        }
    }
    
    markAsRead(conversationId) {
        const readData = {
            type: 'MARK_READ',
            senderId: this.currentUserId,
            conversationId: conversationId
        };
        
        this.sendMessage(readData);
    }
    
    handleIncomingMessage(message) {
        switch (message.type) {
            case 'CHAT':
                this.handleChatMessage(message);
                break;
            case 'USER_ONLINE':
                this.handleUserOnline(message);
                break;
            case 'USER_OFFLINE':
                this.handleUserOffline(message);
                break;
            case 'TYPING':
                this.handleTyping(message);
                break;
            case 'MESSAGE_READ':
                this.handleMessageRead(message);
                break;
            default:
                console.log('Tipo de mensaje desconocido:', message.type);
        }
        
        // Notificar a todos los handlers registrados
        this.messageHandlers.forEach(handler => handler(message));
    }
    
    handleChatMessage(message) {
        // Solo procesar si es para la conversación actual
        if (this.currentConversationId && 
            (message.senderId === this.currentConversationId || 
             message.receiverId === this.currentConversationId)) {
            
            this.addMessageToUI(message);
            
            // Marcar como leído automáticamente si la ventana está activa
            if (document.hasFocus() && message.senderId !== this.currentUserId) {
                this.markAsRead(message.senderId);
            }
        } else {
            // Mostrar notificación para mensajes de otras conversaciones
            this.showNotification(message);
        }
    }
    
    handleUserOnline(message) {
        this.updateUserStatus(message.userId, true);
    }
    
    handleUserOffline(message) {
        this.updateUserStatus(message.userId, false);
    }
    
    handleTyping(message) {
        if (message.conversationId === this.currentConversationId) {
            this.showTypingIndicator(message.senderId, message.isTyping);
        }
    }
    
    handleMessageRead(message) {
        // Actualizar indicadores de lectura en la UI
        this.updateReadStatus(message.messageId);
    }
    
    addMessageToUI(message) {
        const messagesList = document.getElementById('messagesList');
        if (!messagesList) return;
        
        const isOwn = message.senderId === this.currentUserId;
        const messageDiv = document.createElement('div');
        messageDiv.className = 'message-item ' + (isOwn ? 'own' : 'other');
        messageDiv.setAttribute('data-message-id', message.id || Date.now());
        
        const timestamp = new Date(message.timestamp || Date.now())
            .toLocaleTimeString('es-ES', {hour: '2-digit', minute: '2-digit'});
        
        messageDiv.innerHTML = `
            ${!isOwn ? `<div class="small-avatar me-2">${this.getAvatarInitials(message)}</div>` : ''}
            <div class="message-bubble ${isOwn ? 'own' : 'other'}">
                <p class="mb-1">${this.escapeHtml(message.content)}</p>
                <small class="opacity-75">${timestamp}</small>
            </div>
        `;
        
        // Agregar con animación suave
        messageDiv.style.opacity = '0';
        messageDiv.style.transform = 'translateY(20px)';
        messagesList.appendChild(messageDiv);
        
        // Animar entrada
        requestAnimationFrame(() => {
            messageDiv.style.transition = 'all 0.3s ease';
            messageDiv.style.opacity = '1';
            messageDiv.style.transform = 'translateY(0)';
        });
        
        this.scrollToBottom();
    }
    
    getAvatarInitials(message) {
        // Obtener iniciales desde el contexto de la página o mensaje
        const otherUserElement = document.querySelector('[data-other-user]');
        if (otherUserElement) {
            const firstName = otherUserElement.getAttribute('data-first-name');
            const lastName = otherUserElement.getAttribute('data-last-name');
            if (firstName && lastName) {
                return firstName.charAt(0) + lastName.charAt(0);
            }
        }
        
        // Fallback
        return message.senderName ? message.senderName.split(' ').map(n => n.charAt(0)).join('') : 'U';
    }
    
    escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
    
    scrollToBottom() {
        const messagesList = document.getElementById('messagesList');
        if (messagesList) {
            messagesList.scrollTop = messagesList.scrollHeight;
        }
    }
    
    updateUserStatus(userId, isOnline) {
        // Actualizar indicadores de estado en línea
        const indicators = document.querySelectorAll(`[data-user-id="${userId}"] .online-indicator`);
        indicators.forEach(indicator => {
            indicator.style.display = isOnline ? 'block' : 'none';
        });
    }
    
    showTypingIndicator(userId, isTyping) {
        const typingIndicator = document.getElementById('typingIndicator');
        if (typingIndicator) {
            if (isTyping) {
                typingIndicator.style.display = 'block';
                typingIndicator.textContent = 'Escribiendo...';
            } else {
                typingIndicator.style.display = 'none';
            }
        }
    }
    
    updateReadStatus(messageId) {
        // Actualizar indicadores de mensaje leído
        const messageElement = document.querySelector(`[data-message-id="${messageId}"]`);
        if (messageElement) {
            const readIndicator = messageElement.querySelector('.read-indicator');
            if (readIndicator) {
                readIndicator.classList.add('read');
            }
        }
    }
    
    showNotification(message) {
        // Mostrar notificación del navegador si está permitido
        if (Notification.permission === 'granted') {
            new Notification(`Nuevo mensaje de ${message.senderName || 'Usuario'}`, {
                body: message.content,
                icon: '/favicon.ico'
            });
        }
    }
    
    // Métodos para registrar handlers
    onMessage(handler) {
        this.messageHandlers.push(handler);
    }
    
    onStatusChange(handler) {
        this.statusHandlers.push(handler);
    }
    
    // Método para enviar indicador de escritura
    sendTypingIndicator(receiverId, isTyping) {
        const typingData = {
            type: 'TYPING',
            senderId: this.currentUserId,
            receiverId: receiverId,
            conversationId: this.currentConversationId,
            isTyping: isTyping
        };
        
        this.sendMessage(typingData);
    }
}

// Inicializar WebSocket cuando la página esté lista
let chatWS = null;

document.addEventListener('DOMContentLoaded', function() {
    // Solo inicializar en la página de mensajes
    if (window.location.pathname.includes('/messages')) {
        chatWS = new ChatWebSocket();
        
        // Configurar handlers de la UI
        setupMessageUI();
        setupTypingIndicators();
        requestNotificationPermission();
    }
});

function setupMessageUI() {
    const messageForm = document.getElementById('messageForm');
    const messageInput = document.getElementById('messageInput');
    
    if (messageForm && messageInput) {
        // Interceptar envío del formulario para usar WebSocket
        messageForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            const content = messageInput.value.trim();
            const receiverId = document.querySelector('[name="receiverId"]').value;
            
            if (content && chatWS && chatWS.isConnected) {
                // Enviar por WebSocket
                if (chatWS.sendChatMessage(receiverId, content)) {
                    // Limpiar input si se envió exitosamente
                    messageInput.value = '';
                }
            } else {
                // Fallback: envío tradicional por formulario
                messageForm.submit();
            }
        });
        
        // Manejar Enter para enviar
        messageInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                messageForm.dispatchEvent(new Event('submit'));
            }
        });
    }
}

function setupTypingIndicators() {
    const messageInput = document.getElementById('messageInput');
    if (messageInput && chatWS) {
        let typingTimer;
        let isTyping = false;
        
        messageInput.addEventListener('input', function() {
            const receiverId = document.querySelector('[name="receiverId"]').value;
            
            if (!isTyping) {
                isTyping = true;
                chatWS.sendTypingIndicator(receiverId, true);
            }
            
            // Limpiar timer anterior
            clearTimeout(typingTimer);
            
            // Establecer nuevo timer para dejar de escribir
            typingTimer = setTimeout(() => {
                isTyping = false;
                chatWS.sendTypingIndicator(receiverId, false);
            }, 3000);
        });
        
        messageInput.addEventListener('blur', function() {
            if (isTyping) {
                isTyping = false;
                const receiverId = document.querySelector('[name="receiverId"]').value;
                chatWS.sendTypingIndicator(receiverId, false);
            }
        });
    }
}

function requestNotificationPermission() {
    if ('Notification' in window && Notification.permission === 'default') {
        Notification.requestPermission();
    }
}

// Limpiar conexión cuando se cierre la página
window.addEventListener('beforeunload', function() {
    if (chatWS) {
        chatWS.disconnect();
    }
});

// Exponer funciones globales si es necesario
window.chatWS = chatWS;