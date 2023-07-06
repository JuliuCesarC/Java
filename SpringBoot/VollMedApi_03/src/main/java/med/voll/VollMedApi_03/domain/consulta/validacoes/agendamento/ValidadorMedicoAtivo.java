package med.voll.VollMedApi_03.domain.consulta.validacoes.agendamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import med.voll.VollMedApi_03.domain.ValidacaoException;
import med.voll.VollMedApi_03.domain.consulta.DadosAgendamentoConsulta;
import med.voll.VollMedApi_03.domain.medico.MedicoRepository;

@Component
public class ValidadorMedicoAtivo implements ValidadorAgendamentoDeConsulta {

  @Autowired
  private MedicoRepository repository;

  public void validar(DadosAgendamentoConsulta dados) {
    if (dados.idMedico() == null) {
      // Caso o médico não seja informado, apenas retornamos sem fazer a validação.
      return;
    }

    var medicoEstaAtivo = repository.findAtivoById(dados.idMedico());
    // Faz uma consulta no banco de dados no campo "ativo" do médico, e a variável recebe o valor desse campo.
    if (!medicoEstaAtivo) {
      // Caso o médico esteja desativado, bloqueia a consulta de ser agendada.
      throw new ValidacaoException("Consulta não pode ser agendada com médico excluído");
    }
  }

}
