# 🚀 Deployment Guide - ProLink

This guide covers different deployment scenarios for ProLink in production environments.

## 📋 **Prerequisites**

- **Java 21+** installed
- **Docker** and **Docker Compose**
- **Oracle Database** (21c XE or higher)
- **Web Server** (Nginx/Apache for production)
- **SSL Certificate** (for HTTPS)

## 🐳 **Docker Deployment (Recommended)**

### **1. Quick Start with Docker Compose**

```bash
# Clone repository
git clone https://github.com/Jefer2510/prolink.git
cd prolink

# Start all services
docker-compose up -d

# Check services status
docker-compose ps
```

### **2. Manual Docker Setup**

```bash
# 1. Start Oracle Database
docker run --name oracle-xe-prolink \
  -p 1521:1521 -p 5500:5500 \
  -e ORACLE_PWD=ProLink123 \
  -d container-registry.oracle.com/database/express:21.3.0-xe

# 2. Build backend image
cd prolink-backend
docker build -t prolink-backend:latest .

# 3. Run backend container
docker run -d --name prolink-backend \
  -p 8080:8080 \
  --link oracle-xe-prolink:oracle \
  prolink-backend:latest

# 4. Serve frontend (Nginx)
docker run -d --name prolink-frontend \
  -p 80:80 \
  -v $(pwd):/usr/share/nginx/html:ro \
  nginx:alpine
```

## ☁️ **Cloud Deployment**

### **AWS Deployment**

#### **Option A: EC2 + RDS Oracle**

```bash
# 1. Launch EC2 instance (t3.medium or larger)
# 2. Create RDS Oracle instance
# 3. Configure Security Groups (ports 80, 443, 8080)

# Install dependencies on EC2
sudo yum update -y
sudo yum install -y java-21-amazon-corretto docker
sudo systemctl start docker
sudo usermod -a -G docker ec2-user

# Deploy application
git clone https://github.com/Jefer2510/prolink.git
cd prolink/prolink-backend

# Update application.properties with RDS endpoints
mvn clean package -DskipTests
java -jar target/prolink-backend-1.0.0.jar
```

#### **Option B: ECS Fargate**

```yaml
# ecs-task-definition.json
{
  "family": "prolink-task",
  "networkMode": "awsvpc",
  "requiresCompatibilities": ["FARGATE"],
  "cpu": "512",
  "memory": "1024",
  "executionRoleArn": "arn:aws:iam::ACCOUNT:role/ecsTaskExecutionRole",
  "containerDefinitions": [
    {
      "name": "prolink-backend",
      "image": "your-account.dkr.ecr.region.amazonaws.com/prolink:latest",
      "portMappings": [
        {
          "containerPort": 8080,
          "protocol": "tcp"
        }
      ],
      "environment": [
        {
          "name": "SPRING_PROFILES_ACTIVE",
          "value": "prod"
        }
      ]
    }
  ]
}
```

### **Google Cloud Platform**

```bash
# 1. Create GKE cluster
gcloud container clusters create prolink-cluster \
  --zone=us-central1-a \
  --num-nodes=2

# 2. Deploy to Kubernetes
kubectl apply -f k8s/

# 3. Expose service
kubectl expose deployment prolink-backend \
  --type=LoadBalancer \
  --port=80 \
  --target-port=8080
```

### **Heroku Deployment**

```bash
# 1. Install Heroku CLI
# 2. Login to Heroku
heroku login

# 3. Create app
heroku create prolink-app

# 4. Add Oracle addon (or use external Oracle Cloud)
heroku addons:create heroku-postgresql:hobby-dev

# 5. Configure environment variables
heroku config:set SPRING_PROFILES_ACTIVE=prod
heroku config:set JWT_SECRET=your-super-secret-key

# 6. Deploy
git push heroku main
```

## 🔧 **Production Configuration**

### **Environment Variables**

