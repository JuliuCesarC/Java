package com.api.api_rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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

import com.api.api_rest.domain.endereco.DadosEndereco;
import com.api.api_rest.domain.endereco.Endereco;
import com.api.api_rest.domain.medico.DadosCadastroMedico;
import com.api.api_rest.domain.medico.DadosDetalhamentoMedico;
import com.api.api_rest.domain.medico.Especialidade;
import com.api.api_rest.domain.medico.Medico;
import com.api.api_rest.domain.medico.MedicoRepository;

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
  private MedicoRepository medicoRepository;

  @Test
  @DisplayName("Deveria devolver código http 400 quando informações estão invalidas")
  @WithMockUser(username="admin",roles={"ADMIN"})
  void CadastrarMedico_cenario1() throws Exception {
    var response = mvc.perform(post("/medicos")).andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  @DisplayName("Deveria devolver código http 200 quando informações estão validas")
  @WithMockUser(username="admin",roles={"ADMIN"})
  void CadastrarMedico_cenario2() throws Exception {
    var dadosCadastro = new DadosCadastroMedico(
        "Medico Teste",
        "medico@tes.te",
        "64999999999",
        "654987",
        Especialidade.NEUROLOGIA,
        criaEndereco());

    when(medicoRepository.save(any())).thenReturn(new Medico(dadosCadastro));

    var response = mvc.perform(
        post("/medicos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(dadosCadastroMedicoJson.write(dadosCadastro).getJson()))
        .andReturn().getResponse();

    var dadosDetalhamento = new DadosDetalhamentoMedico(
        null,
        dadosCadastro.nome(),
        dadosCadastro.email(),
        dadosCadastro.crm(),
        dadosCadastro.telefone(),
        dadosCadastro.especialidade(),
        new Endereco(dadosCadastro.endereco()));

    var jsonEsperado = dadosDetalhamentoMedicoJson.write(dadosDetalhamento).getJson();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
  }

  private DadosEndereco criaEndereco() {
    return new DadosEndereco(
        "teste",
        "Teste",
        "88888888",
        "88",
        "teste",
        "teste",
        "SP");
  }
}
