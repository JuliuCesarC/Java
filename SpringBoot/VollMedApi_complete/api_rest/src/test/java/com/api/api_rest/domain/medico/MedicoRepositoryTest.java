package com.api.api_rest.domain.medico;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.api.api_rest.domain.consulta.Consulta;
import com.api.api_rest.domain.endereco.DadosEndereco;
import com.api.api_rest.domain.paciente.DadosCadastroPaciente;
import com.api.api_rest.domain.paciente.Paciente;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
class MedicoRepositoryTest {

  @Autowired
  private MedicoRepository medicoRepository;

  @Autowired
  private TestEntityManager tem;

  @Test
  @DisplayName("Deveria devolver null quando único medico cadastrado nao esta disponível na data.")
  void escolherMedicoAleatorioLivreNaDataCenario1() {
    // given or arrange
    var proximaSegundaFeiraAs10 = LocalDate.now()
        .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
        .atTime(10, 0);

    var medico = cadastrarMedico("medicoTeste", "medico@tes.te", "654789", Especialidade.ORTOPEDIA);
    var paciente = cadastrarPaciente("pacienteTeste", "paciente@tes.te", "11111111111");
    cadastrarConsulta(medico, paciente, proximaSegundaFeiraAs10);

    // when or act
    var medicoLivre = medicoRepository.escolherMedicoAleatorioLivreNaData(Especialidade.ORTOPEDIA,
        proximaSegundaFeiraAs10);
    // then or assert
    assertThat(medicoLivre).isNull();
  }

  @Test
  @DisplayName("Deveria devolver Medico quando ele estiver disponível na data.")
  void escolherMedicoAleatorioLivreNaDataCenario2() {

    var proximaSegundaFeiraAs10 = LocalDate.now()
        .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
        .atTime(10, 0);

    var medico = cadastrarMedico("medicoTeste", "medico@tes.te", "654789", Especialidade.CARDIOLOGIA);

    var medicoLivre = medicoRepository.escolherMedicoAleatorioLivreNaData(Especialidade.CARDIOLOGIA,
        proximaSegundaFeiraAs10);
    assertThat(medicoLivre).isEqualTo(medico);
  }

  private void cadastrarConsulta(Medico medico, Paciente paciente, LocalDateTime data) {
    tem.persist(new Consulta(null, medico, paciente, data, null));
  }

  private Medico cadastrarMedico(String nome, String email, String crm, Especialidade especialidade) {
    var medico = new Medico(dadosMedico(nome, email, crm, especialidade));
    tem.persist(medico);
    return medico;
  }

  private Paciente cadastrarPaciente(String nome, String email, String cpf) {
    var paciente = new Paciente(dadosPaciente(nome, email, cpf));
    tem.persist(paciente);
    return paciente;
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
