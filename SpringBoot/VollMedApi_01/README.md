# Curso 1 projeto de api Voll Med

Esta formação busca ensinar desde a criação de um projeto com Spring Boot, passando por segurança, testes automatizados e por fim o build. A ideia é criar uma api para a empresa fictícia Voll Med, que é uma clinica que precisa de um sistema para gerenciar as consultas.

Como este é um curso que pode ser consumido por pessoas com pouco ou nenhum conhecimento em Spring Boot, iremos seguir uma linha logica para facilitar a compreensão do funcionamento de uma api que utiliza esta tecnologia. Isso significa que não necessariamente seguiremos o caminho mais performático.

Este documento ira seguir um passo a passo do mais básico, como a criação do projeto, para o mais avançado, que é o build do projeto. Porém não sera apresentado todos os códigos, pois basta consultar os arquivos para tal, além de que algumas observações também estarão comentadas nesses arquivos.

## Spring Initializr

O Spring Initializr é uma ferramenta que auxilia na criação um projeto com Spring. Podemos escolher diversas opções de projetos, e para este em especifico temos as opções:

|                  |        Opção         |
| :--------------- | :------------------: |
| **Project**      |        Maven         |
| **Language**     |         java         |
| **Spring Boot**  |        3.1.0         |
| **packaging**    |         Jar          |
| **Java**         |          17          |
| **Dependencies** | Spring Boot DevTools |
|                  |        Lombok        |
|                  |      Spring Web      |

Mais dependências serão necessárias, mas por agora começaremos com apenas estas 3. Apos isso basta clicar em **Generate** para baixar o projeto.

O projeto vem com uma estrutura de pastas simples e com alguns arquivos criados. Um desses arquivos é o que termina com o nome `nomeApplication.java`, que sera responsável por iniciar a aplicação.

## Estrutura de pastas

Existes diversas formas e boas praticas para criar a estrutura de pastas de um projeto, e não sera diferente neste projeto, seguiremos algumas convenções para estruturar os arquivos. Na pasta `src/main/java` seguiremos o seguinte padrão:

- **controller** : responsável por mapear as rotas da api, e chamar os métodos para cada função.

- **domain** : responsável pelo domínio, como o Médico, Pacientem Consulta, entre outros.

> Dentro do pacote *domain.medico* por exemplo, teremos a entidade Medico, os DTOs, e alguns outros arquivos referentes ao domínio da entidade médico.

- **infra** : responsável pela infraestrutura da api, como a parte de segurança, a parte de tratamento de exceções e outras configurações.

Voltando agora para a pasta `src/main/resources`, temos o seguinte estrutura:

- **db.migration** : responsável pos armazenas as *migrations*.

> Migration são os comando sql que faram as alterações no banco de dados, como criar novas tabelas, altera colunas, adicionar colunas, ou seja, tudo que precise alterar o banco de dados sera feito através de uma migration.

- **resources** : dentro da própria pas resources temos os aquivos `application.properties`, que armazenam algumas configurações para a api.

## Primeiro paço

Um dos primeiros arquivos que sera criado é um *controller*, onde iremos criar as rotas, e com ela podemos começar a interagir com a aplicação. Começaremos com o arquivo `MedicoController` que ficara na raiz do projeto no pacote `VollMedApi*01.controller`.

### Carregando a classe

Para que o spring carregue a classe, é necessario fazer a anotação `@RestController` acima da classe, e além disso, informaremos qual o caminho da url, que é `@RequestMapping("medicos")`. Ou seja sempre que recebemos uma requisição para o caminho `"/medicos"`, ele cairá nesse controller.

```java
@RestController
@RequestMapping("medicos")
public class MedicoController {}
```

### Adicionando o primeiro método

Iremos criar o método `cadastrarMedico`, e precisamos fazer a anotação para indicar qual verbo que chamará esse método, que no caso é o POST e a anotação fica `@PostMapping`. Além disso, iremos receber as informações para castrar o médico através do corpo da requisição, então antes de declarar o parâmetro, adicionamos a anotação `@RequestBody`.

