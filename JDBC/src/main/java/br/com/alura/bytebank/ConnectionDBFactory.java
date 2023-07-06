package br.com.alura.bytebank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDBFactory {
  public Connection getConnection() {
    try {
      String url = "jdbc:mysql://localhost:3306/byte_bank?user=Cesar&password="
        + Password.getPassword();
      return DriverManager.getConnection(url);
    } catch (SQLException err) {
      throw new RuntimeException(err);
    }
  }
  // O HikariCP cria uma lista de conexões com o banco de dados que ficam a
  // dispozição para serem utilizadas. Caso alguma conexão seja utilizada, ao
  // final do prosseço ela volta para a lista, sem a necessidade de fechar essa
  // conexão.
  // private HikariDataSource createDataSource(){
  // HikariConfig config = new HikariConfig();
  //
  // config.setJdbcUrl("jdbc:mysql://localhost:3306/byte_bank");
  // config.setUsername("Cesar");
  // config.setPassword(Password.getPassword());
  // config.setMaximumPoolSize(2);
  //
  // return new HikariDataSource(config);
  // }
}
