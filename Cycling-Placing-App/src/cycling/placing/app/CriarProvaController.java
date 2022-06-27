package cycling.placing.app;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    TextField txtFieldNomeProva;

    @FXML
    TextField txtFieldDistancias;

    @FXML
    TextField txtFieldEscalao;

    @FXML
    DatePicker datePickerdataProva;

    @FXML
    CheckBox checkboxMasculinos;

    @FXML
    CheckBox checkboxFemininos;

    @FXML
    CheckBox checkboxParaciclismo;

    @FXML
    CheckBox checkboxEBIKE;

    @FXML
    Button btnAddEscalao;

    @FXML
    Button btnVerEscaloes;

    @FXML
    Button btnCriarProva;

    @FXML
    ChoiceBox<Integer> ChoiceBoxIdadeMin;

    @FXML
    ChoiceBox<Integer> ChoiceBoxIdadeMax;

    @FXML
    ChoiceBox<String> ChoiceBoxCategoria;

    ArrayList<String> categoriasList = new ArrayList<String>();

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
        checkboxMasculinos.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(checkboxMasculinos.isSelected())
                    categoriasList.add("Masculinos");
                else
                    categoriasList.remove("Masculinos");
                System.out.println(categoriasList);
                ChoiceBoxCategoria.getItems().setAll(categoriasList);
            }
        });

        checkboxFemininos.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(checkboxFemininos.isSelected())
                    categoriasList.add("Femininos");
                else
                    categoriasList.remove("Femininos");
                System.out.println(categoriasList);
                ChoiceBoxCategoria.getItems().setAll(categoriasList);
            }
        });
        
        checkboxParaciclismo.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(checkboxParaciclismo.isSelected())
                    categoriasList.add("Paraciclismo");
                else
                    categoriasList.remove("Paraciclismo");
                System.out.println(categoriasList);
                ChoiceBoxCategoria.getItems().setAll(categoriasList);
            }
        });
              
        checkboxEBIKE.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(checkboxEBIKE.isSelected())
                    categoriasList.add("E-BIKE");
                else
                    categoriasList.remove("E-BIKE");
                System.out.println(categoriasList);
                ChoiceBoxCategoria.getItems().setAll(categoriasList);
            }
        });
    }

    @FXML
    public void minimizeClicked(MouseEvent event) {
        Stage stage = (Stage) minimizeLogo.getScene().getWindow();
        stage.setIconified(true);
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
}
