package app;

import java.util.Scanner;

public class Teste_Login {
    public static void main(String[] args){

        String usuario;
        String senha;

        Login NovoLogin = new Login("Rafaelmozzer@1","135");

        Validacaologin validacao = new Validacaologin();

        Scanner teclado = new Scanner(System.in);

        System.out.printf("Digite o usuario:");
        usuario = teclado.nextLine();

        System.out.printf("Digite a senha:");
        senha = teclado.nextLine();

        validacao.Entrar(NovoLogin,usuario,senha);
    }
}
