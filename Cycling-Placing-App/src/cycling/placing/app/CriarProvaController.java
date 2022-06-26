package cycling.placing.app;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

public class CriarProvaController implements Initializable {  
    
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
    Label LabelUsername;
    
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    private double xOffset = 0;
    private double yOffset = 0;
    
    String username;
    
    public CriarProvaController(String username) {
        this.username = username;
    }
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LabelUsername.setText("Provas de "+this.username+":");
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