```java
@PostMapping
public void cadastrarMedico(@RequestBody String dados) {
  System.out.println(dados);
}
```

### Criando um DTO

Por padrão é utilizado o formato **JSON** para transferência de informações em requisições http, porem não é interessante trabalhar com o json "cru", criando funções para procurar os campos e os valores no meio da string. O Spring Boot ja disponibiliza um serializador de json, e com a nova classe **Record**, podemos criar um **DTO** (Data Transfer Object) que representará os campos e os valores que serão enviados e devolvidos nas requisições.

Durante o projeto sera criado alguns DTOs, e cada um deles deve ficar no pacote de seu domínio. Este tera o nome `DadosCadastroMedico` e sera criado no pacote `VollMedApi*01.medico`.

```java
public record DadosCadastroMedico(String nome, String email, String telefone, String crm, Especialidade especialidade, DadosEndereco endereco) {}
```

> *Especialidade* é uma classe **ENUM**, que representa as especialidades de um médico da clinica.

### DTO para o endereço

Assim como foi criado um DTO para os dados do cadastro de médico, vamos criar um para os dados do endereço, pois além de ser diversos campos, eles serão utilizados em outras classes, e, por esse motivo ele sera criado no pacote `VollMedApi*01.endereco`.

```java
public record DadosEndereco(String logradouro, String bairro, String cep, String cidade, String uf, String complemento, String numero) {}
```

Pronto, com isso já é possível imprimir no terminal as informações enviadas para o método `cadastrarMedico()`.

## Adicionando dependências para conexão com o banco de dados

Para criar uma conexão e posteriormente salvarmos informação no bando de dados, precisaremos de algumas novas bibliotecas, que podem ser procuradas no repositório Maven, ou ainda mais fácil utilizando a ferramenta **Spring Initializr** vista anteriormente. Primeiramente precisamos selecionar as opções de *projeto*, *linguagem* e *java* sendo respectivamente: **Maven**, **Java** e **17**.

Agora basta selecionar as dependências (ao segurar a tecla `CTRL` e clicar no item desejado, a lista de dependências não é fechada, facilitando a seleção de múltiplos itens). As quatro dependências são:

- **Spring Data JPA**

- **MySQL Driver**

- **Spring Validation**

- **Flyway Migration**

Então iremos clicar na opção `Explore`, que exibira a estrutura de arquivos do projeto, e também ja virá com o arquivo `pom.xml` selecionado. Logo basta descer até a tag *dependencies* e copiar as dependências selecionadas, colando elas no *pom.xml* do nosso projeto.

Para algumas IDEs é preciso ir até a seção do *Maven* e clicar para recarregar o projeto, assim baixando as novas dependências, mas no VS Code com o pacote Java Pack, isso ocorre automaticamente após salvas as alterações no arquivo *pom.xml*.

### Configurando a conexão com o banco de dados

Após instalar o *Spring Data JPA* a aplicação sempre tentara criar uma conexão com o banco de dados ao ser iniciada, o que ocorrera um erro pois ainda não configuramos essa conexão.

Para esta e diversas outras configurações, é utilizado o arquivo `application.properties`. Iremos configurar o *url*, o *username* e o *password*.

```properties
spring.datasource.url=jdbc:mysql://localhost/nome*database
spring.datasource.username=root
spring.datasource.password=*****
```

> O url é referente à url de conexão com o database do MySQL, seguido do usuário e a senha também do MySQL.

Lembrar de criar o database com o mesmo nome do informando na url no MySQL, caso contrario o programa sempre apresentara um erro durante a execução.

## Entidade JPA

Para salvar e posteriormente trazer informações do bando de dados, precisamos criar uma entidade que ira representar uma tabela no MySQL. Essa entidade é chamada de JPA (Java Persistence API).

O Spring Boot ja possui embutido um provedor de persistência de dados, bastando assim apenas adicionar algumas anotações em cima da classe que sera a entidade JPA.

As anotações que faremos na classe serão o `@Table` que indica qual tabela no banco de dados que esse JPA ira trabalhar, e a `@Entity` que indica que esta classe sera a responsável pela entidade JPA da tabela selecionada com o *Table*.

