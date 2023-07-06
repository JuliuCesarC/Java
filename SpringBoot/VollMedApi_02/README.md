# Curso 2 projeto de api Voll Med

Esta formação busca ensinar desde a criação de um projeto com Spring Boot, passando por segurança, testes automatizados e por fim o build. A ideia é criar uma api para a empresa fictícia Voll Med, que é uma clinica que precisa de um sistema para gerenciar as consultas.

Para o curso 2 já começaremos com o projeto a partir do curso anterior, e também com a entidade *Paciente* pronta.

## Configurando os retornos da API

No curso anterior focamos principalmente em construir o básico dos métodos da api, sem se preocupar com o tipo de resposta, e no momento todos estão retornando o código http 200 e nenhum corpo na resposta caso o método seja processado corretamente (com exceção do método *listar* que obviamente retorna uma lista de medicos ou pacientes). O tratamento das respostas da api sera o primeiro aspecto que iremos abordar.

A classe que utilizaremos para essa tarefa sera a **ResponseEntity**. Ela é disponibilizada pelo Spring e é usada para encapsular a resposta enviada ao cliente, sendo uma classe consideravelmente genérica. Porem possui diversos construtores e métodos estáticos que nos permitem criar respostas especificas para cada método, contendo por exemplo cabeçalho, corpo, status, entre outros.

### Excluir médico

Começaremos com o método mais simples. Na declaração do método excluir iremos substituir o *void* pela classe *ResponseEntity*, e como retorno a resposta `noContent` código 204 que indica requisição processada com sucesso e nenhum conteúdo como resposta.

```java
public ResponseEntity excluirMedico(@PathVariable Long id) {
  var medico = repository.getReferenceById(id);
  medico.excluir();
  return ResponseEntity.noContent().build();
}
```

> O método noContent não constrói a resposta, por isso é necessario o `build`.

### Listar médico

No método de *listar* temos uma situação um pouco diferente, pois ele retorna um objeto do tipo `Page<DTO>`, mas como o ResponseEntity também pode receber um *generic*, basta envolver o *Page* com a classe `ResponseEntity<Page<DTO>>`.

```java
public ResponseEntity<Page<DadosListagemMedico>> listarMedico(
  @PageableDefault(size = 10, sort = { "nome" }) Pageable paginacao) {
  var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
  return ResponseEntity.ok(page);
}
```

Resposta `ok()` código 200. Como parâmetro passamos o objeto *page* que contem a resposta da listagem.

### Atualizar médico

Para o método de atualizar é interessando retornar as informações do médico já com os dados atualizados. Como vamos retornar informações do médico, precisamos criar um DTO de detalhamento das informações do médico. Resposta `ok()` código 200, e como parâmetro passamos o DTO.

```java
public ResponseEntity atualizarMedico(@RequestBody @Valid DadosAtualizacaoMedico dados) {
  var medico = repository.getReferenceById(dados.id());
  medico.atualizarInformacoes(dados);
  return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
}
```

Esse DTO já sera criado com o construtor para receber como parâmetro a entidade de médico, com isso funcionando diretamente com os dados ou com a entidade.

```java
public record DadosDetalhamentoMedico(Long id, String nome, String email, String crm, String telefone,
    Especialidade especialidade, Endereco endereco) {
  public DadosDetalhamentoMedico(Medico medico) {
    this(medico.getId(), medico.getNome(), medico.getEmail(), medico.getCrm(), medico.getTelefone(),
        medico.getEspecialidade(), medico.getEndereco());
  }
}
```

### Cadastrar médico

O código correto para este método seria o http 201 CREATED, que indica que um registro foi criado na api. Porem ele possui algumas regras a serem seguidas, sendo:

- Retornar o código 201

- Retornar no corpo da resposta os dados que acabaram de ser cadastrados

- Retornar o cabeçalho (header) *Location* com a URI

> Uma URI é uma url especifica para identificar um recurso recém cadastrado.

Este método é um pouco mais complexo que os demais, então vamos seguir os itens acima. O primeiro e o terceiro item é razoavelmente simples, bastando apenas utilizarmos o método `created()` passando como parâmetro a URI (logo abaixo temos um tópico especifico de como construir a URI). O segundo é a resposta com os dados do médico recém cadastrado, onde iremos encadear o `body()` no método anterior e passando como parâmetro um DTO, que pode ser o mesmo do método atualizar médico.

```java
public ResponseEntity cadastrarMedico(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder) {
  var medico = new Medico(dados);
  repository.save(medico);
  var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

  return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
}
```

Como tanto no DTO como na URI vai precisar da entidade *Medico*, separamos ela em uma variável.

#### URI builder

A URI como vista na lista de requisitos é o endereço para acessar o registro recém criado, e o Spring já possui uma classe para construir essa url, que é a `UriComponentsBuilder`. Essa classe é interessante por que sempre ira construir a URI com a url do servidor atual. Para utilizar essa classe é preciso fazer sua assinatura como parâmetro do método cadastrar, da forma como foi feita no trecho de código anterior.

O *uriBuilder* ira conter a url atual do servidor, porem é preciso fazer ele apontar para o endpoint de listar um médico detalhadamente. Este método ainda não foi adicionado no controller, mas iremos fazer isso no tópico seguinte. Para adicionar um caminho na url utilizaremos o `path()`, passando como parâmetro o caminho desejado. Esse caminho deve apontar para o controller atual `/medicos` e também para a rota de detalhar médico, onde é informado o id do médico assim como no método *excluir* `/medicos/idDoMedico`.

```java
var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
```

Dentro de *path* além do caminho */medicos* , passamos um segundo caminho como um parâmetro dinâmico `{id}`, pois o id do médico sempre sera diferente. Em seguida temos o método `buildAndExpand()` para inserir no parâmetro dinâmico a informação do id `medico.getId()`. Por fim temos o `toUri()` que como o nome sugere transforma o objeto em uma URI.

#### Método listar médico detalhado

