package com.api.api_rest.domain.consulta;

import jakarta.validation.constraints.NotNull;

public record DadosCancelamentoConsulta(
    @NotNull Long id,

    @NotNull MotivoCancelamento motivoDoCancelamento) {

}
