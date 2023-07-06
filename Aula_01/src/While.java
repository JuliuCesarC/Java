import java.util.Scanner;

public class While {
  public static void main(String[] args) {    
  Scanner read = new Scanner(System.in);
  double rating = 0;
  double average = 0;
  int count = 0;
  
  while(rating != -1){
    System.out.println("Digite sua avaliação para o filme ou -1 para encerrar: ");
    rating = read.nextDouble();
    if (rating != -1){
    average += rating;
    count++;
    }
  }
    System.out.println("Media das notas: "+average/count);
    
  }
}
