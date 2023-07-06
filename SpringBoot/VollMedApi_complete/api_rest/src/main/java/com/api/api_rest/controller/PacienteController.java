package com.api.api_rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.api.api_rest.domain.paciente.DadosAtualizaPaciente;
import com.api.api_rest.domain.paciente.DadosCadastroPaciente;
import com.api.api_rest.domain.paciente.DadosDetalhamentoPaciente;
import com.api.api_rest.domain.paciente.DadosListagemPaciente;
import com.api.api_rest.domain.paciente.Paciente;
import com.api.api_rest.domain.paciente.PacienteRepository;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("pacientes")
@SecurityRequirement(name = "bearer-key")
public class PacienteController {

  @Autowired
  private PacienteRepository repository;

  @PostMapping
  @Transactional
  public ResponseEntity cadastrarPaciente(@RequestBody @Valid DadosCadastroPaciente dados,
      UriComponentsBuilder uriBuilder) {
    var paciente = new Paciente(dados);
    repository.save(paciente);

    var uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();

    return ResponseEntity.created(uri).body(new DadosDetalhamentoPaciente(paciente));
  }

  @GetMapping
  public ResponseEntity<Page<DadosListagemPaciente>> listarPaciente(
      @PageableDefault(size = 10, sort = { "nome" }) Pageable paginacao) {
    var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemPaciente::new);
    return ResponseEntity.ok(page);
  }

  @PutMapping
  @Transactional
  public ResponseEntity atualizaPaciente(@RequestBody @Valid DadosAtualizaPaciente dados) {
    var paciente = repository.getReferenceById(dados.id());
    paciente.atualizarInformacoesPaciente(dados);

    return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
  }

  @DeleteMapping("/{id}")
  @Transactional
  public ResponseEntity excluirPaciente(@PathVariable Long id) {
    var paciente = repository.getReferenceById(id);
    paciente.desativarPaciente();

    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}")
  public ResponseEntity detalharPaciente(@PathVariable Long id) {
    var paciente = repository.getReferenceById(id);
    return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
  }
}
