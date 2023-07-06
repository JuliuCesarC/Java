package med.voll.VollMedApi_03.domain.consulta;

import jakarta.validation.constraints.NotNull;

public record DadosCancelamentoConsulta(
    @NotNull Long idConsulta,

    @NotNull MotivoCancelamento motivo) {
}
