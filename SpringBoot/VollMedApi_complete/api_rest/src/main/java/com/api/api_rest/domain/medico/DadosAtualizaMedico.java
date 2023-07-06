package com.api.api_rest.domain.medico;

import com.api.api_rest.domain.endereco.DadosEndereco;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizaMedico(

    @NotNull Long id,
    String nome,
    String telefone,
    DadosEndereco endereco) {

}
