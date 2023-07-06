package com.api.api_rest.domain.paciente;

import com.api.api_rest.domain.endereco.DadosEndereco;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizaPaciente(

    @NotNull Long id,
    String nome,
    String telefone,
    DadosEndereco endereco) {

}
