-- 1. Tabela Arena
CREATE TABLE arena (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    endereco VARCHAR(200),
    capacidade INT
);

-- 2. Tabela Equipe
CREATE TABLE equipe (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    responsavel VARCHAR(100),
    telefone VARCHAR(20)
);

-- 3. Tabela Jogador
CREATE TABLE jogador (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    posicao VARCHAR(50),
    numero_camisa INT,
    equipe_id BIGINT NOT NULL,
    CONSTRAINT fk_jogador_equipe FOREIGN KEY (equipe_id) REFERENCES equipe(id) ON DELETE CASCADE
);

-- 4. Tabela Campeonato
CREATE TABLE campeonato (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    data_inicio DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    campeao_id BIGINT,
    CONSTRAINT fk_campeonato_campeao FOREIGN KEY (campeao_id) REFERENCES equipe(id)
);

-- 5. Tabela de Associacao: campeonato_equipe (Inscricao de Equipes no Campeonato)
CREATE TABLE campeonato_equipe (
    campeonato_id BIGINT NOT NULL,
    equipe_id BIGINT NOT NULL,
    PRIMARY KEY (campeonato_id, equipe_id),
    CONSTRAINT fk_ce_campeonato FOREIGN KEY (campeonato_id) REFERENCES campeonato(id) ON DELETE CASCADE,
    CONSTRAINT fk_ce_equipe FOREIGN KEY (equipe_id) REFERENCES equipe(id) ON DELETE CASCADE
);

-- 6. Tabela Partida
CREATE TABLE partida (
    id BIGSERIAL PRIMARY KEY,
    rodada INT NOT NULL,
    data_hora TIMESTAMP,
    placar_mandante INT,
    placar_visitante INT,
    status VARCHAR(20) NOT NULL,
    arena_id BIGINT,
    equipe_mandante_id BIGINT NOT NULL,
    equipe_visitante_id BIGINT NOT NULL,
    vencedor_id BIGINT,
    campeonato_id BIGINT NOT NULL,
    CONSTRAINT fk_partida_arena FOREIGN KEY (arena_id) REFERENCES arena(id),
    CONSTRAINT fk_partida_mandante FOREIGN KEY (equipe_mandante_id) REFERENCES equipe(id),
    CONSTRAINT fk_partida_visitante FOREIGN KEY (equipe_visitante_id) REFERENCES equipe(id),
    CONSTRAINT fk_partida_vencedor FOREIGN KEY (vencedor_id) REFERENCES equipe(id),
    CONSTRAINT fk_partida_campeonato FOREIGN KEY (campeonato_id) REFERENCES campeonato(id) ON DELETE CASCADE
);