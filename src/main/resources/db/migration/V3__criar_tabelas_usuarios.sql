-- V3: Criacao das tabelas de seguranca (Usuarios e Papeis)

CREATE TABLE papel (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE usuario_papel (
    usuario_id BIGINT NOT NULL,
    papel_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, papel_id),
    CONSTRAINT fk_up_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE,
    CONSTRAINT fk_up_papel FOREIGN KEY (papel_id) REFERENCES papel(id) ON DELETE CASCADE
);

-- Inserir papeis padroes da aplicacao
INSERT INTO papel (nome) VALUES ('ROLE_ADMIN');
INSERT INTO papel (nome) VALUES ('ROLE_ORGANIZADOR');
