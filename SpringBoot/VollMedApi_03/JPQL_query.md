# Explicando detalhadamente a query com JPQL

Neste arquivo temos a explicação detalhada da consulta JPQL feita no repository da entidade médico. Vamos detalhar linha por linha e salientando as partes importantes.

```java
@Query("""
        select m from Medico m
          where m.ativo = true
          and m.especialidade = :especialidade
          and m.id not in(
              select c.medico.id from Consulta c
                where c.data = :data
          )
        order by rand()
        limit 1
    """)
Medico escolherMedicoAleatorioLivreNaData(Especialidade especialidade, LocalDateTime data);
```

1. Seleciona todos os médicos que são da entidade `Medico` e adiciona um alias `m` para eles. Como boa pratica utilizamos esse mesmo apelido no *select*, pois utilizar o clássico `*` da consulta sql pode nos aproximar de uma consulta relacional, o que não é desejado se estamos utilizando o formato JPQL.

2. Filtra apenas os médicos que estão ativos. Utilizamos o alias `m` para selecionar o campo `ativo`.

3. Filtra apenas os médicos que tenham a especialidade igual a do parâmetro `especialidade`. Utilizamos o **:** para informar o parâmetro do método e o nome utilizado na consulta precisa ser o mesmo do assinado no método. Logo se no método fosse assinado o parâmetro `especialidadeMedico`, na consulta deveria ser feito `m.especialidade = :especialidadeMedico`.

4. Verifica se o id do médico não esta incluso na sub query, que esta selecionando os ids dos médicos que possuem consulta nessa data.

5. Seleciona apenas o campo do id do médico na entidade `Consulta` e adiciona um alias `c`.

6. Filtra apenas as entidades que tenham a data igual a do parâmetro `data`. Assim como na linha de especialidade, utilizamos o **:** para chamar o valor da data para a consulta.

7. Ordena a lista de forma aleatório. O `rand()` é o responsável por deixar os itens organizados de forma aleatória.

8. Limitamos a consulta para apenas 1 resultado.
