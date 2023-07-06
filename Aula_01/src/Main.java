public class Main {
  public static void main(String[] args) {
    System.out.println("Hello World!");
    System.out.println("Movie: Interestelar");
    
    int year = 2014;
    System.out.println("Ano de lançamento: " + year);
    
    double average = (9.8 + 9.3 + 8.0) /3;
    System.out.println("Media: "+ average);

    String text = """
        Filme Interestelar
        Extrema qualidade.
      """;
    String saudacao = "Olá, meu nome é ";
    String nome = "Alice ";
    String continuacao = "e minha idade é ";
    int idade = 17;
    System.out.println(saudacao + nome + continuacao + idade);
  }
}