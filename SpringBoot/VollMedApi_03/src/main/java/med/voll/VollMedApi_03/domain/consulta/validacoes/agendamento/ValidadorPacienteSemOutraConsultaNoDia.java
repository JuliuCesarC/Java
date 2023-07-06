package med.voll.VollMedApi_03.domain.consulta.validacoes.agendamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import med.voll.VollMedApi_03.domain.ValidacaoException;
import med.voll.VollMedApi_03.domain.consulta.ConsultaRepository;
import med.voll.VollMedApi_03.domain.consulta.DadosAgendamentoConsulta;

@Component
public class ValidadorPacienteSemOutraConsultaNoDia implements ValidadorAgendamentoDeConsulta {

  @Autowired
  private ConsultaRepository repository;

  public void validar(DadosAgendamentoConsulta dados) {
    var primeiroHorario = dados.data().withHour(7);
    var ultimoHorario = dados.data().withHour(18);
    // Nas duas variáveis acima, selecionamos o primeiro e o ultimo horário de atendimento da clinica nessa data.
    
    var pacientePossuiOutraConsultaNoDia = repository.existsByPacienteIdAndDataBetween(dados.idPaciente(),
        primeiroHorario, ultimoHorario);
        // Nesta query, buscamos no banco de dados se o paciente não possui nenhuma outra consulta no dia. Podemos nessa ocasião também utilizar uma "Consulta Derivada", onde pesquisamos pelo id do paciente e entre os valores informados no parâmetro. Ao utilizar o "DataBetween" o JPA Repository entende que iremos informar os 2 valores como parâmetro.
    if (pacientePossuiOutraConsultaNoDia) {
      // Caso a query encontre alguma valor entre esses horários, a nova consulta sera bloqueada, pois o paciente não pode ter 2 consultas no mesmo dia.
      throw new ValidacaoException("Paciente já possui uma consulta agendada nesse dia");
    }
  }

}
