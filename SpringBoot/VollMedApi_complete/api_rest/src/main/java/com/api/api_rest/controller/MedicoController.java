package com.api.api_rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.api.api_rest.domain.medico.DadosAtualizaMedico;
import com.api.api_rest.domain.medico.DadosCadastroMedico;
import com.api.api_rest.domain.medico.DadosDetalhamentoMedico;
import com.api.api_rest.domain.medico.DadosListagemMedico;
import com.api.api_rest.domain.medico.Medico;
import com.api.api_rest.domain.medico.MedicoRepository;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("medicos")
@SecurityRequirement(name = "bearer-key")
public class MedicoController {

  @Autowired
  private MedicoRepository repository;

  @PostMapping
  @Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
  @Transactional
  public ResponseEntity cadastrarMedico(@RequestBody @Valid DadosCadastroMedico dados,
      UriComponentsBuilder uriBuilder) {
    var medico = new Medico(dados);
    repository.save(medico);

    var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

    return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
  }

  @GetMapping
  public ResponseEntity<Page<DadosListagemMedico>> listarMedico(
      @PageableDefault(size = 10, sort = { "nome" }) Pageable paginacao) {
    var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
    return ResponseEntity.ok(page);
  }

  @PutMapping
  @Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
  @Transactional
  public ResponseEntity atualizarMedico(@RequestBody @Valid DadosAtualizaMedico dados) {
    var medico = repository.getReferenceById(dados.id());
    medico.atualizarInformacoesMedico(dados);

    return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
  }

  // Para criar uma sub rota dentro de uma rota, passamos como parâmetro a sub
  // rota desejada no Mapping.
  @DeleteMapping("/{id}")
  // Para definirmos uma parâmetro dinâmico na rota, utilizamos as chaves "{
  // nomeDoParâmetro }".
  @Secured("ROLE_ADMIN")
  @Transactional
  public ResponseEntity excluirMedico(@PathVariable Long id) {
    var medico = repository.getReferenceById(id);
    medico.desativarMedico();

    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}")
  @Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
  public ResponseEntity detalharMedico(@PathVariable Long id) {
    var medico = repository.getReferenceById(id);
    return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
  }
}