Agora temos que criar o método para listar o médico detalhadamente. Ele precisar ter uma sub rota assim como o endpoint *excluir* e mesmo que os 2 tenham a mesma rota, o verbo dos 2 sera diferente. O DTO da resposta sera o mesmo do método *atualizar*.

```java
@GetMapping("/{id}")
public ResponseEntity detalharMedico(@PathVariable Long id) {
  var medico = repository.getReferenceById(id);
  return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
}
```

Como resposta temos o código 200 `ok()` e passamos o DTO como parâmetro.

## Estrutura de pacotes da aplicação

Antes de seguirmos para a proxima tarefa, vamos alterar os pacotes da api. As pastas *medico*, *paciente* e *endereco* são do domínio da aplicação, logo eles pertenceram ao pacote `domain`. Além desse, adicionaremos o pacote `infra`, que será responsável pela infraestrutura do projeto, como segurança, exceções, entre outros. Com isso o projeto fica com o seguintes 3 pacotes na pasta raiz do projeto, `controller`, `domain` e `infra`.

## Tratando possíveis erros na API

O Spring por padrão ja envia algumas informações para o usuário caso ocorra algum erro, porem ela trata qualquer problema como uma *exception* e devolve o código de erro 500. Porem o ideal é devolver o código mais especifico para o tipo de erro. Outra informação que o Spring também envia é o *stack trace* do erro, e além de não ser muito legível ela contem diversas informações da api, o que pode até se configurar como uma brecha de segurança. Podemos desabilitar a configuração do stack trace no arquivo `application.properties`.

```properties
server.error.include-stacktrace=never
```

Para saber mais sobre as configurações que podemos utilizar no *properties*, basta consultar a documentação [Common Application Properties](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html).

### Classe responsável pelas exceções

Uma opção para tratar o erro seria criar um try catch no método desejado e enviar a resposta adequada, porem isso cria uma problema onde sera necessario adicionar o bloco de código em todos os métodos, e além disso podemos ter outros controllers com o mesmo tipo de erro. Para resolver isso criaremos uma classe responsável por capturar e tratar cada tipo de exceção e enviar a resposta correta para o usuário.

Dentro do pacote *infra* vamos criar o arquivo `TratadorDeErros`, e para indicar ao Spring que essa é uma classe para tratar erros vamos colocar a anotação `@RestControllerAdvice` na classe. Agora basta adicionar o método referente a cada tipo de exceção.

### Erro 404

Vamos tratar primeiro o método detalhar médico, que caso seja enviado um id que não existe, o código de erro correto seria o 404 (não encontrado). Na classe *TratadorDeErros* vamos adicionar o método `tratarErro404` e acima do método a anotação `@ExceptionHandler`, que indica que este é um método que ira tratar um tipo de exceção. O erro que vamos tratar sera a da classe `EntityNotFoundException`, então vamos passar ela como parâmetro para a anotação.

```java
@ExceptionHandler(EntityNotFoundException.class)
public ResponseEntity tratarErro404() {
  return ResponseEntity.notFound().build();
}
```

Assim como os métodos do controller vamos retornar a classe ResponseEntity com o `notFound` que ira criar o erro 404 NOT FOUND.

### Erro 400

Quando o usuário tenta efetuar uma requisição enviando dados inválidos e ocasionando um erro de validação, o código que devera ser devolvido é o 400 BAD REQUEST, por exemplo no método `cadastrarMedico`. Como estamos utilizando o Bean Validation para fazer as validações, o Spring se integra com ele e caso ocorra alguma exceção, ele ja enviar o erro 400, porem além do código ele retorna diversas informações que não seriam necessárias. Vamos tratar essa exceção para enviar apenas os campos que tiveram problema com suas respectivas mensagens.

Dentro da classe tratador de erros vamos adicionar mais um método chamado `tratarErro400`. O tipo de exceção que ele ira capturar sera o `MethodArgumentNotValidException`, e como vamos precisar capturar as mensagens de erro, iremos assinar esse tipo como parâmetro do método em questão.

```java
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity tratarErro400(MethodArgumentNotValidException ex) {}
```

Caso agora seja colocado apenas o retorno com o ResponseEntity com o devido método, o usuário ira receber apenas o código sem nenhuma resposta no corpo. E como citado anteriormente, precisaremos capturar as mensagem de erro, o que pode ser feito através da função `getFieldErrors()` da propriedade *ex*.

```java
var erros = ex.getFieldErrors();
```

Agora dentro dessa variável temos o mesmo json que o Spring enviava como resposta para o erro 400, e como queremos apenas alguma campos, vamos utilizar um DTO para converter esse objeto.

```java
return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList());
```

O `badRequest` é o responsável pelo código 400, e apos ele chamamos o *body* para enviar informações no corpo da requisição. Então utilizamos o ja conhecido `stream` para lidar com a lista da variável *erros* e o `map` para executar uma função em cada item da lista, chamamos o construtor do DTO com o `::new`, dessa forma convertendo a lista para o padrão do DTO. Lembrando que é preciso utilizar o `toList` para converter o objeto em uma lista.

Esse DTO sera utilizado exclusivamente nessa classe, então vamos criar ele dentro dela mesmo.

```java
private record DadosErroValidacao(String campo, String mensagem) {
  public DadosErroValidacao(FieldError erro) {
    this(erro.getField(), erro.getDefaultMessage());
  }
}
```

O DTO precisa conter apenas o campo e sua mensagem. O construtor sera chamado la no `stream` e passado como parâmetro um objeto do tipo `FieldError`. Esse objeto contem os métodos `getField` e `getDefaultMessage`, que utilizamos para chamar o construtor padrão.

Pronto, agora o método cadastrar médico retorna uma resposta padronizada contendo apenas os campos e os erros.

## Segurança com Spring Security

O Spring possui um modulo especifico para tratar de segurança, que é o **Spring Security**. Esse pacote tem 3 grandes objetivos, que são:

