package med.voll.VollMedApi_03.domain.consulta.validacoes.cancelamento;

import med.voll.VollMedApi_03.domain.ValidacaoException;
import med.voll.VollMedApi_03.domain.consulta.ConsultaRepository;
import med.voll.VollMedApi_03.domain.consulta.DadosCancelamentoConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component("ValidadorHorarioAntecedenciaCancelamento")
// Como temos 2 classes com o mesmo nome, precisamos passar um nome único para a anotação "Component". Mesmo que no java seja permitido que 2 classes de pacotes diferentes tenham o mesmo nome, o Spring causa um erro.
public class ValidadorHorarioAntecedencia implements ValidadorCancelamentoDeConsulta {

  @Autowired
  private ConsultaRepository repository;

  @Override
  public void validar(DadosCancelamentoConsulta dados) {
    var consulta = repository.getReferenceById(dados.idConsulta());
    var agora = LocalDateTime.now();
    var diferencaEmHoras = Duration.between(agora, consulta.getData()).toHours();

    if (diferencaEmHoras < 24) {
      throw new ValidacaoException("Consulta somente pode ser cancelada com antecedência mínima de 24h!");
    }
  }
}
