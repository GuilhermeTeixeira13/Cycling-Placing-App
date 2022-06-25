package cycling.placing.app;

import cycling.placing.app.DBTables.DBConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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
import org.mindrot.jbcrypt.BCrypt;


public class RegisterController implements Initializable {   
    @FXML
    TextField txtFieldUsername;

    @FXML
    PasswordField passFieldPasswordRegister;
    
    @FXML
    PasswordField passFieldPasswordRepeatRegister;
    
    @FXML
    TextField txtFieldEmail;
    
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
    Label RegisterConfirmationLabel;
    
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
    
    public void RegistrationButtonAction(ActionEvent event) throws IOException {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.getConnection();
        
        String verifyRegister = "SELECT count(1) FROM utilizadores WHERE username = '"+ txtFieldUsername.getText()+"'";
        
        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyRegister);
            
            while(queryResult.next()){
                // Verify if another user with the same name already exists
                if(queryResult.getInt(1) == 0 && passFieldPasswordRegister.getText().equals(passFieldPasswordRepeatRegister.getText())){
                    saveUser();
                }
                else{
                    RegisterConfirmationLabel.setText("Já existe um utilizador com esse nome.");
                }
            }
            
            queryResult.close();
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }
    
    public void saveUser(){
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.getConnection();
        
        String username = txtFieldUsername.getText();
        String email = txtFieldEmail.getText();
        String pw = BCrypt.hashpw(passFieldPasswordRegister.getText(), BCrypt.gensalt());
        
        String insertFields = "INSERT INTO utilizadores(username, email, password) VALUES ('";
        String insertValues = username + "','" + email + "','" + pw + "')";
        String insertToRegisters = insertFields + insertValues;
        
        try{
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(insertToRegisters);
            
            RegisterConfirmationLabel.setText("Registado com sucesso!");
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
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
