package app;

import java.util.Random;

public class Investimentos {
    private double Carteira1 = 0;

    // Opções de investimento: nome, cotação base (R$) e variação máxima por trading (R$)
    private static final String[] OPCOES = {"Dollar", "Libra", "Petrobras (PETR4)", "Vale (VALE3)", "Itaú (ITUB4)"};
    private static final double[] COTACAO_BASE = {5.70, 7.45, 37.50, 60.20, 36.80};
    private static final double[] VARIACAO = {0.30, 0.30, 1.90, 3.00, 1.85};

    public static String[] getOpcoes() {
        return OPCOES.clone();
    }

    private static int indiceDe(String nome) {
        for (int i = 0; i < OPCOES.length; i++) {
            if (OPCOES[i].equals(nome)) {
                return i;
            }
        }
        return 0;
    }

    public double getCarteira1() {
        return Carteira1;
    }

    public void zerarCarteira() {
        this.Carteira1 = 0;
    }

    public void setCarteira1(double novoValor) {
        this.Carteira1 = novoValor;
    }

    public double CompraInvestimento(double valor, int qtdRatings, int escolherMoeda, String[] moeda) {
        if (valor > 0) {
            String moedaEscolhida = moeda[escolherMoeda];
            System.out.printf("\n--- Investindo em %s ---\n", moedaEscolhida);

            int indice = indiceDe(moedaEscolhida);
            double cotacaoAnterior = COTACAO_BASE[indice];
            Random gerador = new Random();

            for (int i = 1; i <= qtdRatings; i++) {
                double variacao = VARIACAO[indice];
                double cotacao = COTACAO_BASE[indice] + ((gerador.nextDouble() * 2 * variacao) - variacao);

                double valorAnterior = valor;

                // Valor para seguir a cotação anterior
                double proporcao = cotacao / cotacaoAnterior;
                valor = valor * proporcao;

                // Variação exibindo o lucro ou prejuízo em centavos
                double diferenca = valor - valorAnterior;

                System.out.printf(
                        " Trading %d | Cotação: %.2f  | Montante:  R$ %.2f | Variação: R$ %+.2f\n",
                        i, cotacao, valor, diferenca
                );
            }

            Carteira1 += valor;
            return valor;
        } else {
            System.out.println("Compra Invalida");
            return 0;
        }
    }

    public void Acoes() {
        Random gerador = new Random();

        double Dolar = 5.70 + ((gerador.nextDouble() * 0.1) - 0.05);
        double Libra = 7.45 + ((gerador.nextDouble() * 0.1) - 0.05);
        double Petrobras = 37.50 + ((gerador.nextDouble() * 0.6) - 0.3);
        double Vale = 60.20 + ((gerador.nextDouble() * 1.0) - 0.5);
        double Itau = 36.80 + ((gerador.nextDouble() * 0.6) - 0.3);

        System.out.printf("Dólar (USD): R$ %.2f\n", Dolar);
        System.out.printf("Libra Esterlina (GBP): R$ %.2f\n", Libra);
        System.out.printf("Petrobras (PETR4): R$ %.2f\n", Petrobras);
        System.out.printf("Vale (VALE3): R$ %.2f\n", Vale);
        System.out.printf("Itaú (ITUB4): R$ %.2f\n", Itau);
    }
}
