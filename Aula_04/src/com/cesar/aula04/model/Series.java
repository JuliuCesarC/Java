package com.cesar.aula04.model;

public class Series extends Title {
  private int numberOfSeasons;
  private int episodesPerSeason;
  private boolean completeSeries;
  private int minutesEpisode;

  public Series(String name, int releaseDate) {
    super(name, releaseDate);
  }

  public int getNumberOfSeasons() {
    return numberOfSeasons;
  }

  public void setNumberOfSeasons(int numberOfSeasons) {
    this.numberOfSeasons = numberOfSeasons;
  }

  public int getEpisodesPerSeason() {
    return episodesPerSeason;
  }

  public void setEpisodesPerSeason(int episodesPerSeason) {
    this.episodesPerSeason = episodesPerSeason;
  }

  public boolean isCompleteSeries() {
    return completeSeries;
  }

  public void setCompleteSeries(boolean completeSeries) {
    this.completeSeries = completeSeries;
  }

  public int getMinutesEpisode() {
    return minutesEpisode;
  }

  public void setMinutesEpisode(int minutesEpisode) {
    this.minutesEpisode = minutesEpisode;
  }

  @Override
  public int getDuration() {
    return numberOfSeasons * episodesPerSeason * minutesEpisode;
  }

  @Override
  public String toString() {
    return "Serie: "+this.getName()+" ("+this.getReleaseDate()+")";
  }
}