- **Autenticação** : disponibiliza uma gama de opções customizáveis para autenticar o usuário, como por exemplo se vai ser feito através de um formulário, token (como o JWT), baseado em terceiros (como OAuth), algum protocolo, etc. Ele possui uma boa flexibilidade em como vamos gerenciar esse processo de autenticação.

- **Autorização** : disponibiliza configurações para como lidar com permissões e acessos concedidos a um usuário autenticado. Podemos configurar com base em permissão especificas, como por exemplo bloquear perfis com permissão *A* de acessar métodos de permissão *B*. Podemos também ter controle de acesso através de URL, baseado em método e baseado em anotação.

- **Proteção contra ataques** : disponibiliza alguns recursos para proteger a aplicação de uma variedade de ataques comuns, sendo alguns deles o Cross-Site Scripting (XSS), clickjacking, Cross-Site Request Forgery (CSRF), Injeção de SQL, entre outros. Ele utiliza alguns mecanismos de defesa, como a validação e sanitização de entrada, prevenção de CSRF por meio de tokens, tratamento adequado de erros de segurança e configurações de segurança personalizadas.

No caso desse projeto o que vamos fazer é o controle de acesso e autenticação, pois até o momento ele poderia ser acessado por qualquer um que soubesse da url (caso a api estivesse hospedada em um servidor). E devemos nos atentar ao fato de que esse controle de acesso deve levar em consideração que uma api é **Stateless**, ou seja, ela não guarda estado, não existe uma seção aberta, o que difere de uma aplicação web que é **Stateful**, onde precisa guardar as informações do usuário logado.

A solução que vamos utilizar sera a de tokens com o **JWT**. Mas o que é um token? Abaixo temos um exemplo:

```java
var token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
```

Um token nada mais é que uma string que contem algumas informações que passaram por um algoritmo de encriptação, e utilizamos o mesmo algoritmo também para validar o token.

Agora veremos como sera o funcionamento da api com o JWT:

1. Usuário envia as informações para o login : dentro a api teremos uma rota para o login, onde a aplicação ira consultar no banco de dados se o usuário realmente esta cadastrado.

2. Api retorna o token : caso os dados sejam validados então a aplicação cria e retorna um token do JWT.

3. Novas requisições devem enviar o token : agora o front end que ira consumir a api deve enviar no cabeçalho da requisição o token JWT para validar da operação.

4. Api executa o método : caso o token seja valido, a aplicação executa o método em questão.

### Adicionando o pacote Security

Primeiramente precisamos adicionar a nova dependência, e podemos utilizar o mesmo método do curso anterior onde utilizamos a ferramenta *Spring Initializr* para copiar as dependências e colar elas no arquivo `pom.xml`. Esse pacote adicionar 2 novas dependências, a *starter-security* e a *security-test*.

A partir deste momento todos os métodos da aplicação foram bloqueados, sendo apenas possível executar um método ao efetuar o login. Caso esteja utilizando uma ferramente como o **Insomnia** ou **Postman**, sera exibido apenas um erro de acesso negado, mas ao tentar executar através da web, sera aberto uma janela de login do próprio Spring Security, onde geralmente o usuário padrão é `user` e a senha é uma que ele imprime no console sempre que a api é iniciada.

## \ AVISO /

A partir deste momento serão necessários diversos passos até chegarmos no resultado mínimo esperado, com esse meio tempo sendo muito dificultado o teste da api ou das funcionalidades implementadas. Mas basta aplicar todos os passos que o projeto funcionara. Iremos seguir uma sequencia de quais tarefas serão feitas primeiro, porem diversas funcionalidades podem ser executadas antes ou depois.

## Domínio Usuário

Para ser validado se o usuário possui ou não cadastro no sistema, sera necessario uma tabela para armazenar tais informações. Primeiramente vamos criar a entidade `Usuario` dentro do pacote `domain.usuario`. Esta classe precisa apenas de 3 campos, o **id**, o **login** e a **senha**, e também as anotações para indicar a tabela no banco de dados, entidade, entre outros.

```java
@Table(name = "usuarios")
@Entity(name = "Usuario")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String login;
  private String senha;
}
```

### Migration usuarios

O proximo passo é criar uma migration para adicionar a tabela *usuarios* no banco de dados.

```sql
create table usuarios(
  id bigint not null auto_increment,
  login varchar(100) not null,
  senha varchar(255) not null,
  primary key(id)
);
```

### Usuário repository

Criaremos o repository para acessar o banco de dados.

```java
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {}
```

### Classe de serviço de autenticação

Agora precisamos criar uma classe que ira utilizar o Repository para buscar as informações do usuário no bando de dados. Ela sera carregada automaticamente pelo Spring, pois ele possui um comportamento padrão de buscar por estes arquivos de configuração, bastando ele seguir um certo padrão para poder ser encontrado.

Ainda dentro do domínio do usuário, vamos adicionar o arquivo `AutenticacaoService` que sera uma classe de serviço, então adicionaremos a anotação `@Service` em cima da classe, porem para que o Spring Security saiba que essa sera uma classe de serviço de autenticação, precisamos implementar a interface `UserDetailsService`. O único método que a interface pede para implementar é o `loadUserByUsername`, que ira buscar no banco de dados as informações do usuário.

```java
@Service
public class AutenticacaoService implements UserDetailsService {
  @Autowired
  private UsuarioRepository repository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return repository.findByLogin(username);
  }
}
```

> Lembrando que é preciso adicionar o `@Autowired` para que seja feita a injeção de dependência da classe Repository.

Sempre que o cliente tentar efetuar o login, o Spring Security automaticamente ira chamar este método. O processo e as configurações de login do usuário serão feitos em seguida, mas primeiro vamos adicionar o método `findByLogin` na classe *UsuarioRepository*.

### Busca usuário através do login

Para este método vamos utilizar novamente as *consultas derivadas*, onde é preciso apenas assinar o método na classe repository, que o Spring compreende o funcionamento do método apenas pelo nome. Por exemplo o método `findByLogin`, ele deixa claro o intuito de fazer uma busca através da coluna *login*.

