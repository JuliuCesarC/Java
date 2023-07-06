/* -------------------- flyway_schema_history -------------------- */
create table flyway_schema_history;

SELECT * from flyway_schema_history;

DELETE FROM flyway_schema_history WHERE success = 0;

delete from flyway_schema_history WHERE success = 1;

/* -------------------- medicos -------------------- */
SELECT * FROM medicos;

DESC medicos;

Update medicos set ativo = 1 where id > 0;

/* -------------------- pacientes -------------------- */

select * from pacientes;

/* -------------------- usuarios -------------------- */
select * from usuarios;

Insert into usuarios values(1, 'admin@ad.min', '$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.', 'ADMIN');

Insert into usuarios values(2, 'jenifer@voll.med', '$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.', 'MANAGER');

Insert into usuarios values(3, 'jao@voll.med', '$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.', 'USER');

delete from usuarios where id > 0;

/* -------------------- consultas -------------------- */
SELECT motivo_cancelamento FROM consultas;

SELECT * FROM consultas;

update consultas set motivo_cancelamento = null Where id = 7;

select * from consultas where motivo_cancelamento != null;

/* -------------------- query_api -------------------- */
select * from medicos
  where
  ativo = 1
  and
  especialidade = 'CARDIOLOGIA'
  and
  id not in(
    select medicos.id from consultas
    where
    data = '2023-06-17 08:00'
    and
    motivo_cancelamento is null
  )
  order by rand()
  limit 1;


