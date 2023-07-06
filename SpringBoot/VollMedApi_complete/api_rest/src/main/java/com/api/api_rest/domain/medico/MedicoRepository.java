package com.api.api_rest.domain.medico;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

// O primeiro tipo no JpaRepository é a classe que ele vai trabalhar, e o segundo é o tipo da chave primaria dessa entidade, no caso para a classe Médico o id foi setado como "Long".
public interface MedicoRepository extends JpaRepository<Medico, Long> {

  Page<Medico> findAllByAtivoTrue(Pageable paginacao);

  @Query("""
          select m from Medico m
          where
          m.ativo = true
          and
          m.especialidade = :especialidade
          and
          m.id not in(
              select c.medico.id from Consulta c
              where
              c.data = :data
              and
              c.motivoCancelamento is null
          )
          order by rand()
          limit 1
      """)
  Medico escolherMedicoAleatorioLivreNaData(Especialidade especialidade, LocalDateTime data);

  @Query("""
      SELECT m.ativo FROM Medico m
      WHERE
        m.id = :id
      """)
  boolean findAtivoById(Long id);
}
