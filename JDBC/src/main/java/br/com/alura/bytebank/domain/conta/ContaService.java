package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.ConnectionDBFactory;
import br.com.alura.bytebank.domain.RegraDeNegocioException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Set;

public class ContaService {
	private ConnectionDBFactory connectionDB;

	public ContaService() {
		this.connectionDB = new ConnectionDBFactory();
	}

	public Set<Conta> listarContasAbertas() {
		Connection conn = connectionDB.getConnection();
		return new ContaDAO(conn).listar();
	}

	public Set<Conta> listarContasDesativas() {
		Connection conn = connectionDB.getConnection();
		return new ContaDAO(conn).listarDesativadas();
	}

	public BigDecimal consultarSaldo(Integer numeroDaConta) {
		var conta = buscarContaPorNumero(numeroDaConta);
		return conta.getSaldo();
	}

	public void abrir(DadosAberturaConta dadosDaConta) {
		Connection connection = connectionDB.getConnection();
		new ContaDAO(connection).salvar(dadosDaConta);
	}

	public void realizarSaque(Integer numeroDaConta, BigDecimal valor) {
		var conta = buscarContaPorNumero(numeroDaConta);
		if (valor.compareTo(BigDecimal.ZERO) <= 0) {
			throw new RegraDeNegocioException("Valor do saque deve ser superior a zero!");
		}
		if (valor.compareTo(conta.getSaldo()) > 0) {
			throw new RegraDeNegocioException("Saldo insuficiente!");
		}
		if (!conta.getContaAtiva()) {
			throw new RegraDeNegocioException("Conta inativa.");
		}
		Connection connection = connectionDB.getConnection();
		new ContaDAO(connection).sacar(conta, numeroDaConta, valor);
	}

	public void realizarDeposito(Integer numeroDaConta, BigDecimal valor) {
		var conta = buscarContaPorNumero(numeroDaConta);
		if (valor.compareTo(BigDecimal.ZERO) <= 0) {
			throw new RegraDeNegocioException("Valor do deposito deve ser superior a zero!");
		}
		if (!conta.getContaAtiva()) {
			throw new RegraDeNegocioException("Conta inativa.");
		}

		Connection conn = connectionDB.getConnection();
		new ContaDAO(conn).depositar(conta, valor);
	}

	public void realizarTransferencia(Integer numeroContaOrigem, Integer numeroContaDestino, BigDecimal valor) {
		try {
			this.realizarSaque(numeroContaOrigem, valor);
			this.realizarDeposito(numeroContaDestino, valor);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void encerrar(Integer numeroDaConta) {
		var conta = buscarContaPorNumero(numeroDaConta);
		if (conta.possuiSaldo()) {
			throw new RegraDeNegocioException("Conta não pode ser encerrada pois ainda possui saldo!");
		}

		Connection conn = connectionDB.getConnection();
		new ContaDAO(conn).deletar(numeroDaConta);
	}

	public void encerrarLogico(Integer numeroDaConta) {
		var conta = buscarContaPorNumero(numeroDaConta);
		if (conta.possuiSaldo()) {
			throw new RegraDeNegocioException("Conta não pode ser desativada pois ainda possui saldo!");
		}

		Connection conn = connectionDB.getConnection();
		new ContaDAO(conn).desativarConta(numeroDaConta);
	}

	private Conta buscarContaPorNumero(Integer numero) {
		Connection conn = connectionDB.getConnection();
		Conta conta = new ContaDAO(conn).listarPorNumero(numero);
		if (conta != null) {
			return conta;
		} else {
			throw new RegraDeNegocioException("Não existe conta cadastrada com esse numero.");
		}
	}
}
