package com.cesar.aula02.filters;

public class Recommendation {
  
  public void filter(Classifiable classifiable){
    if (classifiable.getClassification() >= 4){
      System.out.println("Melhores avaliados");
    } else if (classifiable.getClassification() >= 2) {
      System.out.println("Bem avaliado no momento");
    }else {
      System.out.println("Adicione a lista para assistir depois");
    }
  }
  
}
