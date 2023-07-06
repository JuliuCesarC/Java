package com.cesar.aula03.Principal;

import com.cesar.aula03.model.Movie;
import com.cesar.aula03.model.Series;
import com.cesar.aula03.model.Title;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PrincipalList {
  public static void main(String[] args) {
    Movie interestelar = new Movie("Interestelar", 2014);
    interestelar.rate(9);
    Movie feriasFrustradas = new Movie("Férias Frustradas", 2015);
    feriasFrustradas.rate(10);
    Movie matrix = new Movie("Matrix", 1999);
    matrix.rate(9);
    Series RickMorty = new Series("Rick and Morty", 2013);
    RickMorty.rate(9.5);

    ArrayList<Title> movieList = new ArrayList<>();
//    Ao declarar que a lista 'movieList' trabalha com qualquer objeto que herde de 'Title', podemos então adicionar tanto um filme quanto uma serie. Porem quando varremos a lista, temos um problema caso seja necessario invocar um método especifico do filme ou da serie, veremos isso no 'for' abaixo.
    movieList.add(interestelar);
    movieList.add(feriasFrustradas);
    movieList.add(matrix);
    movieList.add(RickMorty);
    
//    -------------------- Varrendo uma lista --------------------
//    Iterando uma lista utilizando o forEach.
//    movieList.forEach(item -> System.out.println(item.getName()));
    
//    Iterando uma lista utilizando o for.
//    for (Title item: movieList){
//      System.out.println(item.getName());
//      Podemos acessar os métodos do filme utilizando o "casting", ou seja convertendo a propriedade "item" para filme "(Movie)". Mas ao chegar na vez da serie, retornara uma excessão, pois não é possivel converter uma serie em um filme.
//      Movie movie = (Movie) item;
//      System.out.println("Avaliação: "+ movie.getClassification());
//    }

//    Uma forma não muito recomandada, mas funcional para resolver este problema é criando uma validação com o "instanceof".
//    for (Title item : movieList) {
//      System.out.println(item.getName());
//      if(item instanceof Movie movie){
//        System.out.println("Avaliação: "+movie.getClassification());
//      }
//    }
//    -------------------- Odenando uma lista --------------------
    
//    Abaixo temos um exemplo de como ordenar uma lista. O método "sort" funciona bem com qualquer dado que ja possua a implementação do "compareTo()", como strings e numeros.
    List<String> actorsName = new ArrayList<>();
    actorsName.add("Jennifer Aniston");
    actorsName.add("Ed Helms");
    actorsName.add("Zach Galifianakis");
    actorsName.add("Seth MacFarlane");
    actorsName.add("Ryan Reynolds");
    
    System.out.println("Lista: "+actorsName);
    Collections.sort(actorsName);
    System.out.println("Lista ordenada: "+actorsName);

//    Porem nas classes que criamos, existem diversos campos e métodos, o que impede de utilizar o "sort" diretamente. Sera necessario implementar a interface "Comparable<>" e informar quais dados devem ser comparados. Iremos implementar na classe "Title", dessa forma, o "sort" funcionara tanto com filmes e series.
    System.out.println("\nAntes da ordenação: "+movieList);
    Collections.sort(movieList);
    System.out.println("Depois da ordenação: "+movieList);
    
//    Existe outra forma de ordenar passando uma comparação diretamente para o "sort". Com isso podemos ter outra forma de comparação sem interferir na comparação original da classe.
    
    movieList.sort(Comparator.comparing(Title::getReleaseDate));
    System.out.println("\nOrdenando por ano de lançamento: "+ movieList);
  }
}
