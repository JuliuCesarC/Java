package med.voll.VollMedApi_02.domain.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DadosEndereco(
    @NotBlank String logradouro,
    @NotBlank String bairro,
    @NotBlank @Pattern(regexp = "\\d{8}") String cep,
    @NotBlank String cidade,
    @NotBlank String uf,
    String complemento,
    String numero) {
}
// A classe Record facilita a criação de DTOs, pois ja possui implementação dos métodos getters e setters por baixo dos panos.
