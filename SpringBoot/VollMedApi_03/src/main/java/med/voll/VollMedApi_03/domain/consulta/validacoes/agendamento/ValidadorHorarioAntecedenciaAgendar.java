package med.voll.VollMedApi_03.domain.consulta.validacoes.agendamento;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import med.voll.VollMedApi_03.domain.ValidacaoException;
import med.voll.VollMedApi_03.domain.consulta.DadosAgendamentoConsulta;

@Component("ValidadorHorarioAntecedenciaAgendamento")
// Como temos 2 classes com o mesmo nome, precisamos passar um nome único para a anotação "Component". Mesmo que no java seja permitido que 2 classes de pacotes diferentes tenham o mesmo nome, o Spring causa um erro.
public class ValidadorHorarioAntecedenciaAgendar implements ValidadorAgendamentoDeConsulta {

  public void validar(DadosAgendamentoConsulta dados) {
    var dataConsulta = dados.data();
    var agora = LocalDateTime.now();
    var diferencaEmMinutos = Duration.between(agora, dataConsulta).toMinutes();
    // Cria uma condicional que verifica a diferença em minutos da data informada para a data atual

    if (diferencaEmMinutos < 30) {
      // Caso tenhamos menos de 30 minutos de antecedência, a consulta não pode ser agendada.
      throw new ValidacaoException("Consulta deve ser agendada com antecedência mínima de 30 minutos");
    }

  }
}
