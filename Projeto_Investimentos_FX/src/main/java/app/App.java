package app;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.*;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;


public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label lblTitulo = new Label("Login - Investimentos");
        lblTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Usuário");
        txtUsuario.setMaxWidth(260);

        PasswordField txtSenha = new PasswordField();
        txtSenha.setPromptText("Senha");
        txtSenha.setMaxWidth(260);

        Button btnEntrar = new Button("Entrar");
        btnEntrar.setPrefWidth(120);

        Label lblMensagem = new Label();

        VBox root = new VBox(12, lblTitulo, txtUsuario, txtSenha, btnEntrar, lblMensagem);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(24));

        final Scene cenaLogin = new Scene(root, 420, 320);

        Validacaologin validacao = new Validacaologin();
        Investimentos investimentos = new Investimentos();

        btnEntrar.setOnAction(event -> {
            String usuarioDigitado = txtUsuario.getText().trim();
            String senhaDigitada = txtSenha.getText();

            if (usuarioDigitado.isEmpty() || senhaDigitada.isEmpty()) {
                lblMensagem.setText("Preencha usuário e senha.");
                lblMensagem.setStyle("-fx-text-fill: #c62828;");
                return;
            }

            Path caminhoJson = Path.of("DadosBancarios.json");
            if (!Files.exists(caminhoJson)) {
                caminhoJson = Path.of("../DadosBancarios.json");
            }
            if (!Files.exists(caminhoJson)) {
                caminhoJson = Path.of("Projeto_Investimentos_FX", "DadosBancarios.json");
            }

            try (Reader reader = Files.newBufferedReader(caminhoJson, StandardCharsets.UTF_8)) {
                Gson gson = new Gson();
                CarregarDados dadosCarregados = gson.fromJson(reader, CarregarDados.class);

                UsuarioJson usuarioEncontrado = null;
                if (dadosCarregados != null && dadosCarregados.getUsuarios() != null) {
                    for (UsuarioJson u : dadosCarregados.getUsuarios()) {
                        if (usuarioDigitado.equals(u.getUsuario())) {
                            usuarioEncontrado = u;
                            break;
                        }
                    }
                }

                if (usuarioEncontrado == null) {
                    lblMensagem.setText("Usuário ou senha incorretos.");
                    lblMensagem.setStyle("-fx-text-fill: #c62828;");
                    return;
                }

                Login contaLogin = new Login(usuarioEncontrado.getUsuario(), usuarioEncontrado.getSenha());
                boolean autenticado = validacao.Entrar(contaLogin, usuarioDigitado, senhaDigitada);

                if (autenticado) {
                    txtSenha.clear();
                    lblMensagem.setText("");

                    double saldoInicial = Double.parseDouble(String.valueOf(usuarioEncontrado.getSaldo()));
                    Pagamentos pagamentos = new Pagamentos(saldoInicial);

                    Scene cenaPrincipal = criarCenaPrincipal(
                            primaryStage,
                            usuarioEncontrado,
                            cenaLogin,
                            investimentos,
                            pagamentos
                    );
                    primaryStage.setTitle("Painel - Projeto Investimentos FX");
                    primaryStage.setScene(cenaPrincipal);
                } else {
                    lblMensagem.setText("Usuário ou senha incorretos.");
                    lblMensagem.setStyle("-fx-text-fill: #c62828;");
                }
            } catch (Exception e) {
                lblMensagem.setText("Erro ao ler DadosBancarios.json: " + e.getMessage());
                lblMensagem.setStyle("-fx-text-fill: #c62828;");
            }
        });

        primaryStage.setTitle("Login - Projeto Investimentos FX");
        primaryStage.setScene(cenaLogin);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private Scene criarCenaPrincipal(
            Stage stage,
            UsuarioJson usuario,
            Scene cenaLogin,
            Investimentos investimentos,
            Pagamentos pagamentos
    ) {
        Label lblBoasVindas = new Label("Boas Vindas - " + usuario.getNome());
        lblBoasVindas.setStyle("-fx-font-size: 15px;");

        Label lblMenu = new Label("Menu Inicial");
        lblMenu.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button btnCarteira = new Button("Carteira");
        btnCarteira.setPrefWidth(180);

        Button btnInvestimentos = new Button("Investimentos");
        btnInvestimentos.setPrefWidth(180);

        Button btnPagamentos = new Button("Pagamentos");
        btnPagamentos.setPrefWidth(180);

        Button btnSair = new Button("Sair");
        btnSair.setPrefWidth(180);

        Label lblStatusAcao = new Label();

        VBox painel = new VBox(12,lblMenu, lblBoasVindas, btnCarteira, btnInvestimentos, btnPagamentos, btnSair, lblStatusAcao);
        painel.setAlignment(Pos.CENTER);
        painel.setPadding(new Insets(24));

        final Scene cenaPrincipal = new Scene(painel, 460, 360);

        btnCarteira.setOnAction(e ->
                lblStatusAcao.setText(
                        "Carteira de " + usuario.getUsuario()
                                + " — Saldo: R$ " + String.format("%.2f", pagamentos.Conta())
                )
        );

        btnInvestimentos.setOnAction(e -> {
            Scene cenaInvestimentos = criarCenaInvestimentos(
                    stage,
                    usuario,
                    investimentos,
                    pagamentos,
                    cenaPrincipal,
                    lblStatusAcao
            );
            stage.setTitle("Investimentos - Projeto Investimentos FX");
            stage.setScene(cenaInvestimentos);
        });

        btnPagamentos.setOnAction(e ->
                lblStatusAcao.setText(
                        "Saldo atual de " + usuario.getUsuario() + ": R$ " + String.format("%.2f", pagamentos.Conta())
                )
        );

        btnSair.setOnAction(e -> {
            stage.setTitle("Login - Projeto Investimentos FX");
            stage.setScene(cenaLogin);
        });

        return cenaPrincipal;
    }

    private Scene criarCenaInvestimentos(
            Stage stage,
            UsuarioJson usuario,
            Investimentos investimentos,
            Pagamentos pagamentos,
            Scene cenaPrincipal,
            Label lblStatusAcao
    ) {
        Label lblTitulo = new Label("Investimentos");
        lblTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label lblSaldo = new Label("Saldo disponível: R$ " + String.format("%.2f", pagamentos.Conta()));
        lblSaldo.setStyle("-fx-font-size: 14px;");

        ComboBox<String> cbMoeda = new ComboBox<>();
        cbMoeda.getItems().addAll(Investimentos.getOpcoes());
        cbMoeda.setPromptText("Escolha o investimento");
        cbMoeda.setMaxWidth(240);

        TextField txtValor = new TextField();
        txtValor.setPromptText("Valor (ex.: 200)");
        txtValor.setMaxWidth(240);

        TextField txtRatings = new TextField();
        txtRatings.setPromptText("Qtd. de tradings (ex.: 2)");
        txtRatings.setMaxWidth(240);

        Button btnInvestir = new Button("Simular Compra");
        btnInvestir.setPrefWidth(180);

        Button btnVoltar = new Button("Voltar ao Painel");
        btnVoltar.setPrefWidth(180);

        Label lblResultado = new Label();
        lblResultado.setWrapText(true);
        lblResultado.setMaxWidth(360);

        btnInvestir.setOnAction(e -> {
            String moedaSelecionada = cbMoeda.getValue();
            String textoValor = txtValor.getText().trim().replace(",", ".");
            String textoRatings = txtRatings.getText().trim();

            if (moedaSelecionada == null || textoValor.isEmpty() || textoRatings.isEmpty()) {
                lblResultado.setText("Preencha investimento, valor e quantidade de tradings.");
                lblResultado.setStyle("-fx-text-fill: #c62828;");
                return;
            }

            try {
                double valor = Double.parseDouble(textoValor);
                int qtdRatings = Integer.parseInt(textoRatings);

                if (valor <= 0 || qtdRatings <= 0) {
                    lblResultado.setText("Valor e quantidade de tradings devem ser maiores que zero.");
                    lblResultado.setStyle("-fx-text-fill: #c62828;");
                    return;
                }

                if (valor > pagamentos.Conta()) {
                    lblResultado.setText("Saldo insuficiente para esse valor.");
                    lblResultado.setStyle("-fx-text-fill: #c62828;");
                    return;
                }

                String[] moedas = Investimentos.getOpcoes();
                int indiceMoeda = java.util.Arrays.asList(moedas).indexOf(moedaSelecionada);

                pagamentos.SaqueInv(valor);
                double montanteFinal = investimentos.CompraInvestimento(valor, qtdRatings, indiceMoeda, moedas);
                pagamentos.DepositoInv(montanteFinal);

                lblSaldo.setText("Saldo disponível: R$ " + String.format("%.2f", pagamentos.Conta()));
                lblResultado.setText(
                        "Operação concluída em " + moedaSelecionada + " (" + qtdRatings + " tradings). Confira os detalhes no console."
                );
                lblResultado.setStyle("-fx-text-fill: #2e7d32;");
            } catch (NumberFormatException ex) {
                lblResultado.setText("Digite números válidos para valor e tradings.");
                lblResultado.setStyle("-fx-text-fill: #c62828;");
            }
        });

        btnVoltar.setOnAction(e -> {
            lblStatusAcao.setText(
                    "Carteira de " + usuario.getUsuario()
                            + " — Saldo: R$ " + String.format("%.2f", pagamentos.Conta())
            );
            stage.setTitle("Painel - Projeto Investimentos FX");
            stage.setScene(cenaPrincipal);
        });

        VBox layout = new VBox(
                10,
                lblTitulo,
                lblSaldo,
                cbMoeda,
                txtValor,
                txtRatings,
                btnInvestir,
                btnVoltar,
                lblResultado
        );
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(24));

        return new Scene(layout, 460, 440);
    }
}