import com.desafio.model.Audio;
import com.desafio.model.Info;

public class PodCast extends Audio implements Info {
  private int podcastNumber;
  private String podcastEpisodeName;
  private String description;

  public int getPodcastNumber() {
    return podcastNumber;
  }

  public void setPodcastNumber(int podcastNumber) {
    this.podcastNumber = podcastNumber;
  }

  public String getPodcastEpisodeName() {
    return podcastEpisodeName;
  }

  public void setPodcastEpisodeName(String podcastEpisodeName) {
    this.podcastEpisodeName = podcastEpisodeName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public void showInfo() {
    System.out.println("\nNome do episódio: "+ podcastEpisodeName);
    System.out.println("Nome do podcast: "+ getTitle());
    System.out.println("Número do episódio: "+ podcastNumber);
    System.out.println("Duração: "+ getDurationMinutes()+" minutos");
    System.out.println("Descrição: "+ description);
    System.out.println("Avaliação: "+ average());
  }
}
