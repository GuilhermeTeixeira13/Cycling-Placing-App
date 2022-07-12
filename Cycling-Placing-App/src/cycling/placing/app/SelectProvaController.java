package cycling.placing.app;

import cycling.placing.app.DBTables.DBConnection;
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
    Button[] arrayButtons;

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

        for (int i = 0; i < arrayButtons.length; i++) {
            Button but = arrayButtons[i];
            but.setText("Nova Prova");
        }

        LabelUsername.setText("Provas de " + this.username + ":");

        String ownerId = getOwnerID();

        int numProvas = contaProvasUser(ownerId);
        System.out.println("O número de provas de " + ownerId + " é " + numProvas + ".");

        provasCriadas = nomeProvasCriadas(ownerId);
        System.out.println(provasCriadas);

        for (int i = 0; i < provasCriadas.size(); i++) {
            Button but = arrayButtons[i];
            but.setText(provasCriadas.get(i));
        }
    }

    public void inicializeButtonArray() {
        arrayButtons = new Button[3];
        arrayButtons[0] = btnNovaProva1;
        arrayButtons[1] = btnNovaProva2;
        arrayButtons[2] = btnNovaProva3;
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
            ProvaController provaController = new ProvaController(btnString);
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

    @FXML
    public void eliminaProva1(MouseEvent event) {
        String nomeProva = btnNovaProva1.getText();

        if (!nomeProva.equals("Nova Prova")) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("DELETE!");
            alert.setHeaderText("Está prestes a eliminar a prova " + nomeProva + ".");
            alert.setContentText("Tem a certeza que quer eliminar " + nomeProva + "?");
            if (alert.showAndWait().get() == ButtonType.OK) {
                eliminaEscalao(getOwnerID(), provasCriadas.get(0));
                eliminaProvaPorNome(getOwnerID(), provasCriadas.get(0));
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
                eliminaEscalao(getOwnerID(), provasCriadas.get(1));
                eliminaProvaPorNome(getOwnerID(), provasCriadas.get(1));
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
                eliminaEscalao(getOwnerID(), provasCriadas.get(2));
                eliminaProvaPorNome(getOwnerID(), provasCriadas.get(2));
                btnNovaProva3.setText("Nova Prova");
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setHeaderText("Não é possível eliminar uma prova que ainda não existe.");
            alert.showAndWait();
        }
    }

    public void eliminaProvaPorNome(String ownerID, String NomeProva) {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.getConnection();

        String deleteProva = "DELETE FROM Prova WHERE ownerID = '" + ownerID + "' AND nome = '" + NomeProva + "'";
        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(deleteProva);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

    }

    public void eliminaEscalao(String ownerID, String NomeProva) {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.getConnection();

        String selectProva = "SELECT id FROM Prova WHERE ownerID = '" + ownerID + "' AND nome = '" + NomeProva + "'";
        String deleteEscalao = "DELETE FROM Escalao WHERE idProva IN (" + selectProva + ")";
        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(deleteEscalao);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public int contaProvasUser(String ownerID) {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.getConnection();
        String verifyProvaRepetida = "SELECT count(*) FROM prova WHERE ownerID = '" + ownerID + "' GROUP BY nome";

        int numProvas = 0;

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyProvaRepetida);

            while (queryResult.next()) {
                numProvas++;
            }
            queryResult.close();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return numProvas;
    }

    public ArrayList<String> nomeProvasCriadas(String ownerID) {
        ArrayList<String> provasCriadas = new ArrayList<String>();

        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.getConnection();
        String verifyProvaRepetida = "SELECT nome FROM prova WHERE ownerID = '" + ownerID + "' GROUP BY nome";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyProvaRepetida);

            while (queryResult.next()) {
                provasCriadas.add(queryResult.getString("nome"));
            }
            queryResult.close();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return provasCriadas;
    }

    public String getOwnerID() {
        String ownerID = "";

        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.getConnection();

        String getUserID = "SELECT id FROM utilizadores WHERE username = '" + this.username + "'";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResultgetUserID = statement.executeQuery(getUserID);
            while (queryResultgetUserID.next()) {
                ownerID = queryResultgetUserID.getString("id");
            }
            queryResultgetUserID.close();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return ownerID;
    }
}
