-- Crear tablas para ProLink en Oracle
-- Ejecutar como usuario prolink

-- Tabla de usuarios
CREATE TABLE users (
    id NUMBER(19) PRIMARY KEY,
    username VARCHAR2(50) UNIQUE NOT NULL,
    email VARCHAR2(100) UNIQUE NOT NULL,
    password VARCHAR2(255) NOT NULL,
    first_name VARCHAR2(50) NOT NULL,
    last_name VARCHAR2(50) NOT NULL,
    professional_title VARCHAR2(100),
    industry VARCHAR2(50),
    location VARCHAR2(100),
    profile_image_url VARCHAR2(255),
    banner_image_url VARCHAR2(255),
    bio VARCHAR2(500),
    skills VARCHAR2(500),
    experience_years NUMBER(3),
    education VARCHAR2(255),
    languages VARCHAR2(255),
    website_url VARCHAR2(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    role VARCHAR2(20) DEFAULT 'USER'
);

-- Tabla de posts
CREATE TABLE posts (
    id NUMBER(19) PRIMARY KEY,
    author_id NUMBER(19) NOT NULL,
    content VARCHAR2(4000) NOT NULL,
    image_url VARCHAR2(255),
    post_type VARCHAR2(20) DEFAULT 'TEXT',
    privacy VARCHAR2(20) DEFAULT 'PUBLIC',
    likes_count NUMBER(10) DEFAULT 0,
    comments_count NUMBER(10) DEFAULT 0,
    shares_count NUMBER(10) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_post_author FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Tabla de likes
CREATE TABLE likes (
    id NUMBER(19) PRIMARY KEY,
    user_id NUMBER(19) NOT NULL,
    post_id NUMBER(19),
    comment_id NUMBER(19),
    like_type VARCHAR2(20) DEFAULT 'POST',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_like_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_like_post FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
);

-- Tabla de comentarios
CREATE TABLE comments (
    id NUMBER(19) PRIMARY KEY,
    author_id NUMBER(19) NOT NULL,
    post_id NUMBER(19) NOT NULL,
    content VARCHAR2(1000) NOT NULL,
    likes_count NUMBER(10) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_comment_author FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_post FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
);

-- Tabla de conexiones
CREATE TABLE connections (
    id NUMBER(19) PRIMARY KEY,
    requester_id NUMBER(19) NOT NULL,
    addressee_id NUMBER(19) NOT NULL,
    status VARCHAR2(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_conn_requester FOREIGN KEY (requester_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_conn_addressee FOREIGN KEY (addressee_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Tabla de conversaciones
CREATE TABLE conversations (
    id NUMBER(19) PRIMARY KEY,
    participant1_id NUMBER(19) NOT NULL,
    participant2_id NUMBER(19) NOT NULL,
    last_message_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_conv_part1 FOREIGN KEY (participant1_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_conv_part2 FOREIGN KEY (participant2_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Tabla de mensajes
CREATE TABLE messages (
    id NUMBER(19) PRIMARY KEY,
    sender_id NUMBER(19) NOT NULL,
    conversation_id NUMBER(19) NOT NULL,
    content VARCHAR2(2000) NOT NULL,
    message_type VARCHAR2(20) DEFAULT 'TEXT',
    is_read NUMBER(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_msg_sender FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_msg_conversation FOREIGN KEY (conversation_id) REFERENCES conversations(id) ON DELETE CASCADE
);

-- Secuencias para IDs
CREATE SEQUENCE users_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE posts_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE likes_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE comments_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE connections_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE conversations_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE messages_seq START WITH 1 INCREMENT BY 1;

COMMIT;