package app;

public class Pagamentos {
    private double saldo;

    public Pagamentos(double saldoInicial) {
        this.saldo = saldoInicial;
    }

    public double Conta() {
        return this.saldo;
    }

    public boolean Deposito(double valor, String nome) {
        if (valor > 0) {
            this.saldo += valor;
            System.out.println("Pagamento de: R$ " + valor + "\nRecebido de: " + nome);
            return true;
        } else {
            System.out.printf("Não foi possivel receber o pagamento");
        }
        return false;
    }
    public boolean Saque(double valor) {
        if (valor > 0 && valor <= this.saldo) {
            this.saldo -= valor;
            System.out.println("Saque de: R$ " + valor + "\nRealizado com Sucesso!");
            return true;
        } else {
            System.out.println("Saque invalido");
            return false;
        }
    }
    public boolean SaqueInv(double valor) {
        if (valor > 0 ) {
            this.saldo -= valor;
            return true;
        } else {
            return false;
        }
    }
    public boolean DepositoInv(double valor) {
        if (valor > 0 ) {
            this.saldo += valor;
            return true;
        } else {
            return false;
        }
    }
}
