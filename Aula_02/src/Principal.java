import com.cesar.aula02.bingeWatch.TotalTimeCalculator;
import com.cesar.aula02.filters.Recommendation;
import com.cesar.aula02.model.Episode;
import com.cesar.aula02.model.Movie;
import com.cesar.aula02.model.Series;

import java.text.DecimalFormat;

public class Principal {
  public static void main(String[] args) {
    DecimalFormat df = new DecimalFormat("#.0");
    
    Movie interestelar = new Movie();
    interestelar.setName("Interestelar");
    interestelar.setReleaseDate(2014);
    interestelar.setDuration(169);
    interestelar.showInfo();
    System.out.println("Duração filme em minutos: "+ interestelar.getDuration());
    
    interestelar.rate(8);
    interestelar.rate(7.5);
    interestelar.rate(8.2);
    System.out.println("Avaliação do filme: "+df.format(interestelar.average()));

    Series RickMorty = new Series();
    RickMorty.setName("Rick and Morty");
    RickMorty.setMinutesEpisode(22);
    RickMorty.setCompleteSeries(false);
    RickMorty.setEpisodesPerSeason(10);
    RickMorty.setNumberOfSeasons(6);
    System.out.println("\nDuração da serie: "+ (RickMorty.getDuration() / 60)+ " horas");
    
    Movie matrix = new Movie();
    matrix.setName("Matrix");
    matrix.setReleaseDate(1999);
    matrix.setDuration(136);
    
    TotalTimeCalculator calculator = new TotalTimeCalculator();
    calculator.include(interestelar);
    calculator.include(matrix);
    calculator.include(RickMorty);
//    System.out.println("\nTempo total para maratonar: "+df.format(calculator.getTotalTime() / 60.0)+" horas");

    Recommendation filter = new Recommendation();
    filter.filter(interestelar);

    Episode episode = new Episode();
    episode.setNumber(1);
    episode.setSeries(RickMorty);
    episode.setNumberOfViews(1200);
    filter.filter(episode);
  }
}