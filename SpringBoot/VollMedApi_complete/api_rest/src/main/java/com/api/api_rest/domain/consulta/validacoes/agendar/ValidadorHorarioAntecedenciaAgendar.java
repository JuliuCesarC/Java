package com.api.api_rest.domain.consulta.validacoes.agendar;

import org.springframework.stereotype.Component;

import com.api.api_rest.domain.ValidacaoException;
import com.api.api_rest.domain.consulta.DadosAgendamentoConsulta;

import java.time.Duration;
import java.time.LocalDateTime;

@Component("ValidadorHorarioAntecedenciaAgendamento")
public class ValidadorHorarioAntecedenciaAgendar implements ValidadorAgendamentoDeConsulta {

  public void validar(DadosAgendamentoConsulta dados) {
    var dataConsulta = dados.data();
    var agora = LocalDateTime.now();
    var diferencaEmMinutos = Duration.between(agora, dataConsulta).toMinutes();

    if (diferencaEmMinutos < 30) {
      throw new ValidacaoException("Consulta deve ser agendada com antecedência mínima de 30 minutos");
    }

  }
}
