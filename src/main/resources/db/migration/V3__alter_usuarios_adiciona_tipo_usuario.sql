-- 1. Criação da tabela TIPO_USUARIOS
CREATE TABLE IF NOT EXISTS TIPO_USUARIOS (
    id_tipo_usuario SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE
);

-- 2. Inserção dos tipos de usuário padrão
INSERT INTO TIPO_USUARIOS (nome) VALUES
('admin'),
('user')
ON CONFLICT (nome) DO NOTHING;

-- 3. Adição da coluna id_tipo_usuario na tabela USUARIOS
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'usuarios'
          AND column_name = 'id_tipo_usuario'
    ) THEN
        ALTER TABLE USUARIOS
        ADD COLUMN id_tipo_usuario INT REFERENCES TIPO_USUARIOS(id_tipo_usuario) ON DELETE SET NULL;
    END IF;
END $$;

-- 4. Criação de índice para id_tipo_usuario
CREATE INDEX IF NOT EXISTS idx_usuarios_tipo_usuario ON USUARIOS(id_tipo_usuario);

-- 5. Criação de restrição de unicidade para o campo email
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.table_constraints tc
        JOIN information_schema.constraint_column_usage ccu
            ON ccu.constraint_name = tc.constraint_name
        WHERE tc.table_name = 'usuarios'
          AND tc.constraint_type = 'UNIQUE'
          AND ccu.column_name = 'email'
    ) THEN
        ALTER TABLE USUARIOS ADD CONSTRAINT usuarios_email_unique UNIQUE (email);
    END IF;
END $$;

-- 6. Inserção do usuário administrador padrão
INSERT INTO USUARIOS (
    nome,
    email,
    senha,
    user_role,
    is_active,
    id_tipo_usuario
)
VALUES (
    'Admin',
    'admin@apa.com.br',
    '$2a$12$abcdefg1234567890abcdefg1234567890abcdefg1234567890abcdefg12', -- hash simulada
    'admin',
    TRUE,
    (SELECT id_tipo_usuario FROM TIPO_USUARIOS WHERE nome = 'admin')
)
ON CONFLICT (email) DO NOTHING;
