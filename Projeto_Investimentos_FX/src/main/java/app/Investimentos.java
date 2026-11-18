package app;

import java.util.Random;

public class Investimentos {

    private static final String[] OPCOES = {"Dollar", "Libra", "Petrobras (PETR4)", "Vale (VALE3)", "Itaú (ITUB4)"};

    private double[] precos = {5.70, 7.45, 37.50, 60.20, 36.80};

    // Últimos 25 preços de cada ativo (usado no gráfico)
    public static final int PONTOS = 25;
    private double[][] historico = new double[OPCOES.length][PONTOS];
    private int quantidade = 0; // quantos preços já estão no histórico

    // Quanto o usuário tem em cada ativo (sobe e desce junto com o preço)
    private double[] carteira = new double[OPCOES.length];

    private Random gerador = new Random();

    // Apaga o histórico para o gráfico começar sem barras
    public void limparHistorico() {
        quantidade = 0;
    }

    public static String[] getOpcoes() {
        return OPCOES;
    }

    public static int indiceDe(String nome) {
        for (int i = 0; i < OPCOES.length; i++) {
            if (OPCOES[i].equals(nome)) {
                return i;
            }
        }
        return 0;
    }

    // Muda os preços entre -1% e +1%
    public void atualizarPrecos() {
        for (int i = 0; i < OPCOES.length; i++) {
            double variacao = (gerador.nextDouble() * 0.02) - 0.01;
            precos[i] = precos[i] * (1 + variacao);
            carteira[i] = carteira[i] * (1 + variacao);

            if (quantidade < PONTOS) {
                // Ainda tem espaço: coloca o preço novo na próxima posição
                historico[i][quantidade] = precos[i];
            } else {
                // Cheio: anda o histórico uma posição e coloca o preço novo no final
                for (int j = 0; j < PONTOS - 1; j++) {
                    historico[i][j] = historico[i][j + 1];
                }
                historico[i][PONTOS - 1] = precos[i];
            }
        }
        if (quantidade < PONTOS) {
            quantidade++;
        }
    }

    public double getPreco(int i) {
        return precos[i];
    }

    // Devolve só os preços que já existem
    public double[] getHistorico(int i) {
        return java.util.Arrays.copyOf(historico[i], quantidade);
    }

    public double getValorInvestido(int i) {
        return carteira[i];
    }

    public double getTotalInvestido() {
        double total = 0;
        for (double valor : carteira) {
            total += valor;
        }
        return total;
    }

    public void investir(int i, double valor) {
        carteira[i] += valor;
    }

    // Retira tudo que está no ativo e devolve o valor
    public double retirar(int i) {
        double valor = carteira[i];
        carteira[i] = 0;
        return valor;
    }
}
