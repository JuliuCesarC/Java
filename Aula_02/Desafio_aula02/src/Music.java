import com.desafio.model.Audio;
import com.desafio.model.Info;

public class Music extends Audio implements Info {
  private String bandName;
  private String album;
  private String genre;

  public String getBandName() {
    return bandName;
  }

  public void setBandName(String bandName) {
    this.bandName = bandName;
  }

  public String getAlbum() {
    return album;
  }

  public void setAlbum(String album) {
    this.album = album;
  }
  
  public String getGenre() {
    return genre;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }

  @Override
  public void showInfo() {
    System.out.println("\nNome da musica: "+ getTitle());
    System.out.println("Banda: "+ bandName);
    System.out.println("Álbum: "+ album);
    System.out.println("Gênero musical: "+ genre);
    System.out.println("Escutada "+getTotalPlays()+" vezes.");
    System.out.println("Avaliação: "+ average());
  }
}
