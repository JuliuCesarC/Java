package com.api.api_rest.domain.consulta;

import java.time.LocalDateTime;

import com.api.api_rest.domain.medico.Especialidade;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record DadosAgendamentoConsulta(
    Long idMedico,

    @NotNull Long idPaciente,

    @NotNull @Future @JsonFormat(pattern = "dd/MM/yyyy HH:mm") @JsonAlias({"dataConsulta", "data_consulta" }) 
    LocalDateTime data,

    Especialidade especialidade

  ){
}
