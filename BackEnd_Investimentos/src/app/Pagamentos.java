package app;

public class Pagamentos {
    private int saldo;

    public Pagamentos(int saldoInicial) {
        this.saldo = saldoInicial;
    }

    public int Conta() {
        return this.saldo;
    }

    public boolean Deposito(int valor, String nome) {
        if (valor > 0) {
            this.saldo += valor;
            System.out.println("Pagamento de: R$ " + valor + "\nRecebido de: " + nome);
            return true;
        } else {
            System.out.printf("Não foi possivel receber o pagamento");
        }
        return false;
    }
    public boolean Saque(int valor) {
        if (valor > 0 && valor <= this.saldo) {
            this.saldo -= valor;
            // reescrever o .json com o novo saldo da conta do usuário
            System.out.println("Saque de: R$ " + valor + "\nRealizado com Sucesso!");
            return true;
        } else {
            System.out.println("Saque invalido");
            return false;
        }
    }
}
