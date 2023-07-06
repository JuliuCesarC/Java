public class Principal {
  public static void main(String[] args) {    

    Music ceuAzul = new Music();
    ceuAzul.setAlbum("Música Popular Caiçara ao Vivo");
    ceuAzul.setBandName("Charlie Brown Jr.");
    ceuAzul.setTitle("Céu Azul");
    ceuAzul.setDurationMinutes(3.17);
    ceuAzul.setGenre("Rock");

    for (int i = 0; i < 2143; i++) {
      double randomNum = Math.random() * 10;
      if (randomNum < 5) {
        randomNum += 2;
      }
      ceuAzul.play();
      ceuAzul.rate(randomNum);
    }
    ceuAzul.showInfo();
    
    PodCast culpaDoCerebro = new PodCast();
    culpaDoCerebro.setTitle("Culpa do Cérebro");
    culpaDoCerebro.setPodcastEpisodeName("Aumente seu FOCO, DISPOSIÇÃO E APRENDIZADO");
    culpaDoCerebro.setPodcastNumber(1);
    culpaDoCerebro.setDurationMinutes(74);
    culpaDoCerebro.setDescription("Nesse episódio, falo sobre 3 pequenos ajustes que podemos fazer na nossa rotina, super fáceis de serem implementados, mas que possuem um enorme impacto na nossa disposição, capacidade de concentração e aprendizado.");

    for (int i = 0; i < 1017; i++) {
      double randomNum = Math.random() * 10;
      if (randomNum < 5) {
        randomNum += 1;
      }
      culpaDoCerebro.play();
      culpaDoCerebro.rate(randomNum);
    }
    culpaDoCerebro.showInfo();
  }
}