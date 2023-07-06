package com.cesar.aula04.principal;

import com.cesar.aula04.model.ViaCepAddress;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Search {
  public ViaCepAddress searchAddress(String cep) {
    URI url = URI.create("https://viacep.com.br/ws/" + cep.replaceAll("-", "".replace(" ", "")) + "/json/");
    System.out.println(url);

    HttpRequest req = HttpRequest.newBuilder()
      .uri(url)
      .build();

    try {
      HttpResponse<String> res = HttpClient.newHttpClient()
        .send(req, HttpResponse.BodyHandlers.ofString());
      return new Gson().fromJson(res.body(), ViaCepAddress.class);
    } catch (Exception e) {
      throw new RuntimeException("Não foi possivel encontrar as informações.");
    }
  }
}
