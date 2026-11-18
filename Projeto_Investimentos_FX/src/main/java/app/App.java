package app;

import com.google.gson.Gson;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.*;
import javafx.util.Duration;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;


public class App extends Application {

    // Todas as telas usam o mesmo tamanho (o da tela de login)
    private static final double LARGURA = 420;
    private static final double ALTURA = 320;

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

        final Scene cenaLogin = new Scene(root, LARGURA, ALTURA);

        Validacaologin validacao = new Validacaologin();

        btnEntrar.setOnAction(event -> {
            // Ignora os espaços: "Leonel Soares" vira "LeonelSoares"
            String usuarioDigitado = txtUsuario.getText().replace(" ", "");
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
                    // Cada login tem sua própria carteira de investimentos
                    Investimentos investimentos = new Investimentos();

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
        lblStatusAcao.setWrapText(true);

        VBox painel = new VBox(12,lblMenu, lblBoasVindas, btnCarteira, btnInvestimentos, btnPagamentos, btnSair, lblStatusAcao);
        painel.setAlignment(Pos.CENTER);
        painel.setPadding(new Insets(24));

        final Scene cenaPrincipal = new Scene(painel, LARGURA, ALTURA);

        btnCarteira.setOnAction(e -> {
            Scene cenaCarteira = criarCenaCarteira(stage, investimentos, pagamentos, cenaPrincipal);
            stage.setTitle("Carteira - Projeto Investimentos FX");
            stage.setScene(cenaCarteira);
        });

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

        btnPagamentos.setOnAction(e -> {
            Scene cenaPagamentos = criarCenaPagamentos(stage, usuario, pagamentos, cenaPrincipal);
            stage.setTitle("Pagamentos - Projeto Investimentos FX");
            stage.setScene(cenaPagamentos);
        });

        btnSair.setOnAction(e -> {
            stage.setTitle("Login - Projeto Investimentos FX");
            stage.setScene(cenaLogin);
        });

        return cenaPrincipal;
    }

    private Scene criarCenaPagamentos(
            Stage stage,
            UsuarioJson usuario,
            Pagamentos pagamentos,
            Scene cenaPrincipal
    ) {
        Label lblTitulo = new Label("Pagamentos");
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button btnVoltar = new Button("Voltar");
        btnVoltar.setOnAction(e -> {
            stage.setTitle("Painel - Projeto Investimentos FX");
            stage.setScene(cenaPrincipal);
        });

        Region espaco = new Region();
        HBox.setHgrow(espaco, Priority.ALWAYS);
        HBox topo = new HBox(10, lblTitulo, espaco, btnVoltar);
        topo.setAlignment(Pos.CENTER_LEFT);

        Label lblSaldo = new Label(String.format("Saldo: R$ %.2f", pagamentos.Conta()));
        lblSaldo.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Parte de depósito
        Label lblDepositar = new Label("Depositar");
        lblDepositar.setStyle("-fx-font-weight: bold;");
        TextField txtDeposito = new TextField();
        txtDeposito.setPromptText("Valor (R$)");
        txtDeposito.setPrefWidth(110);
        Button btnDepositar = new Button("Depositar");
        HBox linhaDeposito = new HBox(8, txtDeposito, btnDepositar);

        // Parte de retirada
        Label lblRetirar = new Label("Retirar");
        lblRetirar.setStyle("-fx-font-weight: bold;");
        TextField txtRetirada = new TextField();
        txtRetirada.setPromptText("Valor (R$)");
        txtRetirada.setPrefWidth(110);
        Button btnRetirar = new Button("Retirar");
        HBox linhaRetirada = new HBox(8, txtRetirada, btnRetirar);

        Label lblMensagem = new Label();

        btnDepositar.setOnAction(e -> {
            try {
                double valor = Double.parseDouble(txtDeposito.getText().trim().replace(",", "."));
                if (pagamentos.Deposito(valor, usuario.getNome())) {
                    lblMensagem.setText(String.format("Depósito de R$ %.2f feito.", valor));
                    txtDeposito.clear();
                } else {
                    lblMensagem.setText("Digite um valor maior que zero.");
                }
            } catch (NumberFormatException ex) {
                lblMensagem.setText("Digite um número válido.");
            }
            lblSaldo.setText(String.format("Saldo: R$ %.2f", pagamentos.Conta()));
        });

        btnRetirar.setOnAction(e -> {
            try {
                double valor = Double.parseDouble(txtRetirada.getText().trim().replace(",", "."));
                if (pagamentos.Saque(valor)) {
                    lblMensagem.setText(String.format("Retirada de R$ %.2f feita.", valor));
                    txtRetirada.clear();
                } else {
                    lblMensagem.setText("Valor inválido ou maior que o saldo.");
                }
            } catch (NumberFormatException ex) {
                lblMensagem.setText("Digite um número válido.");
            }
            lblSaldo.setText(String.format("Saldo: R$ %.2f", pagamentos.Conta()));
        });

        VBox layout = new VBox(8, topo, lblSaldo, lblDepositar, linhaDeposito, lblRetirar, linhaRetirada, lblMensagem);
        layout.setPadding(new Insets(10));

        return new Scene(layout, LARGURA, ALTURA);
    }

    private Scene criarCenaCarteira(
            Stage stage,
            Investimentos investimentos,
            Pagamentos pagamentos,
            Scene cenaPrincipal
    ) {
        Label lblTitulo = new Label("Carteira");
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button btnVoltar = new Button("Voltar");
        btnVoltar.setOnAction(e -> {
            stage.setTitle("Painel - Projeto Investimentos FX");
            stage.setScene(cenaPrincipal);
        });

        Region espaco = new Region();
        HBox.setHgrow(espaco, Priority.ALWAYS);
        HBox topo = new HBox(10, lblTitulo, espaco, btnVoltar);
        topo.setAlignment(Pos.CENTER_LEFT);

        double saldo = pagamentos.Conta();
        double investido = investimentos.getTotalInvestido();

        Label lblSaldo = new Label(String.format("Saldo: R$ %.2f", saldo));
        Label lblInvestido = new Label(String.format("Investido: R$ %.2f", investido));
        Label lblTotal = new Label(String.format("Total: R$ %.2f", saldo + investido));
        lblTotal.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Label lblAtivos = new Label("Seus investimentos:");
        lblAtivos.setStyle("-fx-font-weight: bold;");

        VBox lista = new VBox(4);
        String[] opcoes = Investimentos.getOpcoes();
        for (int i = 0; i < opcoes.length; i++) {
            double valor = investimentos.getValorInvestido(i);
            if (valor > 0) {
                lista.getChildren().add(new Label(String.format("%s: R$ %.2f", opcoes[i], valor)));
            }
        }
        if (lista.getChildren().isEmpty()) {
            lista.getChildren().add(new Label("Você ainda não tem investimentos."));
        }

        VBox layout = new VBox(8, topo, lblSaldo, lblInvestido, lblTotal, lblAtivos, lista);
        layout.setPadding(new Insets(10));

        return new Scene(layout, LARGURA, ALTURA);
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
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label lblSaldo = new Label();

        Button btnVoltar = new Button("Voltar");

        Region espaco = new Region();
        HBox.setHgrow(espaco, Priority.ALWAYS);
        HBox topo = new HBox(10, lblTitulo, lblSaldo, espaco, btnVoltar);
        topo.setAlignment(Pos.CENTER_LEFT);

        ComboBox<String> cbAtivo = new ComboBox<>();
        cbAtivo.getItems().addAll(Investimentos.getOpcoes());
        cbAtivo.setValue("Dollar");

        Label lblPreco = new Label();
        lblPreco.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        HBox linhaAtivo = new HBox(10, cbAtivo, lblPreco);
        linhaAtivo.setAlignment(Pos.CENTER_LEFT);

        // O gráfico começa sem barras toda vez que a tela abre
        investimentos.limparHistorico();

        // Gráfico de barras com os últimos 25 preços
        CategoryAxis eixoX = new CategoryAxis();
        eixoX.setTickLabelsVisible(false);
        eixoX.setTickMarkVisible(false);
        NumberAxis eixoY = new NumberAxis();
        eixoY.setForceZeroInRange(false);
        eixoY.setMinorTickVisible(false);
        eixoY.setTickMarkVisible(false);
        eixoY.setTickLabelsVisible(false);
        BarChart<String, Number> grafico = new BarChart<>(eixoX, eixoY);
        grafico.setAnimated(false);
        grafico.setLegendVisible(false);
        // Sem linhas de grade, só as barras
        grafico.setHorizontalGridLinesVisible(false);
        grafico.setVerticalGridLinesVisible(false);
        grafico.setAlternativeRowFillVisible(false);
        grafico.setBarGap(0);
        grafico.setCategoryGap(7); // espaço entre as barras (menor = barras mais grossas)
        grafico.setPrefHeight(150);
        grafico.setMinHeight(0);
        VBox.setVgrow(grafico, Priority.ALWAYS);

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        grafico.getData().add(serie);

        TextField txtValor = new TextField();
        txtValor.setPromptText("Valor (R$)");
        txtValor.setPrefWidth(110);

        Button btnInvestir = new Button("Investir");
        Button btnRetirar = new Button("Retirar");
        HBox linhaBotoes = new HBox(8, txtValor, btnInvestir, btnRetirar);
        linhaBotoes.setAlignment(Pos.CENTER_LEFT);

        Label lblInvestido = new Label();
        Label lblMensagem = new Label();

        // Atualiza os textos e o gráfico
        Runnable atualizarTela = () -> {
            int i = Investimentos.indiceDe(cbAtivo.getValue());

            lblSaldo.setText(String.format("Saldo: R$ %.2f", pagamentos.Conta()));
            lblPreco.setText(String.format("R$ %.2f", investimentos.getPreco(i)));
            lblInvestido.setText(String.format("Investido em %s: R$ %.2f", cbAtivo.getValue(), investimentos.getValorInvestido(i)));

            double[] historico = investimentos.getHistorico(i);
            serie.getData().clear();
            // Sempre 25 barras; as que ainda não têm preço ficam invisíveis
            for (int j = 0; j < Investimentos.PONTOS; j++) {
                boolean temPreco = j < historico.length;
                double valor = temPreco ? historico[j] : investimentos.getPreco(i);
                XYChart.Data<String, Number> barra = new XYChart.Data<>(String.valueOf(j + 1), valor);
                serie.getData().add(barra);
                barra.getNode().setStyle("-fx-bar-fill: gray;");
                if (!temPreco) {
                    barra.getNode().setVisible(false);
                }
            }
        };

        // A cada 1 segundo os preços mudam
        Timeline relogio = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            investimentos.atualizarPrecos();
            atualizarTela.run();
        }));
        relogio.setCycleCount(Timeline.INDEFINITE);
        relogio.play();

        cbAtivo.setOnAction(e -> atualizarTela.run());

        btnInvestir.setOnAction(e -> {
            try {
                double valor = Double.parseDouble(txtValor.getText().trim().replace(",", "."));
                if (valor <= 0) {
                    lblMensagem.setText("Digite um valor maior que zero.");
                } else if (valor > pagamentos.Conta()) {
                    lblMensagem.setText("Saldo insuficiente.");
                } else {
                    int i = Investimentos.indiceDe(cbAtivo.getValue());
                    pagamentos.SaqueInv(valor);
                    investimentos.investir(i, valor);
                    txtValor.clear();
                    lblMensagem.setText(String.format("Investido R$ %.2f em %s.", valor, cbAtivo.getValue()));
                }
            } catch (NumberFormatException ex) {
                lblMensagem.setText("Digite um número válido.");
            }
            atualizarTela.run();
        });

        // O dinheiro só volta para o saldo quando clica em Retirar
        btnRetirar.setOnAction(e -> {
            int i = Investimentos.indiceDe(cbAtivo.getValue());
            double valor = investimentos.retirar(i);
            if (valor > 0) {
                pagamentos.DepositoInv(valor);
                lblMensagem.setText(String.format("Retirado R$ %.2f de %s.", valor, cbAtivo.getValue()));
            } else {
                lblMensagem.setText("Você não tem nada investido em " + cbAtivo.getValue() + ".");
            }
            atualizarTela.run();
        });

        btnVoltar.setOnAction(e -> {
            relogio.stop();
            lblStatusAcao.setText(
                    "Saldo: R$ " + String.format("%.2f", pagamentos.Conta())
                            + " | Investido: R$ " + String.format("%.2f", investimentos.getTotalInvestido())
            );
            stage.setTitle("Painel - Projeto Investimentos FX");
            stage.setScene(cenaPrincipal);
        });

        atualizarTela.run();

        VBox layout = new VBox(6, topo, linhaAtivo, grafico, linhaBotoes, lblInvestido, lblMensagem);
        layout.setPadding(new Insets(10));

        return new Scene(layout, LARGURA, ALTURA);
    }
}
