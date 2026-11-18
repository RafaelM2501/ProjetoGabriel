package app;

import java.util.Random;

public class Investimentos {

    private int Carteira1 = 500;

    public int getCarteira1(){
        return Carteira1;
    }

    public void CompraInvestimento(int valor){ //vou mexer nisso ainda
        if(valor > 0){
            Carteira1 += valor;
            System.out.printf("Compra Realizada");
        }else{
            System.out.printf("Compra Invalida");
        }
    }
    public void Acoes(){

        Random gerador = new Random();

        double Dolar = 5.70 + ((gerador.nextDouble() * 0.1) - 0.05);
        double Libra = 7.45 + ((gerador.nextDouble() * 0.1) - 0.05);

        System.out.printf("Dólar (USD): R$ %.2f\n",Dolar);
        System.out.printf("Libra Esterlina (GBP): R$ %.2f\n",Libra);
    }
}