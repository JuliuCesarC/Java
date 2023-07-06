# Explicando detalhadamente o cenário de teste 2 do controller agendar

Neste arquivo vamos detalhar o cenário de teste 2 da classe `ConsultaController`. Vamos detalhar linha por linha e salientando as partes importantes.

```java
@Test
@DisplayName("Deveria devolver codigo http 200 quando informacoes estao validas")
@WithMockUser
void agendar_cenario2() throws Exception {
  var data = LocalDateTime.now().plusHours(1);
  var dadosAgendamento = new DadosAgendamentoConsulta(2l, 5l, data, Especialidade.CARDIOLOGIA);
  var dadosDetalhamento = new DadosDetalhamentoConsulta(null, 2l, 5l, data);

  when(agendaDeConsultas.agendar(any())).thenReturn(dadosDetalhamento);
  
  var response = mvc.perform(post("/consultas")
          .contentType(MediaType.APPLICATION_JSON)
          .content(dadosAgendamentoConsultaJson.write(dadosAgendamento).getJson()))
      .andReturn().getResponse();

  assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
  var jsonEsperado = dadosDetalhamentoConsultaJson.write(dadosDetalhamento).getJson();
  assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
}
```

1. `data` : como estamos testando apenas o controller sem passar pelas validações, não é necessario criar uma data padrão. Este cenário não deve testar o horário de agendamento.

2. DTO agendamento : variável com o dto de `DadosAgendamentoConsulta` que sera utilizado para criar o json enviado na requisição.

3. DTO detalhamento : variável com o dto de `DadosDetalhamentoConsulta` que sera utilizado para criar o json para a segunda assertiva.

4. Utilizamos o método estático `when` da biblioteca **Mockito** para simular o funcionamento da classe `agendaDeConsultas`, isso significa que quando ela for chamada o seu comportamento padrão sera o que configuramos. Dentro do *when* chamamos o método `agendar` da propriedade *agendaDeConsultas*, e informamos que independente de quais parâmetros receba `any()`, ele devera retornar `thenReturn` o dto de detalhamento de consulta `dadosDetalhamento`.

5. Agora performamos a requisição do tipo POST para o controller com o `mvc.perform` e salvamos o seu retorno em uma variável, muito semelhante ao que foi feito no cenário 1.

6. Em seguida configuramos o cabeçalho da requisição para informar que sera enviado um json, e fazemos isso com o `contentType` passando o *MediaType* como `APPLICATION_JSON`. Dessa forma informamos para o servidor que receber a requisição que estamos enviando informações no formato JSON.

7. Para enviar as informações de *dadosAgendamento* no corpo da requisição vamos utilizar o `content`, que recebe como parâmetro as informações no formato json, mas não vamos digitar esse json manualmente. O que faremos é utilizar a biblioteca **JacksonTester** para facilitar esse processo. Dentro da classe foi declarado a propriedade `dadosDetalhamentoConsultaJson` sendo do tipo *JacksonTester*, e com ela temos o método `write` que escreve o json através de um dto recebido como parâmetro, logo passamos o `dadosAgendamento`. Lembrando que é preciso encadear o `getJson()` apos o *write*.

8. Por fim utilizamos o `.andReturn()` para pegar o retorno e o `.getResponse()` para pegar a resposta.

9. Na primeira assertiva verificamos se o status da resposta (`response.getStatus()`) é igual ao status 200 (`HttpStatus.OK.value()`).

10. `jsonEsperado` : salvamos nessa variável o json de `DadosDetalhamentoConsulta` que vamos utilizar na assertiva a seguir.

11. Na segunda assertiva verificamos se o JSON retornado pelo controller é igual ao que criamos na variável `jsonEsperado`. Apesar de ambos os json terem sidos criados manualmente, eles contem as mesmas informações, logo o controller precisa retornar essas informações no json de resposta.
