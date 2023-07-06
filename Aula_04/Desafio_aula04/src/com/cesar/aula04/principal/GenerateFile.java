package com.cesar.aula04.principal;

import com.cesar.aula04.model.Address;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;

public class GenerateFile {
  public void writeFile(Address address) throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    FileWriter writer = new FileWriter("endereco.json");
    writer.write(gson.toJson(address));
    writer.close();
  }
}
