package app;

import java.util.Scanner;
import com.google.gson.Gson;
import java.io.FileReader;
import java.io.Reader;

public class Teste_Site {
    public static void main(String[] args) {

        int a;
        String senha;
        String usuario;
        String NovaSenha;
        String NovoUsuario;

        //Criação do json utilizando o Gson + leitura de dados


        Gson gson = new Gson();
        CarregarDados dadosCarregados = null;

        try (Reader reader = new FileReader("DadosBancarios.json")) {
            dadosCarregados = gson.fromJson(reader, CarregarDados.class);
            System.out.println(">> Dados do banco carregados com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao ler o arquivo JSON: " + e.getMessage());
            return;
        }

        //Pegando o primeiro usuario(Rafaelmozzer) e senha(1)

        UsuarioJson usuarioAtual = dadosCarregados.getUsuarios().get(0);

        //Passando os dados do Json para as classes

        Login NovoLogin = new Login(usuarioAtual.getUsuario(), usuarioAtual.getSenha());
        Validacaologin validacao = new Validacaologin();
        Investimentos Carteira = new Investimentos();

        Pagamentos Conta = new Pagamentos(usuarioAtual.getSaldo());
        Scanner teclado = new Scanner(System.in);

        //Tela de Login da Plataforma
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

                UsuarioJson usuarioLogado = null;

                //Procurando na lista Json que tem a senha e usuario

                for (UsuarioJson u : dadosCarregados.getUsuarios()) {
                    if (u.getUsuario().equals(usuario) && u.getSenha().equals(senha)) {
                        usuarioLogado = u; // usuário correto
                        break;
                    }
                }
                if (usuarioLogado != null) {
                    System.out.printf("\nBem-vindo à Plataforma, %s!\n", usuarioLogado.getNome());

                    NovoLogin = new Login(usuarioLogado.getUsuario(), usuarioLogado.getSenha());
                     Conta = new Pagamentos(usuarioLogado.getSaldo());
                    Investimentos Carteira1 = new Investimentos();

                    int num;

                    do{
                        System.out.printf("\n ------ Menu Inicial -------\n");
                        System.out.printf("(1) Para ver o Saldo na Carteira\n");
                        System.out.printf("(2) Para ver ações/Comprar\n");
                        System.out.printf("(3) Para Saque ou deposito\n");
                        System.out.printf("(4) Voltar\n");

                        num = teclado.nextInt();

                        if( num == 1 ){
                            System.out.printf("\nValor Atual: %d\n", Conta.Conta());

                        }else if( num == 2) {
                            int voltar = 1;
                            while(voltar != 0){

                                System.out.printf("\n");
                                System.out.printf("Para voltar ao Menu Inicial digite (0)\n");
                                System.out.printf("Para atualizar o Sistema manualmente digite (1)\n");
                                System.out.printf("Para uma Consulta Automática digite (2)\n");
                                voltar = teclado.nextInt();

                                if(voltar == 0){
                                    System.out.printf("Voltando ao Menu Inicial");
                                }

                                if(voltar == 1){
                                    System.out.printf("----- Preço das Moedas -------\n");
                                    Carteira1.Acoes();
                                }

                                if(voltar == 2){
                                    try {
                                        System.out.printf("----- Preço das Moedas -------\n");
                                        for(int i = 0; i < 5; i++){
                                            Carteira1.Acoes();
                                            Thread.sleep(2000);
                                        }
                                    }catch (InterruptedException e) {
                                        System.out.println("Ocorreu um erro na atualização.");
                                    }
                                }
                            }
                        }else if(num == 3) {
                            char voltar = ' ';
                            while (voltar != 'C') {

                                System.out.printf("(A) - Para Saque:\n");
                                System.out.printf("(B) - Para Deposito:\n");
                                System.out.printf("(C) Para Voltar\n");
                                voltar = teclado.next().charAt(0);

                                if( voltar == 'A' ){
                                    System.out.printf("--- Valor Atual---\n ");
                                    System.out.printf("       %d      \n",Conta.Conta());
                                    System.out.printf("Quanto Deseja Sacar: \n");
                                    int ValorSacar = teclado.nextInt();
                                    Conta.Saque(ValorSacar);
                                }
                                if( voltar == 'B' ) {
                                    System.out.printf("--- Valor Atual---\n ");
                                    System.out.printf("       %d      \n",Conta.Conta());
                                    System.out.printf("Quanto Deseja Depositar: \n");
                                    int ValorDeposito = teclado.nextInt();
                                    Conta.Deposito(ValorDeposito, usuarioAtual.getNome());
                                }
                            }
                        }
                    }while(num != 4);
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

                    validacao.validarSenha(NovoLogin, UsuarioAntigo, NovaSenha);

                } else if (a == 2) {
                    System.out.printf("Digite a Senha Antiga: ");
                    String SenhaAntiga = teclado.nextLine();

                    System.out.printf("Digite o Novo Usuario: ");
                    NovoUsuario = teclado.nextLine();

                    validacao.validarUsuario(NovoLogin, SenhaAntiga, NovoUsuario);
                }
            }
        }while (a != 3) ;

        System.out.printf("Programa encerrado");
    }
}
