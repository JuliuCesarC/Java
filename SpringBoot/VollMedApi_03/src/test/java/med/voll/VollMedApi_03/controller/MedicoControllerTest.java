package med.voll.VollMedApi_03.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import med.voll.VollMedApi_03.domain.endereco.DadosEndereco;
import med.voll.VollMedApi_03.domain.endereco.Endereco;
import med.voll.VollMedApi_03.domain.medico.DadosCadastroMedico;
import med.voll.VollMedApi_03.domain.medico.DadosDetalhamentoMedico;
import med.voll.VollMedApi_03.domain.medico.Especialidade;
import med.voll.VollMedApi_03.domain.medico.MedicoRepository;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class MedicoControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private JacksonTester<DadosCadastroMedico> dadosCadastroMedicoJson;

  @Autowired
  private JacksonTester<DadosDetalhamentoMedico> dadosDetalhamentoMedicoJson;

  @MockBean
  private MedicoRepository repository;

  @Test
  @DisplayName("Deveria devolver código http 400 quando informações estão invalidas")
  @WithMockUser
  void testCadastrarMedico_cenario1() throws Exception {
    var response = mvc.perform(post("/medicos")).andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  @DisplayName("Deveria devolver código http 200 quando informações estão validas")
  @WithMockUser(roles = { "ADMIN" })
  void testCadastrarMedico_cenario2() throws Exception {
    var dadosEndereco = new DadosEndereco(
        "qqqqqqq",
        "qqqqq",
        "88888888",
        "qqqq",
        "SP",
        null,
        null);
    var dadosCadastro = new DadosCadastroMedico(
        "fulano",
        "fulano@gmail.com",
        "999999999",
        "555555",
        Especialidade.CARDIOLOGIA,
        dadosEndereco);
    var dadosDetalhamento = new DadosDetalhamentoMedico(
        null,
        dadosCadastro.nome(),
        dadosCadastro.email(),
        dadosCadastro.crm(),
        dadosCadastro.telefone(),
        dadosCadastro.especialidade(),
        new Endereco(dadosCadastro.endereco()));

    var response = mvc.perform(post("/medicos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(dadosCadastroMedicoJson.write(dadosCadastro).getJson())).andReturn().getResponse();

    var jsonEsperado = dadosDetalhamentoMedicoJson.write(dadosDetalhamento).getJson();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
  }
}
