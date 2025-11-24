CREATE TABLE historico_saude (
    id_evento SERIAL PRIMARY KEY,
    id_animal INT NOT NULL REFERENCES public.animais(id_animal) ON DELETE CASCADE,
    tipo_evento VARCHAR(50) NOT NULL,
    descricao TEXT,
    castrado BOOLEAN DEFAULT false,
    data_evento TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE vacinacoes (
    id_vacinacao SERIAL PRIMARY KEY,
    id_animal INT NOT NULL REFERENCES public.animais(id_animal) ON DELETE CASCADE,
    nome_vacina VARCHAR(100) NOT NULL,
    data_aplicacao DATE NOT NULL,
    dose VARCHAR(50),
    validade DATE,
    veterinario VARCHAR(255),
    observacoes TEXT
);



INSERT INTO historico_saude (id_animal, tipo_evento, descricao, data_evento)
SELECT a.id_animal, 'Doença', s.doencas, NOW()
FROM public.animais a
JOIN public.saude s ON a.id_saude = s.id_saude
WHERE s.doencas IS NOT NULL;

INSERT INTO historico_saude (id_animal, tipo_evento, descricao, data_evento)
SELECT a.id_animal, 'Deficiência', s.deficiencias, NOW()
FROM public.animais a
JOIN public.saude s ON a.id_saude = s.id_saude
WHERE s.deficiencias IS NOT NULL;

INSERT INTO historico_saude (id_animal, tipo_evento, descricao, data_evento)
SELECT a.id_animal, 'Histórico', s.historico_doencas, NOW()
FROM public.animais a
JOIN public.saude s ON a.id_saude = s.id_saude
WHERE s.historico_doencas IS NOT NULL;

ALTER TABLE animais
    DROP CONSTRAINT animais_id_saude_fkey,
    DROP COLUMN id_saude;


DROP TABLE saude;
