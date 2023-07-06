package com.cesar.aula04.model;

import com.cesar.aula04.filters.Classifiable;

public class Movie extends Title implements Classifiable {
  private String director;

  public Movie(String name, int releaseDate) {
    super(name, releaseDate);
  }

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

  @Override
  public String toString() {
    return "Filme: "+this.getName()+" ("+this.getReleaseDate()+")";
  }
}