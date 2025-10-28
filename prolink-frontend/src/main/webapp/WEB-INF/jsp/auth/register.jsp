<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Crear Cuenta - ProLink</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .register-container {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 2rem 0;
        }
        .register-card {
            background: white;
            border-radius: 15px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            width: 100%;
            max-width: 450px;
        }
        .register-header {
            background: linear-gradient(135deg, #0a66c2, #004182);
            color: white;
            padding: 2rem;
            text-align: center;
        }
        .register-body {
            padding: 2rem;
        }
        .form-control:focus {
            border-color: #0a66c2;
            box-shadow: 0 0 0 0.2rem rgba(10, 102, 194, 0.25);
        }
        .btn-linkedin {
            background: linear-gradient(135deg, #0a66c2, #004182);
            border: none;
            color: white;
            padding: 12px 30px;
            border-radius: 25px;
            font-weight: 500;
            transition: all 0.3s ease;
        }
        .btn-linkedin:hover {
            background: linear-gradient(135deg, #004182, #0a66c2);
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
            color: white;
        }
        .alert {
            border-radius: 10px;
            border: none;
        }
        .form-floating {
            margin-bottom: 1rem;
        }
        .divider {
            text-align: center;
            margin: 1.5rem 0;
            position: relative;
        }
        .divider::before {
            content: '';
            position: absolute;
            top: 50%;
            left: 0;
            right: 0;
            height: 1px;
            background: #ddd;
        }
        .divider span {
            background: white;
            padding: 0 1rem;
            color: #666;
        }
        .password-strength {
            height: 5px;
            border-radius: 3px;
            background: #eee;
            margin-top: 5px;
        }
        .password-strength.weak { background: #dc3545; }
        .password-strength.medium { background: #ffc107; }
        .password-strength.strong { background: #28a745; }
    </style>
</head>
<body>
    <div class="register-container">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-6 col-lg-5">
                    <div class="register-card">
                        <div class="register-header">
                            <i class="fab fa-linkedin fa-3x mb-3"></i>
                            <h2 class="mb-0">Únete a ProLink</h2>
                            <p class="mb-0">Construye tu red profesional</p>
                        </div>
                        
                        <div class="register-body">
                            <!-- Mostrar mensajes de error -->
                            <c:if test="${not empty error}">
                                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                    <i class="fas fa-exclamation-circle me-2"></i>${error}
                                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                </div>
                            </c:if>
                            
                            <form method="post" action="${pageContext.request.contextPath}/register" id="registerForm">
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="form-floating">
                                            <input type="text" 
                                                   class="form-control" 
                                                   id="firstName" 
                                                   name="firstName" 
                                                   placeholder="Nombre"
                                                   value="${firstName}"
                                                   required>
                                            <label for="firstName">
                                                <i class="fas fa-user me-2"></i>Nombre
                                            </label>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-floating">
                                            <input type="text" 
                                                   class="form-control" 
                                                   id="lastName" 
                                                   name="lastName" 
                                                   placeholder="Apellido"
                                                   value="${lastName}"
                                                   required>
                                            <label for="lastName">
                                                <i class="fas fa-user me-2"></i>Apellido
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                
                                <div class="form-floating">
                                    <input type="email" 
                                           class="form-control" 
                                           id="email" 
                                           name="email" 
                                           placeholder="correo@ejemplo.com"
                                           value="${email}"
                                           required>
                                    <label for="email">
                                        <i class="fas fa-envelope me-2"></i>Correo electrónico
                                    </label>
                                </div>
                                
                                <div class="form-floating">
                                    <input type="password" 
                                           class="form-control" 
                                           id="password" 
                                           name="password" 
                                           placeholder="Contraseña"
                                           minlength="6"
                                           required>
                                    <label for="password">
                                        <i class="fas fa-lock me-2"></i>Contraseña
                                    </label>
                                    <div class="password-strength" id="passwordStrength"></div>
                                    <small class="text-muted">Mínimo 6 caracteres</small>
                                </div>
                                
                                <div class="form-floating">
                                    <input type="password" 
                                           class="form-control" 
                                           id="confirmPassword" 
                                           name="confirmPassword" 
                                           placeholder="Confirmar contraseña"
                                           required>
                                    <label for="confirmPassword">
                                        <i class="fas fa-lock me-2"></i>Confirmar contraseña
                                    </label>
                                </div>
                                
                                <div class="form-check mb-3">
                                    <input class="form-check-input" 
                                           type="checkbox" 
                                           id="acceptTerms" 
                                           name="acceptTerms"
                                           required>
                                    <label class="form-check-label" for="acceptTerms">
                                        Acepto los <a href="#" class="text-decoration-none">términos y condiciones</a>
                                    </label>
                                </div>
                                
                                <div class="d-grid">
                                    <button type="submit" class="btn btn-linkedin">
                                        <i class="fas fa-user-plus me-2"></i>Crear Cuenta
                                    </button>
                                </div>
                            </form>
                            
                            <div class="divider">
                                <span>¿Ya tienes cuenta?</span>
                            </div>
                            
                            <div class="d-grid">
                                <a href="${pageContext.request.contextPath}/login" 
                                   class="btn btn-outline-primary">
                                    <i class="fas fa-sign-in-alt me-2"></i>Iniciar Sesión
                                </a>
                            </div>
                        </div>
                    </div>
                    
                    <div class="text-center mt-4">
                        <small class="text-white">
                            © 2025 ProLink. Todos los derechos reservados.
                        </small>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const form = document.getElementById('registerForm');
            const password = document.getElementById('password');
            const confirmPassword = document.getElementById('confirmPassword');
            const passwordStrength = document.getElementById('passwordStrength');
            
            // Validación de fortaleza de contraseña
            password.addEventListener('input', function() {
                const value = this.value;
                let strength = 0;
                
                if (value.length >= 6) strength++;
                if (value.match(/[a-z]/)) strength++;
                if (value.match(/[A-Z]/)) strength++;
                if (value.match(/[0-9]/)) strength++;
                if (value.match(/[^a-zA-Z0-9]/)) strength++;
                
                passwordStrength.className = 'password-strength';
                if (strength < 2) {
                    passwordStrength.classList.add('weak');
                } else if (strength < 4) {
                    passwordStrength.classList.add('medium');
                } else {
                    passwordStrength.classList.add('strong');
                }
            });
            
            // Validación de confirmación de contraseña
            confirmPassword.addEventListener('input', function() {
                if (this.value !== password.value) {
                    this.setCustomValidity('Las contraseñas no coinciden');
                } else {
                    this.setCustomValidity('');
                }
            });
            
            // Validación del formulario
            form.addEventListener('submit', function(event) {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            });
        });
    </script>
</body>
</html>