```java
UserDetails findByLogin(String login);
```

## Configurações de segurança

Como visto anteriormente, o Spring Security cria um formulário para autenticar o usuário, mas como vamos utilizar os tokens, precisamos fazer algumas configurações de segurança. Geralmente utilizamos o arquivo *application.properties*, porem como esta é uma configuração mais complexa, que envolve algumas classes, então vamos criar um arquivo separado para concentrar essas configurações.

Dentro do pacote `infra.security` vamos adicionar a classe `SecurityConfigurations`. Acima da classe iremos adicionar as anotações `@Configuration` pois ele é um arquivo de configuração, e a `@EnableWebSecurity` para indicar ao Spring Security que vamos personalizar algumas configurações de segurança.

```java
@Configuration
@EnableWebSecurity
public class SecurityConfigurations {}
```

### Aplicação Stateless

A primeira configuração que vamos fazer é de mudar para uma aplicação stateless, onde não guardamos a seção do usuário. Como esta classe esta sendo carregada pelo security, precisamos retornar um objeto desse pacote, que é o `SecurityFilterChain` e o nome do método sera `securityFilterChain` (o nome não é importante para o security). Quando esse método for chamado, ele recebera um objeto do tipo `HttpSecurity`, logo vamos assinar ele como parâmetro.

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
  return http.csrf(csrf -> csrf.disable())
      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .build();
}
```

> Os nomes dos parâmetros da função anônima não são importantes, podendo ser alterado. Por exemplo poderíamos mudar o nome do *csrf* para `(CrossSiteRequestForgery -> CrossSiteRequestForgery.disable())`

Utilizamos o objeto `http` com o método `csrf` para configurar este tipo de segurança, dentro dele utilizamos uma função anônima com o operador de seta `->` para desabilitar a proteção contra esse tipo de ataque, pois o próprio token é uma proteção contra ataques CSRF. Em seguida encadeamos o `sessionManagement` que serve para configurar a politica de seção, e dentro dele chamamos o `sessionCreationPolicy` para criar uma politica do tipo `STATELESS`. Por fim utilizamos o `build` para construir o objeto *SecurityFilterChain*. Explicaremos o `@Bean` logo abaixo.

### Funcionamento do método securityFilterChain

Até o momento basicamente construímos o método sem explicar muito, o que pode deixar algumas duvidas. Como esse método ira funcionar? Será que devemos chamar esse método em alguma outra classe?

Esta é uma classe de configuração e que ira servir apenas para o Spring Security, não sendo necessario instanciar nada ou chamar algum método. Como visto anteriormente adicionamos a anotação `@EnableWebSecurity` que serve para o security carregar esta classe. Porem não basta apenas que ele carregue o arquivo, é preciso **expor o retorno** de cada método, e isso é feito através do `@Bean` que ira expor para o security o objeto retornado, por exemplo o `SecurityFilterChain`. Então quando o Spring Security precisar do objeto `SecurityFilterChain` ele ira chamar nosso método, assim aplicando as configurações que criamos.

IMPORTANTE: agora que adicionamos esse arquivo de configuração todas as rotas da api estão abertas, pois desativamos o comportamento padrão do Spring Security. Em seguida vamos criar a rota para login, para ai então começarmos a trabalhar com o JWT.

## Spring Boot 3.1 / DEPRECATED

A partir da versão 3.1 do Spring foi alterado a forma como é declarado as configurações de segurança, agora com os métodos recebendo uma função anônima como parâmetro (algo semelhante aos callbacks do javascript). O método *securityFilterChain* acima já esta em conformidade com a nova forma, mas vamos olhar como seria na forma antiga.

```java
// DEPRECATED 
return http.csrf().disable()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and().build();
```

Anteriormente era encadeado tanto o método como as suas configuração. Com a nova forma encadeamos apenas os métodos principais, e as configurações especificas são feitas dentro da função anônima, podendo também ser configurado diversos parâmetro na mesma função. Com isso se torna mais legível para saber qual configuração é de que método.

## Controller para rota de login

Antes de seguirmos para o token JWT precisamos criar uma rota para o login, onde o usuario podera enviar suas informações para poder receber o token de autenticação. Dentro o pacote `controller` vamos adicionar o arquivo `AutenticacaoController`.

```java
@RestController
@RequestMapping("/login")
public class AutenticacaoController {}
```

E em seguida vamos criar o método `efetuarLogin`.

```java
@PostMapping
public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {}
```

Esse DTO ainda não foi criado, então vamos adicionar ele no pacote `usuario`

```java
public record DadosAutenticacao(String login, String senha) {}
```

Agora que temos as informações do usuário no DTO precisamos efetuar a autenticação, então vamos chamar o método `loadUserByUsername` que criamos anteriormente. Porem não chamamos a classe `AutenticacaoService` diretamente, vamos utilizar uma função do Spring Security, e ela sim por baixo dos panos vai executar o método em questão. A classe que é responsável por disparar o método de autenticação é a `AuthenticationManager`, e para utilizar ela no controller, vamos criar uma propriedade com esse tipo.

```java
@Autowired
private AuthenticationManager manager;

