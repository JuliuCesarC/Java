package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.domain.cliente.Cliente;
import br.com.alura.bytebank.domain.cliente.DadosCadastroCliente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ContaDAO {
	private Connection conn;

	public ContaDAO(Connection connection) {
		this.conn = connection;
	}

	public void salvar(DadosAberturaConta dadosDaConta) {
		var cliente = new Cliente(dadosDaConta.dadosCliente());
		var conta = new Conta(dadosDaConta.numero(), BigDecimal.ZERO, cliente, true);

		String sql = "INSERT INTO conta(numero,saldo,cliente_nome,cliente_cpf,cliente_email, conta_ativa)"
				+ "VALUES(?,?,?,?,?,?)";

		try {
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setInt(1, conta.getNumero());
			ps.setBigDecimal(2, BigDecimal.ZERO);
			ps.setString(3, dadosDaConta.dadosCliente().nome());
			ps.setString(4, dadosDaConta.dadosCliente().cpf());
			ps.setString(5, dadosDaConta.dadosCliente().email());
			ps.setBoolean(6, true);

			ps.execute();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Set<Conta> listar() {
		Set<Conta> contas = new HashSet<>();
		PreparedStatement ps;
		ResultSet rs;
		String sql = "SELECT * FROM conta WHERE conta_ativa = true";
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				contas.add(getConta(rs));
			}
			ps.close();
			rs.close();
			conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return contas;
	}

	public Conta listarPorNumero(Integer numeroBusca) {
		Conta conta;
		PreparedStatement ps;
		ResultSet rs;
		String sql = "SELECT * FROM conta WHERE numero = ?";

		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, numeroBusca);
			rs = ps.executeQuery();
			rs.next();
			conta = getConta(rs);
			ps.execute();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return conta;
	}

	public Set<Conta> listarDesativadas() {
		Set<Conta> contas = new HashSet<>();
		PreparedStatement ps;
		ResultSet rs;
		String sql = "SELECT * FROM conta WHERE conta_ativa = false;";
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				contas.add(getConta(rs));
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return contas;
	}

	public void depositar(Conta conta, BigDecimal valor) {
		PreparedStatement ps;
		String sql = "UPDATE conta SET saldo = ? WHERE numero = ?;";

		try {
//      Define a transação como manual, ou seja, o programa que estará responsável pelo rollback, e não o mysql.
			conn.setAutoCommit(false);
			BigDecimal newSaldo = conta.getSaldo().add(valor);
			ps = conn.prepareStatement(sql);

			ps.setBigDecimal(1, newSaldo);
			ps.setInt(2, conta.getNumero());
			ps.execute();
			conn.commit();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
			throw new RuntimeException(e);
		}
	}

	public void sacar(Conta conta, Integer numero, BigDecimal valor) {
		PreparedStatement ps;
		String sqlUp = "UPDATE conta SET saldo = ? WHERE numero = ?";

		try {
			conn.setAutoCommit(false);
			BigDecimal prevSaldo = conta.getSaldo();
			BigDecimal newSaldo = prevSaldo.subtract(valor);

			ps = conn.prepareStatement(sqlUp);
			ps.setBigDecimal(1, newSaldo);
			ps.setInt(2, numero);
			ps.execute();
			conn.commit();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
			throw new RuntimeException(e);
		}
	}

	public void deletar(Integer numeroConta) {
		String sql = "DELETE FROM conta WHERE numero = ?";
		try {
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, numeroConta);

			ps.execute();
			conn.commit();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
			throw new RuntimeException(e);
		}
	}

	public void desativarConta(Integer numero) {
		String sql = "UPDATE conta SET conta_ativa = false WHERE numero = ?";
		try {
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, numero);

			ps.execute();
			conn.commit();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
			throw new RuntimeException(e);
		}
	}

	private Conta getConta(ResultSet rs) {
		Integer numero;
		BigDecimal saldo;
		String nome;
		String cpf;
		String email;
		Boolean ativa;
		try {
			numero = rs.getInt(1);
			saldo = rs.getBigDecimal(2);
			nome = rs.getString(3);
			cpf = rs.getString(4);
			email = rs.getString(5);
			ativa = rs.getBoolean(6);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		DadosCadastroCliente dadosCadastroCliente = new DadosCadastroCliente(nome, cpf, email);
		Cliente cliente = new Cliente(dadosCadastroCliente);
		return new Conta(numero, saldo, cliente, ativa);
	}
}
