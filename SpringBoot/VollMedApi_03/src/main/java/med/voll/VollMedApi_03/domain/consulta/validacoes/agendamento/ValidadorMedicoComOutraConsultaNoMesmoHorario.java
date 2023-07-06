package med.voll.VollMedApi_03.domain.consulta.validacoes.agendamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import med.voll.VollMedApi_03.domain.ValidacaoException;
import med.voll.VollMedApi_03.domain.consulta.ConsultaRepository;
import med.voll.VollMedApi_03.domain.consulta.DadosAgendamentoConsulta;

@Component
public class ValidadorMedicoComOutraConsultaNoMesmoHorario implements ValidadorAgendamentoDeConsulta {

  @Autowired
  private ConsultaRepository repository;

  public void validar(DadosAgendamentoConsulta dados) {
    var medicoPossuiOutraConsultaNoMesmoHorario = repository.existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(dados.idMedico(), dados.data());
    // Fazemos uma consulta no banco de dados para verificar se o médico ja possui uma consulta nessa data, e caso ele tenha uma consulta mas que foi cancelada, então poderá ser escolhido de novo. Apesar de parecer uma consulta um pouco mais complexa, podemos utilizar uma "Consulta Derivada" para para executar essa tarefa. Lembrando que a ordem dos parâmetros é importante, o "ByMedicoId" vem primeiro, logo o parâmetro com o id dele deve também ser o primeiro.
    if (medicoPossuiOutraConsultaNoMesmoHorario) {
      // Caso a query acima encontre algum item, logo o médico ja possui uma consulta neste horário então bloqueamos a aplicação jogando uma exceção.
      throw new ValidacaoException("Médico já possui outra consulta agendada nesse mesmo horário");
    }
  }

}
