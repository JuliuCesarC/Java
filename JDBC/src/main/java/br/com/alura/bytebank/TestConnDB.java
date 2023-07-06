package br.com.alura.bytebank;

import java.sql.*;

public class TestConnDB {
	public static void main(String[] args) {
		ConnectionDBFactory ConnDBFactory = new ConnectionDBFactory();
		Connection connection = ConnDBFactory.getConnection();

		String sqlInsert = "INSERT INTO conta(numero,saldo,cliente_nome,cliente_cpf,cliente_email)"
				+ "VALUES(?,?,?,?,?)";
		String sqlSelect = "SELECT * FROM conta;";
		String sqlSelectP = "SELECT ? FROM conta;";

//    Utilizando o Statement para buscar os campos desejados na tabela.
//    Só é possivel selecionar os campos fixamente no codigo com o statemant.
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sqlSelect);

			while (resultSet.next()) {
				int numero = resultSet.getInt(1);
				String nome = resultSet.getString("cliente_nome");

				System.out.println("numero: " + numero + ", nome: " + nome);
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