Create `application-prod.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:oracle:thin:@your-oracle-host:1521:XE
spring.datasource.username=${DB_USERNAME:prolink}
spring.datasource.password=${DB_PASSWORD}

# JWT Configuration  
jwt.secret=${JWT_SECRET}
jwt.expiration=${JWT_EXPIRATION:86400000}

# Logging
logging.level.com.prolink=INFO
logging.file.name=/var/log/prolink/application.log

# Security
server.ssl.enabled=true
server.ssl.key-store=${SSL_KEYSTORE_PATH}
server.ssl.key-store-password=${SSL_KEYSTORE_PASSWORD}

# CORS (restrict in production)
cors.allowed-origins=${CORS_ORIGINS:https://yourdomain.com}
```

### **Nginx Configuration**

```nginx
# /etc/nginx/sites-available/prolink
server {
    listen 80;
    server_name yourdomain.com www.yourdomain.com;
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name yourdomain.com www.yourdomain.com;

    ssl_certificate /path/to/your/certificate.crt;
    ssl_certificate_key /path/to/your/private.key;

    # Frontend
    location / {
        root /var/www/prolink;
        index prolink-app.html;
        try_files $uri $uri/ =404;
    }

    # Backend API
    location /api/ {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

## 📊 **Monitoring & Health Checks**

### **Health Check Endpoints**

```bash
# Application health
curl https://yourdomain.com/api/v1/auth/health

# Database connectivity
curl https://yourdomain.com/actuator/health

# Metrics
curl https://yourdomain.com/actuator/metrics
```

### **Docker Health Check**

```dockerfile
# Add to Dockerfile
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/api/v1/auth/health || exit 1
```

## 🔒 **Security Checklist**

- [ ] **HTTPS** enabled with valid SSL certificate
- [ ] **JWT secret** is securely generated and stored
- [ ] **Database credentials** are encrypted/secured
- [ ] **CORS** is configured for production domains only
- [ ] **Rate limiting** implemented
- [ ] **Input validation** on all endpoints
- [ ] **Security headers** configured
- [ ] **Regular security updates** scheduled

## 🚨 **Troubleshooting**

### **Common Issues**

#### **Database Connection Fails**
```bash
# Check Oracle container
docker logs oracle-xe-prolink

# Test connection
docker exec -it oracle-xe-prolink sqlplus prolink/ProLink123@XEPDB1
```

#### **JWT Token Issues**
```bash
# Verify JWT secret length (must be 512+ bits for HS512)
echo "your-secret" | wc -c

# Check token expiration
curl -H "Authorization: Bearer YOUR_TOKEN" \
     https://yourdomain.com/api/v1/users/profile
```

#### **CORS Errors**
```javascript
// Check browser console for CORS errors
// Verify application-prod.properties CORS settings
cors.allowed-origins=https://yourdomain.com
```

## 📈 **Performance Optimization**

### **Database**
- Enable Oracle connection pooling
- Configure appropriate JVM heap size
- Use database indexing for queries

### **Application**
- Enable Spring Boot actuator for monitoring
- Configure proper logging levels
- Use caching for static content

### **Frontend**
- Enable gzip compression in Nginx
- Minify CSS/JavaScript files
- Use CDN for static assets

## 🔄 **CI/CD Pipeline**

### **GitHub Actions Example**

```yaml
# .github/workflows/deploy.yml
name: Deploy to Production

on:
  push:
    branches: [main]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Setup Java 21
        uses: actions/setup-java@v3
        with:
          java-version: '21'
          distribution: 'temurin'
          
      - name: Build Backend
        run: |
          cd prolink-backend
          mvn clean package -DskipTests
          
      - name: Deploy to Server
        run: |
          # Your deployment script here
          scp target/*.jar user@server:/opt/prolink/
          ssh user@server "sudo systemctl restart prolink"
```

---

For questions or support, please open an issue on [GitHub](https://github.com/Jefer2510/prolink/issues).