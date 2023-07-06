package med.voll.VollMedApi_02.domain.medico;

import jakarta.validation.constraints.NotNull;
import med.voll.VollMedApi_02.domain.endereco.DadosEndereco;

public record DadosAtualizacaoMedico(
    @NotNull Long id,
    String nome,
    String telefone,
    DadosEndereco endereco) {
}
