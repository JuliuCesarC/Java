package com.api.api_rest.domain.consulta;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.api_rest.domain.ValidacaoException;
import com.api.api_rest.domain.consulta.validacoes.agendar.ValidadorAgendamentoDeConsulta;
import com.api.api_rest.domain.consulta.validacoes.cancelar.ValidadorCancelamentoDeConsulta;
import com.api.api_rest.domain.medico.Medico;
import com.api.api_rest.domain.medico.MedicoRepository;
import com.api.api_rest.domain.paciente.PacienteRepository;

import jakarta.validation.Valid;

@Service
public class AgendaDeConsultas {

  @Autowired
  private ConsultaRepository consultaRepository;

  @Autowired
  private MedicoRepository medicoRepository;

  @Autowired
  private PacienteRepository pacienteRepository;

  @Autowired
  private List<ValidadorAgendamentoDeConsulta> validadoresAgendar;

  @Autowired
  private List<ValidadorCancelamentoDeConsulta> validadoresCancelar;

  public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dados) {
    if (!pacienteRepository.existsById(dados.idPaciente())) {
      throw new ValidacaoException("Id do paciente não encontrado, ou paciente não cadastrado.");
    }
    if (dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())) {
      throw new ValidacaoException("Id do medico não encontrado.");
    }

    validadoresAgendar.forEach(v -> v.validar(dados));

    var paciente = pacienteRepository.findById(dados.idPaciente()).get();
    var medico = escolherMedico(dados);
    if (medico == null) {
      throw new ValidacaoException("Não existe médico disponível neste horário");
    }
    var consulta = new Consulta(null, medico, paciente, dados.data(), null);

    consultaRepository.save(consulta);
    return new DadosDetalhamentoConsulta(consulta);
  }

  public void cancelar(@Valid DadosCancelamentoConsulta dados) {
    if (!consultaRepository.existsById(dados.id())) {
      throw new ValidacaoException("Id da consulta não encontrado.");
    }

    validadoresCancelar.forEach(v -> v.validar(dados));

    var consulta = consultaRepository.getReferenceById(dados.id());
    consulta.cancelar(dados.motivoDoCancelamento());
  }

  private Medico escolherMedico(DadosAgendamentoConsulta dados) {
    if (dados.idMedico() != null) {
      return medicoRepository.getReferenceById(dados.idMedico());
    }

    if (dados.especialidade() == null) {
      throw new ValidacaoException("Especialidade é obrigatória no caso do médico não ser escolhido.");
    }

    return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());
  }
}
