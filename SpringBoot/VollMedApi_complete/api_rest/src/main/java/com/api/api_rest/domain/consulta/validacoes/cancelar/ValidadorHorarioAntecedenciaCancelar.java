package com.api.api_rest.domain.consulta.validacoes.cancelar;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.api_rest.domain.ValidacaoException;
import com.api.api_rest.domain.consulta.ConsultaRepository;
import com.api.api_rest.domain.consulta.DadosCancelamentoConsulta;

@Component
public class ValidadorHorarioAntecedenciaCancelar implements ValidadorCancelamentoDeConsulta {

  @Autowired
  private ConsultaRepository consultaRepository;

  public void validar(DadosCancelamentoConsulta dados) {
    var consulta = consultaRepository.getReferenceById(dados.id());
    var dataConsulta = consulta.getData();
    var agora = LocalDateTime.now();
    var diferencaEmHoras = Duration.between(agora, dataConsulta).toHours();
    if (diferencaEmHoras < 24) {
      throw new ValidacaoException(
          "Cancelamento de consulta deve ser feito com no mínimo 24 horas de antecedência.");
    }
  }

}
