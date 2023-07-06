package med.voll.VollMedApi_03.domain.consulta;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import med.voll.VollMedApi_03.domain.medico.Especialidade;

public record DadosAgendamentoConsulta(
    Long idMedico,

    @NotNull Long idPaciente,

    @NotNull @Future @JsonFormat(pattern = "dd/MM/yyyy HH:mm") @JsonAlias({"dataConsulta", "data_consulta" }) 
    LocalDateTime data,

    Especialidade especialidade
  ){
}
