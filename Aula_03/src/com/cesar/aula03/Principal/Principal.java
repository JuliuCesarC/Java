package com.cesar.aula03.Principal;

import com.cesar.aula03.bingeWatch.TotalTimeCalculator;
import com.cesar.aula03.filters.Recommendation;
import com.cesar.aula03.model.Episode;
import com.cesar.aula03.model.Movie;
import com.cesar.aula03.model.Series;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Principal {
  public static void main(String[] args) {
    DecimalFormat df = new DecimalFormat("#.0");
    
    Movie interestelar = new Movie("Interestelar", 2014);
    interestelar.setDuration(169);
    interestelar.showInfo();
    System.out.println("Duração filme em minutos: "+ interestelar.getDuration());    
    interestelar.rate(8);
    interestelar.rate(7.5);
    interestelar.rate(8.2);
    System.out.println("Avaliação do filme: "+df.format(interestelar.average()));
    
//    TotalTimeCalculator calculator = new TotalTimeCalculator();
//    calculator.include(RickMorty);
//    System.out.println("\nTempo total para maratonar: "+df.format(calculator.getTotalTime() / 60.0)+" horas");
    
    Movie matrix = new Movie("Matrix", 1999);
    matrix.setDuration(136);
    
    Movie feriasFrustradas = new Movie("Férias Frustradas", 2015);
    feriasFrustradas.setDuration(99);
    
    Series RickMorty = new Series("Rick and Morty", 2013);
    RickMorty.setMinutesEpisode(22);
    RickMorty.setCompleteSeries(false);
    RickMorty.setEpisodesPerSeason(10);
    RickMorty.setNumberOfSeasons(6);
    System.out.println("\nDuração da serie: "+ (RickMorty.getDuration() / 60)+ " horas");
    Episode episodeRM = new Episode();
    episodeRM.setNumber(1);
    episodeRM.setSeries(RickMorty);
    episodeRM.setNumberOfViews(1200);
    
//    Recommendation filter = new Recommendation();
//    filter.filter(interestelar);
//    filter.filter(episodeRM);

    ArrayList<Movie> movieList = new ArrayList<>();
    movieList.add(interestelar);
    movieList.add(matrix);
    movieList.add(feriasFrustradas);
    System.out.println("\nTamanho do array: "+ movieList.size());
    System.out.println("Primeiro filme: "+ movieList.get(0).getName());

  }
}