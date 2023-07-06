package com.api.api_rest.domain.consulta.validacoes.agendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.api_rest.domain.ValidacaoException;
import com.api.api_rest.domain.consulta.DadosAgendamentoConsulta;
import com.api.api_rest.domain.paciente.PacienteRepository;

@Component
public class ValidadorPacienteAtivo implements ValidadorAgendamentoDeConsulta {

  @Autowired
  private PacienteRepository repository;

  public void validar(DadosAgendamentoConsulta dados) {
    var pacienteEstaAtivo = repository.findAtivoById(dados.idPaciente());
    if (!pacienteEstaAtivo) {
      throw new ValidacaoException("Consulta não pode ser agendada com paciente excluído");
    }
  }
}