@PostMapping
public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
  var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
  var authentication = manager.authenticate(authenticationToken);

  return ResponseEntity.ok().build();
}
```

Na propriedade `manager` temos o método `authenticate` que recebe como parâmetro um token com os dados de login, que é basicamente um DTO padrão do Security para este tipo de função. Devido a isso na primeira linha utilizamos o `new UsernamePasswordAuthenticationToken()` para converter o nosso DTO no token que o security utiliza. Logo em seguida executamos o método *authenticate* passando então o token. Por fim retornamos um objeto *ResponseEntity* só para testarmos se o método funcionou como esperado, pois logo faremos algumas alterações.

### Erro ao iniciar a aplicação

Apos criarmos o controller teremos o seguinte erro na aplicação `APPLICATION FAILED TO START`, que ocorreu devido a aplicação não ter conseguido encontrar um objeto do tipo `AuthenticationManager` e por consequência não conseguir injetar ele na propriedade do controller. Isso é devido a aplicação não conseguir injetar automaticamente um objeto do tipo *AuthenticationManager*, mesmo sendo uma classe do Spring. Dessa forma sera necessario criar uma configuração para que a aplicação consiga acessar essa dependência.

Como o método em questão é uma configuração de segurança, vamos cria-lo na classe `SecurityConfigurations`. O método precisa retornar um objeto do tipo `AuthenticationManager` e recebe como parâmetro um do tipo `AuthenticationConfiguration`. Agora basta chamar o método `getAuthenticationManager` que sabe como criar a classe `AuthenticationManager` que iremos utilizar no controller. Lembrando de fazer a anotação `@Bean`, pois é ela que vai disponibilizar o objeto para o Security usar e/ou para injetar como dependência.

```java
@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws 
Exception {
  return configuration.getAuthenticationManager();
}
```

Pronto, com isso a aplicação ja deve funcionar normalmente.

## Adicionando usuário na tabela

Apesar de a aplicação estar funcionando normalmente, ao fazer uma requisição para a rota de login o que iremos receber é o erro 403 FORBIDDEN, ou seja proibido. Porem o que de fato aconteceu é que não temos nenhum usuário cadastrado no banco de dados, e com isso qualquer requisição sera de fato não autorizada. Logo para que o cliente seja autenticado, precisamos cadastrar eles no banco de dados.

Podemo tanto criar uma rota para cadastrar novos usuários ou apenas fazer isso diretamente no banco de dados (no terminal, ou utilizando uma ferramenta como o MySQL Workbench). Os campos necessários para cadastrar o usuário são o de email e senha, porem por questão de segurança não inserimos abertamente a senha na tabela, e sim a senha encriptada. O macarismo de criptográfica que vamos utilizar é o Bcrypt, e sera necessario fazer uma configuração para informar ao Security que a senha no banco de dados esta nesse formato. Apenas como um exemplo, a senha `123456` ficaria:

```text
$2a$12$4cUTdbDXiUKe0rmgFSGw/uofYL0rRFXEJoqZCn/pH8IfWQ/KQsm4i
```

### Configurando encoder Bcrypt

Na classe `SecurityConfigurations` vamos adicionar mais um método que retorna um objeto do tipo `PasswordEncoder`, e dentro dele vamos retornar uma instancia da classe `BCryptPasswordEncoder`. Com isso o configuramos o Security para utilizar o encoder do Bcrypt.

```java
@Bean
public PasswordEncoder passwordEncoder() {
  return new BCryptPasswordEncoder();
}
```

## Implementando uma interface UserDetails

Apesar da classe *Usuario* estar sendo carregada pelo Spring, ela não esta pelo Spring Security, logo ela não sabe quais os campos da classe, nem os parâmetros dela. A solução disponibilizada pelo security é implementar a interface `UserDetails`, e com ela vem diversos métodos que podem ser utilizados para fazer configurações como se a conta deve expirar, se ela pode ser bloqueada, se ela esta ativa, entre outros.

Como nesse projeto não temos exigência para as configurações implementadas com a interface, vamos assinalar todos como **true** com exceção de 3 métodos, o `getUsername`, `getPassword` e `getAuthorities`, onde os 2 primeiros devem retornar seus respectivos campos. Já o terceiro é um caso a parte, o security espera que nesse método seja retornado uma lista com os "cargos" do usuário, que seria a autoridade do usuário para acessar os métodos da api, porem neste projeto não vamos ter rotas específicas para determinados cargos, todos os métodos podem ser acessados por qualquer usuário.

Neste curso não vamos implementar os cargos, mas ao final desse documento vou adicionar essa funcionalidade. Mas por agora precisamos dar um jeito de retornar uma lista de autoridades para o método, pois é obrigatório para o security, o que pode ser feito é criar uma lista passando apenas um tipo de cargo. Com isso temo os 3 métodos.

```java
@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
  return List.of(new SimpleGrantedAuthority("ROLE_USER"));
}
@Override
public String getPassword() {
  return senha;
}
@Override
public String getUsername() {
  return login;
}
```

No método `getAuthorities` retornamos uma lista da classe `SimpleGrantedAuthority`, que recebe como parâmetro o cargo. Por padrão o cargo possui o prefixo `ROLE_` e em seguida a autoridade, que pode ser qualquer uma escolhida.

## Token JWT

Ao executar o método de login vamos receber o código 200 OK, porem o que realmente deveríamos retornar é o token JWT para que o usuário se autentique nas outras rotas. Para gerar os tokens nesse formato precisamos adicionar uma nova biblioteca, que sera a **Auth0**. Essa biblioteca é disponibilizada pela empresa de mesmo nome, e tem o foco em criar soluções para autenticação em aplicações. Porem existem diversas outras bibliotecas semelhantes, no site `jwt.io` na opção *Libraries* vamos abrir um pagina com diversas bibliotecas que podemos escolher, mas é preciso se atentar na linguagem selecionada.

No card da biblioteca temos uma opção `View Repo`, onde seremos direcionados ao repositório no github. No arquivo readme temos varias explicações sobre o projeto, vamos copiar a dependência e colocar no nosso arquivo *pom.xml*. Pronto com isso ja instalamos o auth0 no projeto, mas ainda iremos utilizar o repositório para outras consultas.

### Gerando o token JWT

Vamos criar uma classe especifica para lidar com os tokens, e utiliza-la na rota de login, pois temos trechos de códigos um pouco mais complexos para essa tarefa, o que tornaria a classe controller muito verbosa e adicionando muitas responsabilidades a ela.

Dentro do pacote `infra.security` vamos criar a classe `TokenService` e acima dela adicionar a anotação `@Service`. O primeiro método deve retornar o tipo *string* e dentro dele é que vamos utilizar a nova biblioteca. De volta ao repositório do pacote, temos o tópico **Create a JWT** e abaixo dele um trecho de código de exemplo, vamos copia-lo e colar no método.

```java
public String gerarToken() {
  try {
    var algoritmo = Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
    String token = JWT.create()
        .withIssuer("auth0")
        .sign(algoritmo);
  } catch (JWTCreationException exception){
    // Invalid Signing configuration / Couldn't convert Claims.
  }
}
```

Vamos entender o funcionamento do código e adaptar o que necessario. Primeiramente devemos selecionar um algoritmo de criptografia, no exemplo foi escolhido o *RSA256* mas vamos utilizar o **HMAC256**, que recebe uma senha para assinar o token, deve ser uma senha forte e de preferencia não ficar exposta diretamente no código. Em seguida podemos retornar diretamente o token sem a necessidade da variável, e então chamamos o JWT com o método `create` para começar a criar o token. Após esse método, devemos encadear algumas configurações que iram adicionar informações no token, e algumas delas são:

- **Issuer** : adiciona no token o proprietário da api que o gerou

- **Subject** : adiciona o email do usuário no token

- **ExpiresAt** : adiciona o horário que o token expira

- **Claim** : adiciona qualquer informação de chave-valor. Por exemplo o código `.withClaim("id", usuario.getId())` adiciona ao token a informação do id do usuário

```java
public String gerarToken(Usuario usuario) {
  try {
    var algoritmo = Algorithm.HMAC256(secret);
    return JWT.create()
        .withIssuer("API Voll.med")
        .withSubject(usuario.getLogin())
        .withExpiresAt(dataExpiracao())
        .sign(algoritmo);
  } catch (JWTCreationException exception) {
    throw new RuntimeException("erro ao gerar token jwt", exception);
  }
}
```

Como precisamos das informações do usuario, foi assinado ele como parâmetro do método. No algoritmo adicionamos a propriedade `secret`, que busca a informação da senha no arquivo *application.properties*. Ja na configuração de expiração chamamos um método da própria classe que estipula um tempo máximo de 2 horas de duração do token.

```java
@Value("${api.security.token.secret}")
private String secret;

