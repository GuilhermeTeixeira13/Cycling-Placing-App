package cycling.placing.app;

import cycling.placing.app.DataBase.DBConnection;
import cycling.placing.app.DataBase.queries;
import cycling.placing.app.classes.Prova;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SelectProvaController implements Initializable {

    @FXML
    private BorderPane sceneBorderPane;

    @FXML
    ImageView minimizeLogo;

    @FXML
    Button btnNovaProva1;

    @FXML
    Button btnNovaProva2;

    @FXML
    Button btnNovaProva3;

    @FXML
    Button[] ProvasButtons;

    @FXML
    Label LabelUsername;

    ArrayList<String> provasCriadas = new ArrayList<String>();

    private Stage stage;
    private Scene scene;
    private Parent root;

    private double xOffset = 0;
    private double yOffset = 0;

    String username;

    public SelectProvaController(String username) {
        this.username = username;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializeButtonArray();

        LabelUsername.setText("Provas de " + this.username + ":");

        String ownerId = queries.getOwnerID(this.username);

        int numProvas = queries.contaProvasUser(ownerId);
        System.out.println("O número de provas de " + ownerId + " é " + numProvas + ".");

        provasCriadas = queries.nomeProvasCriadas(ownerId);
        System.out.println(provasCriadas);

        for (int i = 0; i < provasCriadas.size(); i++) {
            Button button = ProvasButtons[i];
            button.setText(provasCriadas.get(i));
        }
    }

    public void inicializeButtonArray() {
        ProvasButtons = new Button[3];
        ProvasButtons[0] = btnNovaProva1;
        ProvasButtons[1] = btnNovaProva2;
        ProvasButtons[2] = btnNovaProva3;
        
        for (int i = 0; i < ProvasButtons.length; i++) {
            Button but = ProvasButtons[i];
            but.setText("Nova Prova");
        }
    }

    public void NovaProvaButtonAction(ActionEvent event) throws IOException {
        String btnString = ((Button) event.getSource()).getText();
        
        if (btnString.equals("Nova Prova")) {
            CriarProvaController criarProvaController = new CriarProvaController(username);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CriarProva.fxml"));
            loader.setController(criarProvaController);
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
        } else {
            ArrayList<Prova> prova = queries.getProva(btnString, queries.getOwnerID(this.username));
            ProvaController provaController = new ProvaController(prova, queries.getOwnerID(this.username));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Prova.fxml"));
            loader.setController(provaController);
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
    }
    
    public void eliminaDadosProva(int numprova){
        queries.eliminaCiclista(queries.getOwnerID(this.username), provasCriadas.get(numprova));
        queries.eliminaParticipacao(queries.getOwnerID(this.username), provasCriadas.get(numprova));
        queries.eliminaEscalao(queries.getOwnerID(this.username), provasCriadas.get(numprova));
        queries.eliminaProvaPorNome(queries.getOwnerID(this.username), provasCriadas.get(numprova));
    }

    @FXML
    public void eliminaProva1(MouseEvent event) {
        String nomeProva = btnNovaProva1.getText();

        if (!nomeProva.equals("Nova Prova")) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("DELETE!");
            alert.setHeaderText("Está prestes a eliminar a prova " + nomeProva + ".");
            alert.setContentText("Tem a certeza que quer eliminar " + nomeProva + "?");
            if (alert.showAndWait().get() == ButtonType.OK) {
                eliminaDadosProva(0);
                btnNovaProva1.setText("Nova Prova");
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setHeaderText("Não é possível eliminar uma prova que ainda não existe.");
            alert.showAndWait();
        }

    }

    @FXML
    public void eliminaProva2(MouseEvent event) {
        String nomeProva = btnNovaProva2.getText();

        if (!nomeProva.equals("Nova Prova")) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("DELETE!");
            alert.setHeaderText("Está prestes a eliminar a prova '" + nomeProva + "'.");
            alert.setContentText("Tem a certeza que quer eliminar '" + nomeProva + "'?");
            if (alert.showAndWait().get() == ButtonType.OK) {
                eliminaDadosProva(1);        
                btnNovaProva2.setText("Nova Prova");
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setHeaderText("Não é possível eliminar uma prova que ainda não existe.");
            alert.showAndWait();
        }
    }

    @FXML
    public void eliminaProva3(MouseEvent event) {
        String nomeProva = btnNovaProva3.getText();

        if (!nomeProva.equals("Nova Prova")) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("DELETE!");
            alert.setHeaderText("Está prestes a eliminar a prova '" + nomeProva + "'.");
            alert.setContentText("Tem a certeza que quer eliminar '" + nomeProva + "'?");
            if (alert.showAndWait().get() == ButtonType.OK) {
                eliminaDadosProva(2);
                btnNovaProva3.setText("Nova Prova");
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setHeaderText("Não é possível eliminar uma prova que ainda não existe.");
            alert.showAndWait();
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
