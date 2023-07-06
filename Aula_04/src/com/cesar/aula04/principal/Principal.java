package com.cesar.aula04.principal;

import com.cesar.aula04.CustomException.ConvertYearException;
import com.cesar.aula04.model.Title;
import com.cesar.aula04.model.TitleOMDb;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {
  public static void main(String[] args) throws IOException, InterruptedException {
    Scanner input = new Scanner(System.in);
    String search = "";
    List<Title> titleList = new ArrayList<>();

//    Por padrão o gson escreve o json em apenas uma linha, o que não é muito legivel, podemos então utilizar o "setPrettyPrinting" que ira escrever cada propriedade em uma linha separada.
    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).setPrettyPrinting().create();

    while (!search.equalsIgnoreCase("sair")) {
      System.out.println("Digite o nome do filme: ");
      search = input.nextLine();

      if (search.equalsIgnoreCase("sair")) {
        break;
      }

      String url = "http://www.omdbapi.com/?t=" + search.replaceAll(" ", "+") + "&apikey=8348a285";

      try {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(url))
          .build();
        HttpResponse<String> response = client
          .send(request, HttpResponse.BodyHandlers.ofString());

        String json = response.body();
        System.out.println(json);

//    Gson gson = new Gson();
//    Title searchedMovie = gson.fromJson(json, Title.class);        

        TitleOMDb recordJson = gson.fromJson(json, TitleOMDb.class);
        Title searchedMovie = new Title(recordJson);
        System.out.println("\n" + searchedMovie);

//      FileWriter writer = new FileWriter("movies.txt");
//      writer.write(searchedMovie.toString());
//      writer.close();
        titleList.add(searchedMovie);

      } catch (NumberFormatException e) {
        System.out.println("Ocorreu um erro: ");
        System.out.println(e.getMessage());
      } catch (IllegalArgumentException e) {
        System.out.println("Erro de argumento, verifique o texto de pesquisa (url).");
      } catch (ConvertYearException e) {
        System.out.println(e.getMessage());
      }
    }
    FileWriter writer = new FileWriter("movies.json");
    writer.write(gson.toJson(titleList));
    writer.close();
    System.out.println("final do programa");
  }
}