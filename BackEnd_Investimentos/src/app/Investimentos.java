package app;

public class Investimentos {

    private int Carteira1 = 0;

    public int getCarteira1(){
        return Carteira1;
    }

    public void CompraInvestimento(int valor){
        if(valor > 0){
            Carteira1 += valor;
            System.out.printf("Compra Realizada");
        }else{
            System.out.printf("Compra Invalida");
        }
    }
}