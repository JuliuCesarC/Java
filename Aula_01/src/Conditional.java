public class Conditional {
  public static void main(String[] args) {
    int releaseDate = 2014;
    boolean premium = true;
    String subscription = "plus";

    if(releaseDate >= 2008){
      System.out.println("Principais lançamentos.");
    }else{
      System.out.println("Filmes retrô que vale a pena assistir");
    }
    
    if(premium && subscription.equals("plus")){
      System.out.println("Assistir");
    }
    else{
      System.out.println("Não incluso no plano.");
    }
  }
}
