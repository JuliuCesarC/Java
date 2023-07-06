import com.cesar.aula03.account.CreditCard;

import java.util.Scanner;

public class Main {
  public static Scanner input = new Scanner(System.in);

  public static void main(String[] args) {

    System.out.println("Digite o limite do cartão: ");
    CreditCard card = new CreditCard(input.nextDouble());

    int shopping = 1;
    while (shopping == 1) {
      String item;
      double value;
      input.nextLine();
      System.out.println("Insira o nome do item: ");
      item = input.nextLine();
      System.out.println("Digite o valor do item: ");
      value = input.nextDouble();

      if (card.buy(item, value) == false) {
        shopping = 0;
      } else {
        System.out.println("Deseja comprar mais algum item? 1:sim ; 0:não.");
        shopping = input.nextInt();
      }
    }
    
    double purchaseAmount = 0;
    System.out.println("\n-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_\n");
    System.out.println("Compras \t\t\t Valor\n");
    for (Object item : card.getSortedPurchases().keySet()) {
      System.out.println(item + ": \t\tR$ " + card.getSortedPurchases().get(item));
      purchaseAmount += (double) card.getSortedPurchases().get(item);
    }
    System.out.println("\nTotal: \t\t\tR$ " + purchaseAmount);
    System.out.println("\nLimite disponivel: \t\t\tR$ " + card.getBalance());
    System.out.println("\n-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_");

  }
}