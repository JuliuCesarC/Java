import java.util.Scanner;

public class DesafioAula01 {
  public static Scanner input = new Scanner(System.in);
  public static void main(String[] args) {
    String name = "";
    double balance = 2500;

    System.out.println("Digite um nome: ");
    name = input.nextLine();

    System.out.println("-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_");
    System.out.println("Dados do cliente\n");
    System.out.println("Nome: \t\t\t\t"+name);
    System.out.println("Tipo da conta: \t\tCorrente");
    System.out.println("Saldo inicial: \t\tR$"+balance);
    System.out.println("-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_\n");

    printOptions();
    int option = 0;
    double inputOption = 0;
    while (option != 4){
      System.out.println("Digite a opção desejada ou 5 para repetir as opções:");
      inputOption = input.nextDouble();
      if (inputOption == 1){
        System.out.println("Saldo em conta: \tR$"+balance);
      }
      if (inputOption == 2){
        balance = cashOut(balance);
      }
      if (inputOption == 3){
        balance = deposit(balance);
      }
      if (inputOption == 4){
        option =(int) inputOption;
      }
      if (inputOption == 5){
        printOptions();
      }
      
      if (inputOption <= 0 || inputOption > 5 || inputOption != (int) inputOption){
        System.out.println("ERRO: opção invalida.");
      }
    }
  }

  public static void printOptions() {
    System.out.println("""
        Operações\n
        1 - Consultar saldo
        2 - Sacar
        3 - Depositar
        4 - Sair\n
        """);
  }

  public static double cashOut(double balance) {
    while (true){
      System.out.println("Digite um valor ou aperte 0 para sair: ");
      double amount = input.nextDouble();
      if (balance >= amount && amount >= 0){
        double newBalance = balance - amount;
        System.out.println("Saque efetuado com sucesso, saldo: \tR$"+newBalance);
        return newBalance;
      } else {
        System.out.println("Saldo insuficiente.");
      }
    }
  }
  public static double deposit(double balance) {
    while (true){
      System.out.println("Digite um valor ou aperte 0 para sair: ");
      double amount = input.nextDouble();
      if (balance >= amount && amount >= 0){
        double newBalance = balance + amount;
        System.out.println("Deposito efetuado com sucesso, saldo: \tR$"+newBalance);
        return newBalance;
      } else {
        System.out.println("Digite um valor valido.");
      }
    }
  }
  
}
