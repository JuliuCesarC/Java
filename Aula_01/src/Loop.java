import java.util.Scanner;
public class Loop {
  public static void main(String[] args) {
    Scanner read = new Scanner(System.in);
    double rating = 0;
    double average = 0;

    for (int i = 0; i < 3 ; i++) {
      System.out.println("Digite sua avaliação para o filme: ");
      rating = read.nextDouble();
      average += rating;
    }
    System.out.println("Media das avaliações: "+ average / 3);
  }
}
