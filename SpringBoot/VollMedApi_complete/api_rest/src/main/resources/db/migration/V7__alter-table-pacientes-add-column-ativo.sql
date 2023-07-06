ALTER TABLE pacientes ADD ativo TINYINT;
UPDATE pacientes SET ativo = 1;
ALTER TABLE pacientes CHANGE ativo ativo TINYINT NOT NULL