ALTER TABLE medicos ADD ativo TINYINT;
UPDATE medicos SET ativo = 1;
ALTER TABLE medicos CHANGE ativo ativo TINYINT NOT NULL