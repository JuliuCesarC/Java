package com.api.api_rest.domain.consulta.validacoes.agendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.api_rest.domain.ValidacaoException;
import com.api.api_rest.domain.consulta.ConsultaRepository;
import com.api.api_rest.domain.consulta.DadosAgendamentoConsulta;

@Component
public class ValidadorPacienteSemOutraConsultaNoDia implements ValidadorAgendamentoDeConsulta {

  @Autowired
  private ConsultaRepository repository;

  public void validar(DadosAgendamentoConsulta dados) {
    var primeiroHorario = dados.data().withHour(7);
    var ultimoHorario = dados.data().withHour(18);
    var pacientePossuiOutraConsultaNoDia = repository.existsByPacienteIdAndDataBetween(dados.idPaciente(),
        primeiroHorario, ultimoHorario);
    if (pacientePossuiOutraConsultaNoDia) {
      throw new ValidacaoException("Paciente já possui uma consulta agendada nesse dia");
    }
  }

}
