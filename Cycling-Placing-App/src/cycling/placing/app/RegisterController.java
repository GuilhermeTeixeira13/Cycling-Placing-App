package cycling.placing.app;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class RegisterController implements Initializable {   
    @FXML
    TextField txtFieldUsername;

    @FXML
    PasswordField passFieldPassword;
    
    @FXML
    PasswordField passFieldPasswordRepeat;
    
    @FXML
    ImageView AppLogo;
    
    @FXML
    ImageView userLogo;
    
    @FXML
    ImageView passwordLogo;
    
    @FXML
    ImageView usersLogo;
    
    @FXML
    ImageView exitLogo;
    
    @FXML
    ImageView minimizeLogo;
    
    @FXML
    ImageView emailLogo;
    
    @FXML
    Hyperlink criarConta;
    
    @FXML
    Hyperlink esqueceuPass;
    
    @FXML
    private BorderPane sceneBorderPane;
    
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    private double xOffset = 0;
    private double yOffset = 0;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    
    public void userRegister(ActionEvent event) throws IOException {
    
    }

    public void goToLogin(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        
        root.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }         
        });
        
        root.setOnMouseDragged(new EventHandler<MouseEvent>(){
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
