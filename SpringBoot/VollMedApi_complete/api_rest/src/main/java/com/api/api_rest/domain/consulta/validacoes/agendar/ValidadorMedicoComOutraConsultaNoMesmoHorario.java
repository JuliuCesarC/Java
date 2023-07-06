package com.api.api_rest.domain.consulta.validacoes.agendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.api_rest.domain.ValidacaoException;
import com.api.api_rest.domain.consulta.ConsultaRepository;
import com.api.api_rest.domain.consulta.DadosAgendamentoConsulta;

@Component
public class ValidadorMedicoComOutraConsultaNoMesmoHorario implements ValidadorAgendamentoDeConsulta {

  @Autowired
  private ConsultaRepository repository;

  public void validar(DadosAgendamentoConsulta dados) {
    var medicoPossuiOutraConsultaNoMesmoHorario = repository.existsByMedicoIdAndData(dados.idMedico(), dados.data());
    if (medicoPossuiOutraConsultaNoMesmoHorario) {
      throw new ValidacaoException("Médico já possui outra consulta agendada nesse mesmo horário");
    }
  }

}
