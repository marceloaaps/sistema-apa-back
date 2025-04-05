-- CREATE DATABASE "APA"
--      WITH
--     OWNER = postgres
--     ENCODING = 'UTF8'
--     LC_COLLATE = 'pt-BR'
--     LC_CTYPE = 'pt-BR'
--     LOCALE_PROVIDER = 'libc'
--     TABLESPACE = pg_default
--     CONNECTION LIMIT = -1
--     IS_TEMPLATE = False;


-- Deletando Tabelas para evitar conflitos
DROP TABLE IF EXISTS HISTORICO_DOACOES CASCADE;
DROP TABLE IF EXISTS HISTORICO_STATUS_ADOCAO CASCADE;
DROP TABLE IF EXISTS ADOCAO CASCADE;
DROP TABLE IF EXISTS ESTOQUES CASCADE;
DROP TABLE IF EXISTS DOACOES_ANIMAIS CASCADE;
DROP TABLE IF EXISTS ANIMAIS CASCADE;
DROP TABLE IF EXISTS DOACOES CASCADE;
DROP TABLE IF EXISTS TIPOS_DOACOES CASCADE;
DROP TABLE IF EXISTS DOADORES CASCADE;
DROP TABLE IF EXISTS USUARIOS CASCADE;
DROP TABLE IF EXISTS STATUS_ADOCAO CASCADE;
DROP TABLE IF EXISTS TIPO_USUARIOS CASCADE;
DROP TABLE IF EXISTS CATEGORIAS_ESTOQUE CASCADE;
DROP TABLE IF EXISTS OBSERVACOES_ANIMAIS CASCADE;
DROP TABLE IF EXISTS SAUDE CASCADE;

-- Criar ENUM para user_role
DO $$ BEGIN
    CREATE TYPE user_role_enum AS ENUM ('guest', 'user', 'admin');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

-- Tabela USUARIOS
CREATE TABLE USUARIOS (
    id_usuario SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    data_nascimento DATE,
    user_role user_role_enum DEFAULT 'user',
    is_active BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deletado_em TIMESTAMP DEFAULT NULL,
    deletado_por INT REFERENCES USUARIOS(id_usuario)
);

COMMENT ON COLUMN USUARIOS.deletado_em IS 'Registra a data/hora da exclusão lógica do usuário';
COMMENT ON COLUMN USUARIOS.deletado_por IS 'ID do usuário que realizou a exclusão lógica';

