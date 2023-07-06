package com.cesar.aula04.model;

import com.cesar.aula04.filters.Classifiable;

public class Episode implements Classifiable {
  private int number;
  private String name;
  private Series series;
  private int numberOfViews;

  public int getNumberOfViews() {
    return numberOfViews;
  }

  public void setNumberOfViews(int numberOfViews) {
    this.numberOfViews = numberOfViews;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Series getSeries() {
    return series;
  }

  public void setSeries(Series series) {
    this.series = series;
  }

  @Override
  public int getClassification() {
    if (numberOfViews > 1000){
      return 4;
    }else {
      return 2;
    }
  }
}