// CÓDIGO OMITIDO

private Instant dataExpiracao() {
  return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
}
```

Com a anotação `@Value` o Spring nos permite ler alguma informação no arquivo `application.properties`, que além de configurações de projeto, podemos adicionar campos personalizados. Como passamos a chave `${api.security.token.secret}` como parâmetro, ele ira procurar no arquivo o valor inserido nessa chave. Logo precisamos configurar essas informações no arquivo.

```properties
api.security.token.secret=${JWT_SECRET:123456}
```

> O nome da chave pode ser qualquer coisa, mas é interessante ser descritivo e obrigatoriamente conter o mesmo nome na anotação que ira utiliza-la.

Quado passamos o `${JWT_SECRET}` informamos que devera ser procurado na variável de ambiente da maquina esse valor (variável essa que deve ter o nome JWT_SECRET), mas como ainda estamos em tempo de produção podemos passar um valor caso a variável não seja encontrada, por isso adicionamos o `:123456`.

### Verificando o token JWT

Existe mais alguns passos a frente para precisar do método de verificar o token, mas vamos aproveitar para deixar ele pronto. Este método deve receber o token, deve valida-lo e por fim retornar o email do usuário. Novamente no repositório do auth0 temos outro trecho de código de exemplo para verificar um token, fazendo as adaptações necessárias temos:

```java
public String getSubject(String tokenJWT) {
  try {
    var algoritmo = Algorithm.HMAC256(secret);
    return JWT.require(algoritmo)
        .withIssuer(Issuer)
        .build()
        .verify(tokenJWT)
        .getSubject();
  } catch (JWTVerificationException exception) {
    throw new RuntimeException("Token JWT inválido ou expirado!");
  }
}
```

Configuramos o algoritmo para o mesmo que estamos utilizando, informamos o *Issuer*, chamamos o *build* e então utilizamos o `verify` para verificar o token, caso ele seja valido então chamamos o `getSubject` para pegar o email do usuario, caso não ele joga uma exception.

### Controller retornando token

Agora voltamos para a classe controller e vamos criar uma propriedade da classe `TokenService` criada anteriormente, e vamos utilizar o método `gerarToken` passando o usuario como parâmetro. Por fim no ResponseEntity retornamos o DTO do token.

```java
@Autowired
private TokenService tokenService;

