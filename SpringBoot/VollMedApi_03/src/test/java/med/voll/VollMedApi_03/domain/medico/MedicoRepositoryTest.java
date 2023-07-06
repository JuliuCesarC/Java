package med.voll.VollMedApi_03.domain.medico;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import med.voll.VollMedApi_03.domain.consulta.Consulta;
import med.voll.VollMedApi_03.domain.consulta.MotivoCancelamento;
import med.voll.VollMedApi_03.domain.endereco.DadosEndereco;
import med.voll.VollMedApi_03.domain.paciente.DadosCadastroPaciente;
import med.voll.VollMedApi_03.domain.paciente.Paciente;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class MedicoRepositoryTest {

  @Autowired
  private MedicoRepository medicoRepository;

  @Autowired
  private TestEntityManager entityManager;

  @Test
  @DisplayName("Deveria devolver null quando unico medico cadastrado nao esta disponivel na data")
  void escolherMedicoAleatorioLivreNaDataCenario1() {
    // given ou arrange : dada essa situação ...
    var especialidade = Especialidade.CARDIOLOGIA;
    var proximaSegundaAs10 = LocalDate.now()
        .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
        .atTime(10, 0);
    var medico = cadastrarMedico("Medico", "medico@voll.med", "123456", especialidade);
    var paciente = cadastrarPaciente("Paciente", "paciente@email.com", "00000000000");
    cadastrarConsulta(medico, paciente, proximaSegundaAs10);

    // when ou act : quando executado isso ...
    var medicoLivre = medicoRepository.escolherMedicoAleatorioLivreNaData(especialidade,
        proximaSegundaAs10);

    // then ou assert : esperamos isso ...
    assertThat(medicoLivre).isNull();
  }

  @Test
  @DisplayName("Deveria devolver medico quando ele estiver disponível na data")
  void escolherMedicoAleatorioLivreNaDataCenario2() {
    // given ou arrange
    var especialidade = Especialidade.CARDIOLOGIA;
    var proximaSegundaAs10 = LocalDate.now()
        .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
        .atTime(10, 0);
    var medico = cadastrarMedico("Medico", "medico@voll.med", "123456", especialidade);

    // when ou act
    var medicoLivre = medicoRepository.escolherMedicoAleatorioLivreNaData(especialidade,
        proximaSegundaAs10);

    // then ou assert
    assertThat(medicoLivre).isEqualTo(medico);
  }

  @Test
  @DisplayName("Deveria devolver null quando nenhum medico de mesma especialidade estiver cadastrado")
  void escolherMedicoAleatorioLivreNaDataCenario3() {
    // given ou arrange : dada essa situação ...
    var proximaSegundaAs10 = LocalDate.now()
        .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
        .atTime(10, 0);
    cadastrarMedico("Medico", "medico@voll.med", "123456", Especialidade.CARDIOLOGIA);

    // when ou act : quando executado isso ...
    var medicoLivre = medicoRepository.escolherMedicoAleatorioLivreNaData(Especialidade.ORTOPEDIA,
        proximaSegundaAs10);

    // then ou assert : esperamos isso ...
    assertThat(medicoLivre).isNull();
  }

  @Test
  @DisplayName("Deveria devolver o médico caso a consulta anterior tenha sido cancelada")
  void escolherMedicoAleatorioLivreNaDataCenario4() {
    // given ou arrange : dada essa situação ...
    var especialidade = Especialidade.CARDIOLOGIA;
    var proximaSegundaAs10 = LocalDate.now()
        .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
        .atTime(10, 0);
    var medico = cadastrarMedico("Medico", "medico@voll.med", "123456", especialidade);
    var paciente = cadastrarPaciente("Paciente", "paciente@email.com", "00000000000");
    cadastrarConsultaCancelada(medico, paciente, proximaSegundaAs10);

    // when ou act : quando executado isso ...
    var medicoLivre = medicoRepository.escolherMedicoAleatorioLivreNaData(especialidade,
        proximaSegundaAs10);

    // then ou assert : esperamos isso ...
    assertThat(medicoLivre).isEqualTo(medico);
  }

  private Medico cadastrarMedico(String nome, String email, String crm, Especialidade especialidade) {
    var medico = new Medico(dadosMedico(nome, email, crm, especialidade));
    entityManager.persist(medico);
    return medico;
  }

  private Paciente cadastrarPaciente(String nome, String email, String cpf) {
    var paciente = new Paciente(dadosPaciente(nome, email, cpf));
    entityManager.persist(paciente);
    return paciente;
  }

  private void cadastrarConsulta(Medico medico, Paciente paciente, LocalDateTime data) {
    entityManager.persist(new Consulta(null, medico, paciente, data, null));
  }

  private void cadastrarConsultaCancelada(Medico medico, Paciente paciente, LocalDateTime data) {
    entityManager.persist(new Consulta(null, medico, paciente, data, MotivoCancelamento.PACIENTE_DESISTIU));
  }

  private DadosCadastroMedico dadosMedico(String nome, String email, String crm, Especialidade especialidade) {
    return new DadosCadastroMedico(
        nome,
        email,
        "61999999999",
        crm,
        especialidade,
        dadosEndereco());
  }

  private DadosCadastroPaciente dadosPaciente(String nome, String email, String cpf) {
    return new DadosCadastroPaciente(
        nome,
        email,
        "61999999999",
        cpf,
        dadosEndereco());
  }

  private DadosEndereco dadosEndereco() {
    return new DadosEndereco(
        "rua xpto",
        "bairro",
        "00000000",
        "Brasilia",
        "DF",
        null,
        null);
  }
}
