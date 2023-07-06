import java.util.Scanner;

public class ReadCLI {
  public static void main(String[] args) {
    Scanner read = new Scanner(System.in);

    System.out.println("Digite um filme: ");
    String movie = read.nextLine();
    System.out.println("Ano de lançamento: ");
    int releaseDate = read.nextInt();
    System.out.println("Digite sua avaliação: ");
    double hating = read.nextDouble();

    System.out.println(movie);
    System.out.println(releaseDate);
    System.out.println(hating);
  }
}
