package app;

import java.util.Scanner;

public class Teste_Login {
    public static void main(String[] args) {

        String usuario;
        String senha;
        String NovaSenha;
        String NovoUsuario;
        int a;

        Login NovoLogin = new Login("Rafaelmozzer@1", "135");

        Validacaologin validacao = new Validacaologin();

        Investimentos Carteira = new Investimentos();

        Scanner teclado = new Scanner(System.in);

        do {

            System.out.printf("\n(1) - para login\n");
            System.out.printf("(2) - para recuperar senha/Usuario\n");
            System.out.printf("(3) - para Encerrar o Programa\n");

            a = teclado.nextInt();

            teclado.nextLine();

            if (a == 1) {

                System.out.printf("Digite o usuario: ");
                usuario = teclado.nextLine();

                System.out.printf("Digite a senha: ");
                senha = teclado.nextLine();

                boolean loginPermitido = validacao.Entrar(NovoLogin, usuario, senha);

                if(loginPermitido){
                    System.out.printf("\nBem vindo a Plataforma!\n");

                    int num;

                    do{
                        System.out.printf("\n ------ Menu Inicial -------\n");
                        System.out.printf("(1) Para ver o Saldo na Carteira\n");
                        System.out.printf("(2) Para ver ações no mercado\n");
                        System.out.printf("(3) Para ver o tutorial\n");
                        System.out.printf("(4) Para Saque ou deposito\n");
                        System.out.printf("(5) Voltar\n");

                        num = teclado.nextInt();

                        if( num == 1 ){
                        System.out.printf("\nValor Atual: %d\n",Carteira.getCarteira1());
                        }
                    }while(num != 5);
                }
            } else if(a == 2) {

                System.out.printf("(1) - Para trocar a Senha\n");
                System.out.printf("(2) - Para trocar o Usuario\n");
                a = teclado.nextInt();

                teclado.nextLine();

                if (a == 1) {
                    System.out.printf("Digite o Antigo Usuario: ");
                    String UsuarioAntigo = teclado.nextLine();

                    System.out.printf("Digite a Nova Senha: ");
                    NovaSenha = teclado.nextLine();

                    validacao.ValidarSenha(NovoLogin, UsuarioAntigo, NovaSenha);

                } else if (a == 2) {
                    System.out.printf("Digite a Senha Antiga: ");
                    String SenhaAntiga = teclado.nextLine();

                    System.out.printf("Digite o Novo Usuario");
                    NovoUsuario = teclado.nextLine();

                    validacao.ValidarUsuario(NovoLogin, SenhaAntiga, NovoUsuario);
                }
            }
        }while (a != 3) ;

        System.out.printf("Programa encerrado");
    }
}
