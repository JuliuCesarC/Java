ALTER TABLE usuarios ADD cargo VARCHAR(50);
UPDATE usuarios SET cargo = 'USER';
ALTER TABLE usuarios CHANGE cargo cargo VARCHAR(50) NOT NULL;