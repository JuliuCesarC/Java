package com.cesar.aula02.model;

import com.cesar.aula02.filters.Classifiable;

public class Movie extends Title implements Classifiable {
  private String director;

  public String getDirector() {
    return director;
  }

  public void setDirector(String director) {
    this.director = director;
  }

  @Override
  public int getClassification() {
    return (int) average() / 2;
  }
}