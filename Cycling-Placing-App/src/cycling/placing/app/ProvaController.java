package cycling.placing.app;

import cycling.placing.app.classes.Prova;
import cycling.placing.app.DataBase.queries;
import cycling.placing.app.classes.Classificacao;
import cycling.placing.app.classes.Escalao;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ProvaController implements Initializable {

    @FXML
    private BorderPane sceneBorderPane;

    @FXML
    ImageView minimizeLogo;

    @FXML
    Label LabelNomeProva;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private double xOffset = 0;
    private double yOffset = 0;

    ArrayList<Prova> prova;
    String OwnerID;

    public ProvaController(ArrayList<Prova> prova, String OwnerID) {
        this.prova = prova;
        this.OwnerID = OwnerID;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LabelNomeProva.setText(this.prova.get(0).getNome());
    }

    @FXML
    public void InscreverCiclistaButton(ActionEvent event) throws IOException {
        InscreveCiclistaController inscreveCiclistaController = new InscreveCiclistaController(this.prova.get(0).getNome(), this.OwnerID);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Inscrever.fxml"));
        loader.setController(inscreveCiclistaController);
        Parent root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });

        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void ComecarProvaButton(ActionEvent event) throws IOException {
        ComecarProvaController comecarProvaController = new ComecarProvaController(this.prova, this.OwnerID);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ComecarProva.fxml"));
        loader.setController(comecarProvaController);
        Parent root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });

        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void ResultadosButton(ActionEvent event) throws IOException {
        mostraClassificacoes();

        ResultadosPathAndGeneratorController resultadosPathAndGeneratorController = new ResultadosPathAndGeneratorController(this.prova, this.OwnerID);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ResultadosPathAndGenerator.fxml"));
        loader.setController(resultadosPathAndGeneratorController);
        Parent root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });

        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });

        stage.setScene(scene);
        stage.show();
    }

    public void mostraClassificacaoPorEscaloes(Prova Prova, Escalao Escalao) {
        ArrayList<Classificacao> classificacoesDoEscalao = queries.classificaPorEscalao(Prova.getId(), Escalao.getID());
        if (classificacoesDoEscalao.isEmpty() == false) {
            System.out.println("-------------------------------\nEscalão: " + Escalao.getNome() + " // " + Escalao.getCategoria());
            for (int k = 0; k < classificacoesDoEscalao.size(); k++) {
                System.out.println(k + 1 + "ª -> " + classificacoesDoEscalao.get(k).getNomeCiclista() + " // " + classificacoesDoEscalao.get(k).getTempoProva());
            }
            System.out.println("-------------------------------");
        } else {
            System.out.println("Ainda não há classificações no escalão " + Escalao.getNome() + " " + Escalao.getCategoria() + "[ID=" + Escalao.getID() + "] na Prova '" + Prova.getNome() + "' Distância: " + Prova.getDistancia() + "KM [ID=" + Prova.getId() + "]");
        }
    }

    public void mostraClassificacaoGeral(Prova Prova) {
        ArrayList<Classificacao> classificacaoGeralProva = queries.classificaGeralDaProva(Prova.getId());

        System.out.println("-------------------------------\n" + "Classificação GERAL:");
        for (int k = 0; k < classificacaoGeralProva.size(); k++) {
            System.out.println(k + 1 + "ª -> " + classificacaoGeralProva.get(k).getNomeCiclista() + " // " + classificacaoGeralProva.get(k).getTempoProva());
        }
    }

    public void mostraClassificacoes() {
        for (int i = 0; i < this.prova.size(); i++) {
            System.out.println("\n******************************************************************************************************\n");

            System.out.println("Prova: '" + this.prova.get(i).getNome() + "' // Distância: " + this.prova.get(i).getDistancia() + "KM");

            mostraClassificacaoGeral(this.prova.get(i));

            ArrayList<Escalao> EscaloesDaProva = queries.getEscaloesDaProva(this.prova.get(i).getId());
            for (int j = 0; j < EscaloesDaProva.size(); j++) {
                mostraClassificacaoPorEscaloes(this.prova.get(i), EscaloesDaProva.get(j));
            }
        }
    }

    @FXML
    public void exitClicked(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("EXIT!");
        alert.setHeaderText("Está prestes a sair da aplicação.");
        alert.setContentText("Tem a certeza que a quer fechar? ");

        if (alert.showAndWait().get() == ButtonType.OK) {
            stage = (Stage) sceneBorderPane.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    public void minimizeClicked(MouseEvent event) {
        Stage stage = (Stage) minimizeLogo.getScene().getWindow();
        stage.setIconified(true);
    }
}