@PostMapping
public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
  var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
  var authentication = manager.authenticate(authenticationToken);
  var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

  return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
}
```

A variável `authentication` representa o usuario autenticado, com ela podemos chamar o método `getPrincipal` para obter as informações do usuario, porem ele retorna um objeto, por isso é necessario fazer o casting dele com o `(Usuario)`. Por fim vamos criar o DTO `DadosTokenJWT` no pacote `infra.security`.

```java
public record DadosTokenJWT(String token) {}
```

Pronto, com isso a rota de *login* agora esta retornando o token JWT. Mas apesar de ter uma grande quantidade de passos para chegar nesse resultado, ainda não estamos validando esse token nas outras rotas, e é o que veremos a seguir.

## Interceptando requisições

Desde quando fizemos a configuração para tornar a aplicação **stateless** todos os métodos estão abertos para qualquer requisição, porem deve ser obrigatório o envio do token pelo usuario e também precisamos validar esse token, e o melhor momento para executar essa tarefa é antes dela cair no controller. Mas primeiro vamos falar sobre o pacote *jakarta.servlet*.

Os Servlets são unidades que processão as requisições recebidas na aplicação, ela padroniza o tratamento de requisições e respostas de aplicações Web no Java. Por exemplo, um servlet recebe uma requisição e decide qual controller ira chamar. Esse pacote é uma especificação do java, e ja vem incorporado no Spring Boot, porem pode ser utilizado em qualquer aplicação java. Mas o que estamos interessados nesse pacote é o recurso **Filter** que permitem interceptar e manipular requisições e respostas antes de elas chegaram no próprio Spring, e é ele que vamos utilizar para autenticar o token. Obviamente os *filters* não servem apenas para isso, podendo ser utilizamos para autorização, registro de logs e auditoria, para compressão de resposta, entre outros. Na aplicação podemos ter zero ou vários filtros, sendo possível encadear diversos deles em sequencia, onde o *filter* pode chamar o proximo ou não de acordo com uma condição.

## Criando o filtro

Agora vamos criar um filter no pacote `infra.security` com o nome `SecurityFilter`, e a anotação da classe sera `@Component`, pois ela não é uma classe controller, nem uma repository, nem de configuração (não de configuração para o Spring) e nem uma de service, logo para esse tipo de classe temos a anotação *component*, com isso a classe sera carregada pelo Spring mas não tem um significado semântico forte.

Normalmente ao construir uma classe filter do jakarta, implementamos a interface `Filter` desse mesmo pacote, porem como ja estamos carregando a classe com o Spring e queremos algumas facilidades que ele traz, vamos extender da classe `OncePerRequestFilter` que ela sim implementa a interface Filter. O nome da classe é praticamente auto explicativa, onde é para executar o filtro uma unica vez para cada requisição.

```java
@Component
public class SecurityFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain){
    System.out.println("Filtro sendo chamado!!!!");
  }
}
```

O único método que é obrigatório sobrescrever é o `doFilterInternal`, e após adicionarmos esse filtro, todas as rotas retornam o código 200 mas não enviam nada no corpo da resposta. Isso ocorre por que a partir do momento que adicionamos 1 filtro na aplicação, ele obrigatoriamente precisa chamar a **cadeia de filtros** caso a requisição passe pela condição, caso contrario acontece o erro citado acima, o proximo passo nunca é chamado.

No método temos 3 parâmetros assinados sendo o terceiro o que representa a cadeia de filtros, ou seja precisamos chamar ele passando o *request* e o *response* como parâmetro para que as próximas função sejam chamadas corretamente.

```java
@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain){
  filterChain.doFilter(request, response);
}
```

### Função para recuperar o token

Para recuperar o token dentro do método precisamos sabe por onde ele sera enviado, e como esta mais envolvido com a configuração de segurança ele não é enviado no corpo da requisição e sim no cabeçalho, sendo que temos um especifico para enviar o token, que é o **cabeçalho Authorization**. Lembrando que o cliente que vai consumir essa api precisar seguir esse mesmo padrão.

No Insomnia temos como simular esse cabeçalho, indo na navbar e abrindo a aba **auth** e escolhendo a opção **bearer token**, sera aberto a janela com o campo *token* onde iremos adicionar o token enviado pela rota *login*, já no campo *prefix* vamos deixar em branco que o Insomnia automaticamente envia a string `Bearer`.

Antes de validarmos o token, vamos olhar a função `recuperarToken` que sera usada no método principal, ela ira receber o `request` como parâmetro, e retorna a string do token, ou null caso esteja vazia.

```java
private String recuperarToken(HttpServletRequest request) {
  var authorizationHeader = request.getHeader("Authorization");
  if (authorizationHeader != null) {
    return authorizationHeader.replace("Bearer ", "");
  }
  return null;
}
```

> Na string "Bearer " do replace, não esquecer de adicionar o espaço apos a palavra, pois se não o token ira ficar com um espaço em brando antes da primeira letra.

O objeto *request* já possui um método ideal que é o `getHeader` onde passamos o nome do cabeçalho e ele recupera a informação desse campo. Em seguida validamos se ele não é nulo e então retornamos o token. Utilizamos o `replace("Bearer ", "")` pois como foi dito anteriormente, o Insomnia envia o prefixo *Bearer* antes do token.

### Adicionando a logica

Os próximos passos são validar o token e autenticar o usuario que fez a requisição, e para a primeira tarefa iremos chamar o método `getSubject` da classe `TokenService`.

```java
@Autowired
private TokenService tokenService;

// Código omitido
  var tokenJWT = recuperarToken(request);
  var subject = tokenService.getSubject(tokenJWT);
```

Com isso tanto o token foi validado como temos na variável `subject` o email do usuario, e com ele vamos buscar o usuario no banco de dados através do email, para então autenticarmos essa requisição. Porem sera necessario verificar se esta vindo um token na requisição, pois na rota `/login` não é enviado o token, logo ao tentar valida-lo ira ocorrer um erro e a requisição não sera processada.

```java
@Autowired
private UsuarioRepository repository;
// Código omitido

  var tokenJWT = recuperarToken(request);
  if (tokenJWT != null) {
    var subject = tokenService.getSubject(tokenJWT);
    var usuario = repository.findByLogin(subject);
  }