-- Tabela DOADORES
CREATE TABLE DOADORES (
    id_doador SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    contato VARCHAR(255),
    historico_doacoes TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela TIPOS_DOACOES
CREATE TABLE TIPOS_DOACOES (
    id_tipo_doacao SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL
);

-- Tabela DOACOES
CREATE TABLE DOACOES (
    id_doacao SERIAL PRIMARY KEY,
    id_doador INT NOT NULL REFERENCES DOADORES(id_doador) ON DELETE CASCADE,
    id_tipo_doacao INT NOT NULL REFERENCES TIPOS_DOACOES(id_tipo_doacao) ON DELETE CASCADE,
    data_doacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Nova tabela SAUDE
CREATE TABLE SAUDE (
    id_saude SERIAL PRIMARY KEY,
    doencas TEXT,
    historico_doencas TEXT,
    deficiencias TEXT
);

-- Tabela ANIMAIS
CREATE TABLE ANIMAIS (
    id_animal SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    idade INT,
    raca VARCHAR(255),
    id_saude INT REFERENCES SAUDE(id_saude) ON DELETE SET NULL,
    comportamento TEXT,
    historico TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    disponivel_para_adocao BOOLEAN DEFAULT TRUE,
    deletado_em TIMESTAMP DEFAULT NULL,
    deletado_por INT REFERENCES USUARIOS(id_usuario)
);

COMMENT ON COLUMN ANIMAIS.deletado_em IS 'Registra a data/hora da exclusão do Animal';
COMMENT ON COLUMN ANIMAIS.deletado_por IS 'ID do usuário que realizou a exclusão';

-- Tabela DOACOES_ANIMAIS
CREATE TABLE DOACOES_ANIMAIS (
    id SERIAL PRIMARY KEY,
    id_doacao INT REFERENCES DOACOES(id_doacao) ON DELETE CASCADE,
    id_animal INT REFERENCES ANIMAIS(id_animal) ON DELETE CASCADE
);

-- Tabela STATUS_ADOCAO
CREATE TABLE STATUS_ADOCAO (
    id_status_adocao SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL
);

-- Tabela ADOCAO
CREATE TABLE ADOCAO (
    id_adocao SERIAL PRIMARY KEY,
    id_animal INT NOT NULL REFERENCES ANIMAIS(id_animal) ON DELETE CASCADE,
    id_usuario INT NOT NULL REFERENCES USUARIOS(id_usuario) ON DELETE CASCADE,
    id_status_adocao INT NOT NULL REFERENCES STATUS_ADOCAO(id_status_adocao) ON DELETE CASCADE,
    data_adocao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela HISTORICO_STATUS_ADOCAO
CREATE TABLE HISTORICO_STATUS_ADOCAO (
    id_historico SERIAL PRIMARY KEY,
    id_adocao INT REFERENCES ADOCAO(id_adocao) ON DELETE CASCADE,
    id_status_adocao INT REFERENCES STATUS_ADOCAO(id_status_adocao) ON DELETE CASCADE,
    data_mudanca TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela CATEGORIAS_ESTOQUE
CREATE TABLE CATEGORIAS_ESTOQUE (
    id_categoria SERIAL PRIMARY KEY,
    nome VARCHAR(50) UNIQUE NOT NULL
);

-- Tabela ESTOQUES
CREATE TABLE ESTOQUES (
    id_estoque SERIAL PRIMARY KEY,
    id_doacao INT REFERENCES DOACOES(id_doacao) ON DELETE CASCADE,
    id_categoria INT NOT NULL REFERENCES CATEGORIAS_ESTOQUE(id_categoria) ON DELETE RESTRICT,
    nome VARCHAR(255) NOT NULL,
    quantidade INT NOT NULL,
    validade DATE,
    alerta_reposicao BOOLEAN DEFAULT FALSE
);

-- Tabela HISTORICO_DOACOES
CREATE TABLE HISTORICO_DOACOES (
    id_historico SERIAL PRIMARY KEY,
    id_doacao INT REFERENCES DOACOES(id_doacao) ON DELETE CASCADE,
    status VARCHAR(50) NOT NULL,
    data_mudanca TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela OBSERVACOES_ANIMAIS
CREATE TABLE OBSERVACOES_ANIMAIS (
    id_observacao SERIAL PRIMARY KEY,
    id_animal INT NOT NULL REFERENCES ANIMAIS(id_animal) ON DELETE CASCADE,
    data_observacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    observacao TEXT NOT NULL,
    id_responsavel INT REFERENCES USUARIOS(id_usuario),
    deletado_em TIMESTAMP DEFAULT NULL
);

-- Dados iniciais
INSERT INTO STATUS_ADOCAO (nome) VALUES
('Pendente'), ('Concluída'), ('Cancelada');

INSERT INTO TIPOS_DOACOES (nome) VALUES
('Animal'), ('Produto');

INSERT INTO CATEGORIAS_ESTOQUE (nome) VALUES
('Ração'), ('Medicamento'), ('Material de Limpeza'), ('Vacina');

-- Exemplo de inserção com nova estrutura
INSERT INTO DOADORES (nome, contato, historico_doacoes)
VALUES ('Maria Silva', 'maria@email.com', 'Doação de animais');

INSERT INTO DOACOES (id_doador, id_tipo_doacao)
VALUES (1, 1);

INSERT INTO SAUDE (doencas, historico_doencas, deficiencias)
VALUES (NULL, NULL, NULL);

INSERT INTO ANIMAIS (nome, idade, raca, id_saude, comportamento, historico)
VALUES ('Rex', 3, 'Vira-lata', 1, 'Brincalhão', 'Resgatado das ruas');

INSERT INTO DOACOES_ANIMAIS (id_doacao, id_animal)
VALUES (1, 1);

-- Índices
CREATE INDEX idx_usuario_email ON USUARIOS(email);
CREATE INDEX idx_animal_nome ON ANIMAIS(nome);
CREATE INDEX idx_doacao_data ON DOACOES(data_doacao);
CREATE INDEX idx_adocao_status ON ADOCAO(id_status_adocao);
CREATE INDEX idx_doacoes_doador ON DOACOES(id_doador);
CREATE INDEX idx_estoques_categoria ON ESTOQUES(id_categoria);
CREATE INDEX idx_adocao_usuario ON ADOCAO(id_usuario);


