Alter TABLE animais
ADD COLUMN rg_animal VARCHAR(50) UNIQUE,
ADD COLUMN especie VARCHAR(100) ,
ADD COLUMN sexo CHAR(1) CHECK(SEXO IN ('M', 'F')),
ADD COLUMN cor VARCHAR(100);

UPDATE animais
SET	especie = 'Desconhecida'
WHERE especie IS NULL;

ALTER TABLE animais
    ALTER COLUMN especie SET DEFAULT 'Desconhecida',
    ALTER COLUMN especie SET NOT NULL;