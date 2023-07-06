package com.desafio.model;

import java.text.DecimalFormat;

public class Audio {
  private String title;
  private double durationMinutes;
  private int totalPlays;
  private double rating;
  private int totalRating;

  public String getTitle() {
    return title;
  }


  public void setTitle(String title) {
    this.title = title;
  }

  public double getDurationMinutes() {
    return durationMinutes;
  }

  public void setDurationMinutes(double durationMinutes) {
    this.durationMinutes = durationMinutes;
  }

  public int getTotalPlays() {
    return totalPlays;
  }
  public void play(){
    totalPlays ++;
  }
  public int getTotalRating() {
    return totalRating;
  }
  public void rate(double rating){
    this.rating += rating;
    this.totalRating++;
  }
  public String average(){
    DecimalFormat df = new DecimalFormat("#.0");
    return df.format(rating / totalRating);
  }
}
