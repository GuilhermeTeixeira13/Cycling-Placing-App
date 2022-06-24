package cycling.placing.app;

import cycling.placing.app.DBTables.DBConnection;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginController implements Initializable {   
    @FXML
    TextField txtFieldUsername;

    @FXML
    PasswordField passFieldPassword;
    
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
    Hyperlink criarConta;
    
    @FXML
    Hyperlink esqueceuPass;
    
    @FXML
    Label LoginResult;
    
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
    
    public void userLogin(ActionEvent event) throws IOException {
        if(txtFieldUsername.getText().isBlank() == false && passFieldPassword.getText().isBlank() == false){
            validateLogin();
        }
        else{
            LoginResult.setText("Escreva o seu nome de utilizador e password!");
        }
    }

    public void goToRegister(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Register.fxml"));
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
    
    public void validateLogin(){
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.getConnection();
        
        String verifylogin = "SELECT count(1) FROM utilizadores WHERE username = '"+ txtFieldUsername.getText() + "' AND password='"+ passFieldPassword.getText() + "'";
        
        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifylogin);
            
            while(queryResult.next()){
                if(queryResult.getInt(1) == 1){
                    LoginResult.setText("Sucesso!");
                }
                else{
                    LoginResult.setText("Login inválido. Tente outra vez.");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }
}
