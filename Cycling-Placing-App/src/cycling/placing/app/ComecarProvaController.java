package cycling.placing.app;

import cycling.placing.app.DataBase.queries;
import cycling.placing.app.classes.Prova;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ComecarProvaController implements Initializable {

    @FXML
    private BorderPane sceneBorderPane;

    @FXML
    ImageView minimizeLogo;

    @FXML
    Label LabelCronometro;
    
    @FXML
    Label LabelAvisos;

    @FXML
    Button btnIniciarTerminarProva;

    @FXML
    Button btnRegistarDorsal;

    @FXML
    TextField txtFieldDorsal;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private double xOffset = 0;
    private double yOffset = 0;

    ArrayList<Prova> prova;
    String OwnerID;

    Timeline timeline;
    LocalTime time = LocalTime.parse("00:00:00");
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

    public ComecarProvaController(ArrayList<Prova> prova, String OwnerID) {
        this.prova = prova;
        this.OwnerID = OwnerID;
    }

    private void incrementTime() {
        time = time.plusSeconds(1);
        LabelCronometro.setText(time.format(dtf));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        timeline = new Timeline(new KeyFrame(Duration.millis(1000), ae -> incrementTime()));
        timeline.setCycleCount(Animation.INDEFINITE);
        
        txtFieldDorsal.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtFieldDorsal.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    @FXML
    public void RegistarDorsalButtonAction(ActionEvent event) throws IOException {
        if(txtFieldDorsal.getText().equals("")){
            LabelAvisos.setText("Escreva um número de dorsal para o conseguir registar.");
        }
        else{
            boolean dorsalExisteNaProva = false;
            String idProva = "";
            
            for(int i=0; i<this.prova.size() && dorsalExisteNaProva == false; i++){
                dorsalExisteNaProva = queries.DorsalExisteNaProva(this.prova.get(i).getId(), txtFieldDorsal.getText());
                idProva = this.prova.get(i).getId();
            }
            
            if(dorsalExisteNaProva == true){
                queries.RegistarTempo(idProva, txtFieldDorsal.getText(), LabelCronometro.getText());
                LabelAvisos.setText("Dorsal " + txtFieldDorsal.getText() + " terminou a prova em " + LabelCronometro.getText() + ".");
            }
            else{
                LabelAvisos.setText("Este dorsal não existe na prova.");
            }
        }
    }

    @FXML
    public void IniciarTerminarProvaButtonAction(ActionEvent event) throws IOException {
        if (timeline.getStatus().equals(Animation.Status.STOPPED)) {
            timeline.play();
            btnIniciarTerminarProva.setText("TerminarProva");
        } else if (timeline.getStatus().equals(Animation.Status.RUNNING)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Terminar Prova!");
            alert.setHeaderText("Está prestes a terminar a prova.");
            alert.setContentText("Tem a certeza que a quer terminar? ");

            if (alert.showAndWait().get() == ButtonType.OK) {
                timeline.pause();

                ProvaController provaController = new ProvaController(this.prova, this.OwnerID);
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
