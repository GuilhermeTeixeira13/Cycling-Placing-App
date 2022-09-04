package cycling.placing.app;

import cycling.placing.app.classes.Prova;
import cycling.placing.app.DataBase.queries;
import cycling.placing.app.classes.Classificacao;
import cycling.placing.app.classes.Escalao;
import java.io.File;
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
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class ResultadosPathAndGeneratorController implements Initializable {

    @FXML
    private BorderPane sceneBorderPane;

    @FXML
    ImageView minimizeLogo;
    
    @FXML
    TextField txtFieldPath;
    
    private Stage stage;
    private Scene scene;
    private Parent root;

    private double xOffset = 0;
    private double yOffset = 0;

    ArrayList<Prova> prova;
    String OwnerID;

    public ResultadosPathAndGeneratorController(ArrayList<Prova> prova, String OwnerID) {
        this.prova = prova;
        this.OwnerID = OwnerID;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtFieldPath.setEditable(false);
    }

    @FXML
    public void DownloadButton(ActionEvent event) throws IOException {
        
    }

    @FXML
    public void BrowseButton(ActionEvent event) throws IOException {
        final DirectoryChooser dirchooser = new DirectoryChooser();
        
        Stage stage = (Stage) sceneBorderPane.getScene().getWindow();
        
        File file = dirchooser.showDialog(stage);
        
        if(file != null){
            txtFieldPath.setText(file.getAbsolutePath());
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