```

Por fim é necessario autorizar o usuario da requisição, e fazemos isso com a classe do Spring `SecurityContextHolder` chamando o método estático `getContext` e por fim setando um objeto *usuario logado* com o método `setAuthentication`. Porem este ultimo método recebe objeto *authentication* que pode ser criado instanciando a classe `UsernamePasswordAuthenticationToken`, onde ela recebe no primeiro parâmetro o objeto *Usuario*, no segundo a credencial, que no caso não é necessario devido a ja estarmos informando a entidade Usuario, e o terceiro é a autorização.

```java
@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain 
filterChain)
    throws ServletException, IOException {
  var tokenJWT = recuperarToken(request);
  if (tokenJWT != null) {
    var subject = tokenService.getSubject(tokenJWT);
    var usuario = repository.findByLogin(subject);
    var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
  filterChain.doFilter(request, response);
}
```

Agora vamos revisar o funcionamento do método. Na primeira linha recuperamos o token para em seguida validar se esta nulo ou não, caso não, dentro do bloco de código selecionamos o email do usuario com o *getSubject* e passamos ele para o *repository* recuperar a entidade Usuario no banco de dados, em seguida criamos o objeto *authentication* passando a entidade e seu *cargo* (que no momento é apenas ROLE_USER), e por fim podemos forçar a autenticação da requisição com o *SecurityContextHolder* pois o token ja foi verificado na classe *tokenService*. Fora do bloco de códigos, não podemos esquecer de chamar o *filterChain* para que ele de sequencia a aplicação.

## Configurando autenticação para as rotas

Com o filtro pronto precisamos exigir a autenticação da requisição em todas as rotas, com exceção é claro da rota *login*, pois desde quando configurado a aplicação para *stateless* todas as rotas estão abertas, com agora o filtro a diferença que ele exige que seja enviado um token na requisição, porem se enviarmos qualquer coisa no token como `123456` ja é valido para executar o método.

Esta configuração sera feita no arquivo `SecurityConfigurations` no método `securityFilterChain`. Vamos utilizar o `authorizeHttpRequests` para essa tarefa, ele nos fornece um objeto do tipo *AuthorizationManagerRequestMatcherRegistry* e com ele podemos configurar as restrições para as rotas.

```java
return http.csrf(csrf -> csrf.disable())
  .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
  .authorizeHttpRequests(req -> {
    req
        .requestMatchers(HttpMethod.POST, "/login").permitAll()
        .anyRequest().authenticated();
  })
  .build();
```

Com o objeto `req` podemos encadear as configurações, e são elas a `requestMatchers` que nos permite mapear uma rota e atribuir uma configuração a ela, que no caso foi o `permitAll`, ou seja qualquer requisição para `/login` deve ser permitida, e em seguida utilizamo o `anyRequest` para selecionar todas as outras rotas não mapeadas e aplicamos o `authenticated` para exigir que suas requisições sejam autenticadas.

### Ordenando os filtros

Apos a configuração anterior, todas as rotas com exceção da *login* foram bloqueadas, isso ocorre devido ao fato de haver mais de um filtro na aplicação, o que criamos e o do próprio Spring, e por padrão ele ira chamar o seu por primeiro, logo ele checa se o usuario esta autenticado (e obviamente não esta) e já bloqueia a requisição, assim nem chamando o nosso filtro para autenticar o usuario. Para solucionar esse problema precisamos configurar a ordem de quais filtros serão chamados.

Ainda no método `securityFilterChain` vamos adicionar uma nova configuração antes do `.build()`, que sera o `addFilterBefore` que recebe no primeiro parâmetro o filtro que deve ser chamado antes, e no segundo o filtro o que deve ser chamado depois.

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
  return http.csrf(csrf -> csrf.disable())
      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(req -> {
        req
            .requestMatchers(HttpMethod.POST, "/login").permitAll()
            .anyRequest().authenticated();
      })
      .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
      .build();
}
```

No primeiro parâmetro do *addFilterBefore* passamos o filtro que criamos e no segundo o filtro padrão do Spring, que podemos chamar através do `UsernamePasswordAuthenticationFilter.class`.

Pronto, com isso temos todas as rotas funcionando como esperado.

Em sequencia vamos adicionar a funcionalidade de *cargos* na aplicação. Como sera uma tarefa extra curso, pode ser que algumas boas praticas não sejam seguidas.

## Adicionando o campo de Cargo

Primeiramente iremos adicionar o campo cargo na entidade usuario, que sera do tipo ENUM, e além do campo é preciso fazer uma alteração no método `getAuthorities`.

```java
@Enumerated(EnumType.STRING)
private Cargos cargo;

@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
  var role = "ROLE_"+cargo;
  return List.of(new SimpleGrantedAuthority(role));
}
```

Na variável `role` concatenamos o cargo com o `ROLE_`, pois quando se trabalha com autoridade é preciso adicionar esse prefixo, e em seguida passamos a variável para a lista de `SimpleGrantedAuthority`.

Agora vamos criar o ENUM, ele também vai ser adicionado no `domain.usuario`

```java
public enum Cargos {
  USER,
  ADMIN,
  MANAGER
}
```

Além disso precisamos criar o novo campo no banco de dados, logo vamos adicionar uma nova migration com o nome `V6__alter-table-usuarios-add-column-cargo.sql`.

```sql
ALTER TABLE usuarios ADD cargo VARCHAR(50);
UPDATE usuarios SET cargo = 'USER';
ALTER TABLE usuarios CHANGE cargo cargo VARCHAR(50) NOT NULL;
```

Na primeira linha adicionamos a coluna nova, na segunda setamos todos os usuarios já criados para um usuario comum e na ultima adicionamos a constraint NOT NULL.

### Habilitando anotação *Secured*

Antes de fazer as anotações nos métodos que vamos restringir o acesso, é preciso fazer uma configuração simples no arquivo `SecurityConfigurations` no pacote `infra.security`. Em cima da declaração da classe vamos adicionar a anotação `@EnableMethodSecurity` para habilitar outra anotação que iremos utilizar a seguir.

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfigurations {}
```

Ativamos o *Secured* com a configuração `securedEnabled`. Além dessa configuração temos outras como por exemplo o `jsr250Enabled` para habilitar o *@RoleAllowed*, ou o `prePostEnabled` para ativar o *@PreAuthorize* e *@PostAuthorize*.

### Exigindo cargo nas rotas

A anotação habilitada acima sera utilizada para exigir que alguma rote precise ter uma autoridade especifica, vamos fazer a anotação `@Secured` acima do método desejado, passando como parâmetro um objeto contendo os cargos que poderão utilizar esse método. Por exemplo o método de detalhar medico:

```java
@GetMapping("/{id}")
@Secured({ "ROLE_ADMIN", "ROLE_MANAGER" })
public ResponseEntity detalharMedico(@PathVariable Long id) {
  var medico = repository.getReferenceById(id);
  return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
}
```

Como este método da acesso a informações sensíveis dos médicos, somente o admin e o gerente podem acessa-lo.
