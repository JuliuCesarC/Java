package com.cesar.aula04.principal;

import com.cesar.aula04.model.Address;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class principal {
  public static void main(String[] args) throws IOException, InterruptedException {
    Scanner input = new Scanner(System.in);
    

    System.out.println("Pesquisar por CEP: ");
    String CEP = input.nextLine();

    Search search = new Search();
    try {
      Address address = new Address(search.searchAddress(CEP));
      GenerateFile generate = new GenerateFile();
      generate.writeFile(address);
    } catch (RuntimeException e) {
      System.out.println("Finalizando a aplicação.");
    }

  }
}