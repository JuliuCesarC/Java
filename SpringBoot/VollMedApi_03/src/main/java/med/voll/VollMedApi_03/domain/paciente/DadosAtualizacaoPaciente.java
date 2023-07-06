package med.voll.VollMedApi_03.domain.paciente;

import jakarta.validation.constraints.NotNull;
import med.voll.VollMedApi_03.domain.endereco.DadosEndereco;

public record DadosAtualizacaoPaciente(
        @NotNull
        Long id,
        String nome,
        String telefone,
        DadosEndereco endereco) {
}
