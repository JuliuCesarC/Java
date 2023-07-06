package com.api.api_rest.domain.consulta.validacoes.cancelar;

import com.api.api_rest.domain.consulta.DadosCancelamentoConsulta;

public interface ValidadorCancelamentoDeConsulta {

  void validar(DadosCancelamentoConsulta dados);
  
}
