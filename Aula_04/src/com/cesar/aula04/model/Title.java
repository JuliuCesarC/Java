package com.cesar.aula04.model;

import com.cesar.aula04.CustomException.ConvertYearException;
import com.google.gson.annotations.SerializedName;

public class Title implements Comparable<Title> {
//  @SerializedName("Title")
  private String name;
//  @SerializedName("Year")
  private int releaseDate;
  private boolean premiumPlan;
  private int duration;
  private double rating;
  private int totalRating;

  public Title(String name, int releaseDate) {
    this.name = name;
    this.releaseDate = releaseDate;
  }

  public Title(TitleOMDb recordClass) {
    this.name = recordClass.title();
    if (recordClass.year().length() > 4){
//     ---------- Criando uma exceção personalizada ----------
//      Para este projeto, não necessariamente seria indicado criar uma exceção para o problema de conversão de 'ano de lançamento', é apenas um exemplo de como criar uma exceção para um erro especifico.
      throw new ConvertYearException("Não foi possivel converter o ano de lançamento. Texto maior que 4 digitos.");
    }
    this.releaseDate = Integer.valueOf(recordClass.year());
    this.duration = Integer.valueOf(recordClass.runtime().substring(0,2));
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getReleaseDate() {
    return releaseDate;
  }

  public boolean isPremiumPlan() {
    return premiumPlan;
  }

  public int getDuration() {
    return duration;
  }

  public int getTotalRating() {
    return totalRating;
  }


  public void setReleaseDate(int releaseDate) {
    this.releaseDate = releaseDate;
  }

  public void setPremiumPlan(boolean premiumPlan) {
    this.premiumPlan = premiumPlan;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public void showInfo() {
    System.out.println("Nome do filme: " + name);
    System.out.println("Ano de lançamento: " + releaseDate);
  }

  public void rate(double grade) {
    rating += grade;
    totalRating++;
  }

  public double average() {
    return rating / totalRating;
  }

  @Override
  public int compareTo(Title nextName) {
    return this.getName().compareTo(nextName.getName());
  }

  @Override
  public String toString() {
    return "Nome: " + name + " (" + releaseDate + "), duração: "+duration+" min";
  }
}