```java
@Table(name = "medicos")
@Entity(name = "Medico")
public class Medico {}
```

> É possível omitir o nome na anotação *Entity*, isso fara o Spring inferir que o nome da entidade é o mesmo nome da classe.

### Adicionando os campos

A classe JPA precisa ter os mesmos campos e com os mesmos nomes das colunas na tabela do MySQL. Focaremos apenas no 3 campos que precisam de anotação, que são o **id** que precisa da anotação `@Id` indicando que ele sera a chave primaria da tabela, além da anotação `@GeneratedValue` já que esse valor sera atribuído automaticamente, o campo **especialidade** que precisa da `@Enumerated` pois ele é uma classe ENUM, e por ultimo o **endereco** que precisa da anotação `@Embedded`.

Explicando um pouco melhor o *Embedded*, ele é utilizado para incorporar um objeto complexo dentro de uma entidade. Utilizamos eles para evitar criar uma nova tabela para as informações de endereço, pois podemos incorporar essas informações na entidade *Medico*. Com isso eles serão tratados como um único valor no banco de dados.

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Enumerated(EnumType.STRING)
private Especialidade especialidade;

@Embedded
private Endereco endereco;
```

### Criando a entidade Endereço

Para que a entidade *Medico* e outras próximas possam utilizar a classe endereço, sera necessario criar uma entidade para ela. A forma como vamos cria-la é semelhante a de *Medico*, mas possui algumas diferenças.

Em cima da classe, ao invés de ter as anotações de tabelas, teremos a anotação `@Embeddable`, que indica que essa classe sera incorporada em outras entidades.

```java
@Embeddable
public class Endereco {}
```

E os campos serão atribuídos da mesma forma de qualquer classe `private String logradouro;`.

## Diminuindo verbosidade com o Lombok

Uma das bibliotecas que foram adicionadas no projeto foi o Lombok, que cria métodos em tempo de execução apenas fazendo anotações em cima da classe.

Para que nossa primeira entidade JPA esteja completa, precisamos de alguns métodos sejam implementados. As anotações do Lombok que utilizaremos são o `@Getter` para adicionar os getters para cada campo, `@NoArgsConstructor` para adicionar o construtor padrão sem nenhum parâmetro que é obrigatório para uma classe JPA, `@AllArgsConstructor` para adicionar um construtor com todos os parâmeros da classe e `@EqualsAndHashCode` para adicionar os métodos equals e hashCode.

O \*EqualsAndHashCode é mais utilizado no campo identificador da tabela, pois ele é um campo único que não pode pode ter seu valor repetido com nenhum outro na tabela.

```java
@Table(name = "medicos")
@Entity(name = "Medico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Medico {}
```

> Ao passar o parâmetro *of* para a anotação *EqualsAndHashCode*, informamos qual o único campo que deve receber os métodos equals e hashCode.

O mesmo vale para a classe endereço, precisamos fazer algumas anotações.

```java
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {}
```

## Criando o repository

Normalmente para acessar o banco de dados utilizamos classes DAO (Data Access Object), e criamos métodos para efetuar o **CRUD** da aplicação. Porém com o Spring Boot temos um mecanismo que simplifica essa tarefa, que é a interface `JpaRepository`.

Para criar um repository da classe médico, iremos adicionar no pacote `VollMedApi*01.medico` a interface `MedicoRepository`. Devemos também estender da interface `JpaRepository`, informando qual o nome da entidade que sera mapeada no banco de dados, e o tipo do identificador da entidade.

```java
public interface MedicoRepository extends JpaRepository<Medico, Long>{}
```

## Salvando informações no banco de dados

Agora que temos o repository criado, vamos utiliza-lo na classe controller apenas adicionando uma propriedade do tipo `MedicoRepository`. Porém não sera nos a colocar as informações na propriedade, e sim o Spring. Com a anotação `@Autowired` o Spring faz a **injeção de dependência** automaticamente, pois ele conhece a classe *MedicoRepository* ja que ela estende de uma interface do próprio Spring.

```java
@Autowired
private MedicoRepository repository;
```

O JpaRepository ja possui grande parte dos métodos que são utilizados para o CRUD, com exceção apenas para métodos mais complexos ou específicos para o caso. Começaremos com o `save`, que como o nome sugere, ira salvas o dados no DB.

```java
@PostMapping
@Transactional
public void cadastrarMedico(@RequestBody DadosCadastroMedico dados) {
  repository.save(new Medico(dados));
}
```

Além disso, como esse é um método que vai alterar informações no banco de dados, precisamos de uma transação ativa durante a execução do métodos, para isso utilizamos a anotação `@Transactional`.

## Migrations com Flyway

Ao tentar cadastrar um médico recebemos um erro 500, por que não foi possível encontrar a tabela, isso se deve ao fato de que ainda não criamos as tabelas. E como boa pratica não é recomendado criar manualmente essas tabelas, e sim utilizar uma *migration*, pois com elas temos melhor controle sobre o banco de dados e um histórico de evolução do database.

**OBS**: Qualquer alteração no database deve ser feito através de uma **migration**. Sempre que for criar uma nova migration, parar a aplicação. Uma migration é **Imutável**, ou seja, jamais deve ser alterada após ser executada no banco de dados.

Um das bibliotecas adicionadas ao projeto foi o Flyway, que ja possui uma certa integração com o Spring Boot facilitando a tarefa. Voltando para a pasta `src/main/resources`, vamos adicionar o pacote `db/migration`, e dentro desta pasta iremos criar as migrations. O Flyway possui um padrão de nomenclatura para as migrations, que é `V1__nome-descritivo.sql`, e a cada novo item alteramos a versão (V1, V2), além de que é preciso saliente que são 2 underlines apos a versão.

A primeira migration tem o nome `V1__create-table-medicos.sql` e contem o código:

```sql
create table medicos(
  id bigint not null auto_increment,
  nome varchar(100) not null,
  email varchar(100) not null unique,
  crm varchar(6) not null unique,
  especialidade varchar(100) not null,
  logradouro varchar(100) not null,
  bairro varchar(100) not null,
  cep varchar(9) not null,
  complemento varchar(100),
  numero varchar(20),
  uf char(2) not null,
  cidade varchar(100) not null,

  primary key(id)
);
```

### Histórico e erros nas migrations

O Flyway cria no DB uma tabela chamada `flyway_schema_history`, que é um histórico que armazena algumas informações de cada migration executada. Uma delas a coluna `success`, que armazena 1 para uma migration executada corretamente e 0 caso não.

Digamos então que ao tentar executar a aplicação e ocorra algum erro na migration, por exemplo se o código sql estiver incorreto, a aplicação ira parar e será gravado no historio na coluna *success* o valor 0. Isso impedira que a aplicação seja iniciada até que o erro no database seja corrigido (caso exista) e a linha referente ao erro no historio do Flyway seja removida. Podemos utilizar o comando sql abaixo para remover o valor do histórico:

```sql
DELETE FROM flyway_schema_history WHERE success = 0;
```

## Validações com o Bean Validation

Até este ponto ja conseguimos cadastrar um médico salvando suas informações no DB, mas assim como qualquer formulário precisamos validar essas informações, e para isso utilizaremos a biblioteca Spring Validation que adicionamos no projeto anteriormente. Ela funciona adicionando anotações em cima da propriedade que desejamos efetuar a validação, e geralmente utilizamos na classe DTO.

A classe DTO que representa o cadastro de médicos é a `DadosCadastroMedico`, e é nela que iremos adicionar as anotações. Existem diversas opções de validação para um campo, e para saber os detalhes de cada um basta pesquisar a documentação. Mas veremos algumas das mais comuns e algumas especificas para o projeto.

- `@NotBlank` : a string não pode esta vazia ou conter apenas espaços

- `@NotNull` : o campo não pode ser nulo

- `@Email` : possui um padrão de email

- `@Pattern(regexp = "")` : define um padrão para o campo de acordo com um regex informado. Por exemplo o regex `"\\d{8}"` indica que o campo deve conter apenas números e um total de 8 dígitos.

- `@Valid` : valida os campos de outro DTO

- `@Future` : valida se a data informada esta no futuro

- `@Size(min = , max = )` : o campo precisa estar entre o mínimo e o máximo informado. Pode ser aplicado em *string*, *collection*, *map* e *array*.

Lembrando que existem parâmetros que podemos informar para cada anotações, como o `message` onde podemos informar uma mensagem caso o campo em questão não passe na validação.

Observação, para exibir a mensagem é preciso criar um classe para lidar com as exceções, sendo ela do tipo `MethodArgumentNotValidException`. Neste artigo temos um exemplo de como aplicar o processo [Link](https://www.baeldung.com/spring-boot-bean-validation).

```java
@NotBlank(message = "Nome é obrigatório") String nome,
@NotBlank @Email String email,
@NotBlank String telefone,
@NotBlank @Pattern(regexp = "\\d{4,6}") String crm,
@NotNull Especialidade especialidade,
@NotNull @Valid DadosEndereco endereco
```

E assim como no DTO de cadastro de médico, precisamos validar as informações de endereço, por esse motivo colocamos o `@Valid` no campo *endereco*.

```java
@NotBlank String logradouro,
@NotBlank String bairro,
@NotBlank @Pattern(regexp = "\\d{8}") String cep,
@NotBlank String cidade,
@NotBlank String uf,
String complemento,
String numero
```

Por fim precisamos adicionar a anotação `@Valid` no parâmetro do método cadastrar no *MedicoController*.

```java
public void cadastrarMedico(@RequestBody @Valid DadosCadastroMedico dados){}
```

## Adicionando novo campo

Digamos que houve uma alteração no projeto e agora passa ser obrigatório o campo telefone para o cadastro de médico, veremos então como fazer essas modificações.

Começaremos com o DTO *DadosCadastroMedico*, bastando apenas adicionar o campo `String telefone` com a anotação `@NotBlank`. Em seguida alteramos a entidade JPA *Medico*, adicionando a propriedade telefone `private String telefone;`, e alterando o construtor `this.telefone = dados.telefone();`. Apos isso iremos criar uma nova migration para alterar a tabela *medicos* no banco de dados, com o nome `V2__alter-table-medicos-add-column-telefone`, com o código:

```sql
alter table medicos add telefone varchar(20) not null;
```

Pronto, com algumas poucas modificações já foi possível adicionar um novo campo com as validações necessárias.

## Listando médicos

O segundo método do CRUD que iremos adicionar é o `listarMedico`, e o verbo http sera o GET sendo a anotação `@GetMapping`. O método precisa retornar uma lista, porem qual sera o tipo de item dessa lista, o mais comum é pensar em retornar a entidade JPA, já que ela representa cada item da tabela no DB, porém não é recomendado trabalhar diretamente com a entidade JPA nos controllers, pode ocasionar alguns problemas de segurança, de flexibilidade, maior quantidade de dados sendo transferido, além de que nesse método não queremos exibir as informações de todos os campos. A solução é, sempre que houver uma entrada ou saída de dados, utilizaremos um **DTO**. O método completo ficaria:

```java
@GetMapping
public List<DadosListagemMedico> listarMedico() {
  return repository.findAll().stream().map(DadosListagemMedico::new).toList();
}
```

> O `::new` é a forma de chamar o construtor de uma classe, que é chamado de *referência de método*. Ela foi introduzida no java 8 junto com os recursos de Stream API.

Como dito anteriormente o repository do Spring ja possui alguns métodos mais básicos do CRUD, e o `findAll()` é um deles. Esse método retorna uma lista com todos os elementos do banco de dados (referente a entidade descrita no JpaRepository, que no caso é o *Medico*), então utilizamos o `stream()` para realizar operações de maneira encadeada, em seguida o `map()` para executar alguma função com cada item da lista *stream*, e dentro do map convertemos todas as entidades *Medico* para *DadosListagemMedico*, que é o DTO que contem apenas os campos que julgamos necessario. Quando o método retorna um DTO o spring entende que os dados devem ser convertidos para JSON antes de serem enviados na resposta.

O proximo passo é criar o DTO `DadosListagemMedico`, ele sera uma classe record e além disso deve conter um construtor que recebe a entidade Medico. Isso se deve por que no método acima, ele é chamado através do `DadosListagemMedico::new`.

```java
public record DadosListagemMedico(String nome, String email, String crm, Especialidade especialidade) {
  public DadosListagemMedico(Medico medico) {
    this(medico.getNome(), medico.getEmail(), medico.getCrm(), medico.getEspecialidade());
  }
}
```

### Paginação e ordenação

Um dos requisitos para o método *listarMedico*, é que ele seja ordenado pelo nome do médico, e que seja retornado apenas 10 itens na lista, e no momento o método esta retornando todos os médicos do DB em ordem de inserção. Esse problema é facilmente resolvido com o interface `Pageable`, onde iremos colocar ela como parâmetro do método *listarMedico*.

```java
public Page<DadosListagemMedico> listar(Pageable paginacao) {
  return repository.findAll(paginacao).map(DadosListagemMedico::new);
}
```

O método precisou sofrer algumas alterações, sendo elas a mudança do retorno de `List` para `Page`, a sobrecarga de método no `findAll`, que nessa sobrecarga já possui um *map* interno e com isso não precisamos mais do `stream()`, bastando apenas chamar o map com o DTO *DadosListagemMedico* para converter a entidade, além de que agora como estamos retornando um objeto do tipo *Page*, não precisamos do `toList()` no final.

O *Pageable* é uma ótima ferramenta para se utilizar em buscas no banco de dados, pois por padrão ele limita a busca em apenas 20 itens, poupando muitos dados do serviço de database. Imagine uma lista com 1000 linhas, retornar esta lista completa em apenas uma requisição não é algo muito eficiente. Porém os requisitos para o método são de 10 itens, então como informamos qual a configuração de paginação desejamos?

Uma das maneiras mais simples é passando um parâmetro na url de requisição, sendo o `size` o tamanho da lista, `page` a pagina desejada e `sort` para a ordenação de acordo com o campo desejado. Temos o exemplo com a url da api local `http://localhost:8080/medicos?size=2&page=3&sort=nome`, onde são apenas 2 itens por pagina, a primeira atual é a terceira e esta sendo ordenado pelo nome.

> Uma maneira fácil de entender o *size* e o *page* é por exemplo com uma lista de 20 itens, caso o *size* seja 5, teremos um total de 4 paginas, e caso o *size* seja 10, teremos apenas 2 paginas.

Outra forma de mudar a paginação é definindo um padrão no parâmetro do método, e esse é a forma que iremos utilizar. Para isso utilizaremos a anotação `@PageableDefault` informando o *size* e o *sort*.

```java
public Page<DadosListagemMedico> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){}
```

Lembrando que o nome do campo no `sort` deve ser o mesmo da entidade JPA que estamos trabalhando, no caso a Medico.

## Atualizando as informações do médico

De acordo com a clinica, somente os campos de **nome**, **telefone** e **endereço** podem ser alterados, os demais campos não podem. Além das informações dos campos que serão alterados, precisamos do **id** do médico para efetuar a alteração, porem o nosso método para listar médico não esta retornando o id, o que impediria um front-end de escolher qual médico sera alterado. Começaremos então alterando o método listar.

As alterações em um DTO são feitas com muita facilidade, no nosso caso basta adicionar o campo na classe e no construtor, sendo o campo id do tipo `Long`. Com apenas isso o método *listar* ja esta retornando o id.

Agora para criar o método de atualizar, teremos algo muito semelhante ao cadastrar, vamos receber algumas informações pelo corpo da requisição, precisamos de um DTO especifico para a atualização, iremos adicionar novas informações no banco de dados e também precisamos da anotação `@Transactional`, ja que sera feito alterações no DB.

```java
@PutMapping
@Transactional
public void atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados) {
  var medico = repository.getReferenceById(dados.id());
  medico.atualizarInformacoes(dados);
}
```

Recebemos os dados do médico vindo pelo corpo da requisição com o `@RequestBody`, e utilizamos o DTO `DadosAtualizacaoMedico` para validar os dados, então utilizamos o *repository* do JPA com o método `getReferenceById` que retornar a entidade que contem o ID informado, então armazenamos a entidade na variável *medico*, e chamamos o método `atualizarInformacoes` passando o DTO como parâmetro. Com apenas isso o método esta pronto.

Mas fica a pergunta, como fazemos para salvar essa nova entidade no banco de dados? Não é necessario. Quando é aberto uma transação, o próprio Spring Boot identifica que uma entidade foi carregada a alguns atributos dessa entidade alterados, e ao final da transação ele automaticamente faz o update.

Claro, para que o método funcione precisamos criar o DTO, o método *atualizarInformacoes* na entidade *Medico*, e mesmo para a classe  *Endereco*.

### DTO atualizar medico

```java
public record DadosAtualizacaoMedico(
  @NotNull Long id,
  String nome,
  String telefone,
  DadosEndereco endereco) {
    }
```

### Método atualizar informações médico

```java
public void atualizarInformacoes(DadosAtualizacaoMedico dados) {
  if (dados.nome() != null) {
    this.nome = dados.nome();
  }
  if (dados.telefone() != null) {
    this.telefone = dados.telefone();
  }
  if (dados.endereco() != null) {
    this.endereco.atualizarInformacoes(dados.endereco());
  }
}
```

### Método atualizar informações endereço

```java
public void atualizarInformacoes(DadosEndereco dados) {
  if (dados.logradouro() != null) {
    this.logradouro = dados.logradouro();
  }
  if (dados.bairro() != null) {
    this.bairro = dados.bairro();
  }
  if (dados.cep() != null) {
    this.cep = dados.cep();
  }
  if (dados.uf() != null) {
    this.uf = dados.uf();
  }
  if (dados.cidade() != null) {
    this.cidade = dados.cidade();
  }
  if (dados.numero() != null) {
    this.numero = dados.numero();
  }
  if (dados.complemento() != null) {
    this.complemento = dados.complemento();
  }
}
```

## Sub rotas

Antes de seguirmos para o próximo método, vamos entender um pouco como criar um rota dentro de uma rota principal. Até o momento adicionamos algumas anotações como `@GetMapping` ou `@PostMapping` para identificar o verbo do método, porem também podemos passar como parâmetro uma sub rota. Por exemplo o controller abaixo:

```java
@RestController
@RequestMapping("filmes")
public class FilmesController {
  @GetMapping("/comedia")
  public Page<DadosListagemFilme> listarFilmesComedia(Pageable paginacao) { ... }

  @GetMapping("/acao")
  public Page<DadosListagemFilme> listarFilmesAcao(Pageable paginacao) { ... }

  @GetMapping("/suspense")
  public Page<DadosListagemFilme> listarFilmesSuspense(Pageable paginacao) { ... }

  @GetMapping("/nome/{nome}")
  public Page<DadosListagemFilme> procurarFilmePorNome(@PathVariable String nome) { ... }
}
```

Com as sub rotas podemos ter vários métodos com o mesmo verbo, porem cada um apontando para uma rota diferente, todos pertencendo a uma mesma rota principal. Além também de ser possível passar parâmetros dinâmicos através da rota, como feito em `/{nome}`, utilizamos as chaves "{ }" para informar ao método que sera recebido informações através da rota. Lembrando que é preciso fazer a anotação `@PathVariable` para que o Spring atribua as informações no parâmetro.

## Exclusão logica

Vamos adicionar uma funcionalidade para excluir o médico, porem não vamos realmente remover o médico do banco de dados, vamos apenas marca-lo como desativado, e isso é chamado de **Exclusão Logica**. Excluir de fato uma entidade no banco de dados pode ocasionar alguns problemas, principalmente se ela possuir relações com outras informações. Por exemplo no caso da clinica fictícia Voll Med, muito provavelmente ela ira armazenar o histórico do médico, e ela não poderia excluir essas informações até mesmo por questões de contrato.

Então vamos começar por criar o método `excluirMedico`, ele ira receber apenas a informação do ID do médico. Porem não vamos enviar essa informação pelo corpo da requisição, e sim diretamente pela URL. Por exemplo para excluir o médico de ID 5 vamos utilizar a URL `http://localhost:8080/medicos/5`. Para capturar essa informação iremos criar uma nova rota com um parâmetro dinâmico, isso diretamente na anotação do verbo do método.

```java
@DeleteMapping("/{id}")
@Transactional
public void excluirMedico(@PathVariable Long id) {
  var medico = repository.getReferenceById(id);
  medico.excluir();
}
```

Para criar o parâmetro dinâmico utilizamos as chaves com um nome no meio, ficando então `/{id}`, com isso e com a anotação `@PathVariable` qualquer informação que chegar na rota `/medicos/alguma_coisa` sera atribuído na variável *id* (obviamente se for possível converter para o tipo do parâmetro no método). Após isso chamamos o método `getReferenceById` do repository para carregar a entidade, e então executamos o método `excluir` da própria entidade. Lembrando novamente que apos abrir uma transação e alterar algum atributo de uma entidade, o Spring automaticamente faz o update no banco de dados.

Agora resta criar a propriedade `ativo` na entidade *Medico* e no banco de dados, além de também adicionar o método `excluir`. Ja no método *listar*, sera preciso mudar para retornar apenas os médicos ativos.

### Adicionando campo *ativo* e o método *excluir* na entidade Medico

```java
public class Medico {
  // código omitido
  private Boolean ativo;

  public Medico(DadosCadastroMedico dados) {
    this.ativo = true;
    // código omitido
  }
  public void excluir() {
    this.ativo = false;
  }
}
```

Sempre ao criar um médico ele estará como ativo, então não tem por que adicionar esse campo no DTO.

### Nova migration para o campo *ativo*

```sql
ALTER TABLE medicos ADD ativo TINYINT;
UPDATE medicos SET ativo = 1;
ALTER TABLE medicos CHANGE ativo ativo TINYINT NOT NULL;
```

O campo *ativo* não pode ser nulo, porem ao adicionar uma nova coluna em uma tabela existente, todos as linhas já existentes ficaram como nulo neste campo, por isso na segunda linha utilizamos o `UPDATE` para alterar todas as colunas para TRUE. O `CHANGE` do MySQL permite alterar diversas informações de uma coluna da tabela, e um delas é o nome, por isso colocamos 2 vezes o mesmo nome `ativo ativo`, o primeiro é o nome antigo e o segundo o novo nome, logo se não é preciso alterar o nome, repetimos ele. Por fim adicionamos a constraint `NOT NULL`.

### Método *listar* exibe apenas médicos ativos

Agora no método listar, vamos retornar apenas os médicos que tiverem o ativo igual a TRUE. Para resolver isso sem criar consultas personalizadas, podemos utilizar as **consultas derivadas**, que com apenas o nome do método cria uma consulta especifica, e apesar de ser simples de entender, existem diversas formar de montar a consulta. O artigo que vou deixar o link explica alguns casos e como montar a consulta [Derived Query Methods](https://www.baeldung.com/spring-data-derived-queries).

```java
return repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
```

Como este não é um método padrão do repository, vamos precisar adicionar ele na classe `MedicoRepository`.

```java
Page<Medico> findAllByAtivoTrue(Pageable paginacao);
```

É necessario apenas assinar o método, o Spring identifica o nome e cria a query. O próprio nome ja é sugestivo, mas o que ele esta buscando são todos as entidades que possuem a coluna `ativo` marcado como TRUE.

## Conclusão

Por fim todos os métodos do CRUD foram aplicados, e concluímos as tarefas para o Curso 1. Também era necessario criar todos os mesmos métodos para a entidade `Paciente`, mas como ela é basicamente igual a de `Medico`, não sera necessario explicar ponto a ponto como foi feito.
