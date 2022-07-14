package cycling.placing.app;

import cycling.placing.app.DBTables.DBConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.sql.Connection;
import java.util.ArrayList;
import javafx.scene.control.ChoiceBox;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Period;

public class InscreveCiclistaController implements Initializable {

    @FXML
    private BorderPane sceneBorderPane;

    @FXML
    ImageView minimizeLogo;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private double xOffset = 0;
    private double yOffset = 0;
    
    @FXML
    TextField txtFieldNomeCiclista;
    
    @FXML
    TextField txtFieldDorsalCiclista;
    
    @FXML
    DatePicker datePickerDataNascimento;
    
    @FXML
    Label labelEscalao;
    
    @FXML
    Button btnInscrever;
    
    @FXML
    ChoiceBox<String> choiceBoxDistancias;
    
    @FXML
    ChoiceBox<String> choiceBoxCategorias;
    
    String nomeProva;
    
    String OwnerID;
    
    ArrayList<String> categoriasList = new ArrayList<String>();;

    public InscreveCiclistaController(String nomeProva, String OwnerID) {
        this.nomeProva = nomeProva;
        this.OwnerID = OwnerID;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        categoriasList.add("Masculinos");
        categoriasList.add("Femininos");
        categoriasList.add("Paraciclismo Feminino");
        categoriasList.add("Paraciclismo Masculino");
        categoriasList.add("E-BIKE Feminino");
        categoriasList.add("E-BIKE Masculino");
        choiceBoxCategorias.getItems().setAll(categoriasList);
        
        ArrayList<Prova> prova = getProva(this.nomeProva, this.OwnerID);
        ArrayList<String> distancias = new ArrayList<String>();
        
        for(int i=0; i<prova.size(); i++){
            distancias.add(prova.get(i).getDistancia());
        }

        choiceBoxDistancias.getItems().setAll(distancias);
        
        LocalDate dataProva = LocalDate.parse(prova.get(0).getDataRealziacao());
        
        datePickerDataNascimento.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> ov, LocalDate t, LocalDate t1) {
                LocalDate dataNascimento = datePickerDataNascimento.getValue();
                String dist = choiceBoxDistancias.getValue();       
                String categoria = choiceBoxCategorias.getValue();
                int idade = calculaIdadeNoDiaDaProva(dataNascimento, dataProva);
                
                if(dataNascimento != null)
                    labelEscalao.setText("Escalão: "+descobreEscalao(prova.get(0).getId(), String.valueOf(idade), categoria));
            }
        });
        
        choiceBoxDistancias.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends java.lang.String> ov, java.lang.String t, java.lang.String t1) {
                LocalDate dataNascimento = datePickerDataNascimento.getValue();
                String dist = choiceBoxDistancias.getValue();
                String categoria = choiceBoxCategorias.getValue();
                int idade = calculaIdadeNoDiaDaProva(dataNascimento, dataProva);
                
                if(dataNascimento != null)
                    labelEscalao.setText("Escalão: "+descobreEscalao(prova.get(0).getId(), String.valueOf(idade), categoria));
            }
        });
        
        choiceBoxCategorias.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends java.lang.String> ov, java.lang.String t, java.lang.String t1) {
                LocalDate dataNascimento = datePickerDataNascimento.getValue();
                String dist = choiceBoxDistancias.getValue();
                String categoria = choiceBoxCategorias.getValue();
                int idade = calculaIdadeNoDiaDaProva(dataNascimento, dataProva);
                
                if(dataNascimento != null)
                    labelEscalao.setText("Escalão: "+descobreEscalao(prova.get(0).getId(), String.valueOf(idade), categoria));
            }
        });
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
    
    public ArrayList<Prova> getProva(String NomeProva, String OwnerID){
        ArrayList<Prova> prova = new ArrayList<Prova>();
        
        String id = "";
        String ownerID = "";
        String nome = "";
        String dataRealizacao = "";
        String distancia = "";
        
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.getConnection();

        String getProva = "SELECT id, ownerID, nome, dataRealizacao, distancia FROM Prova WHERE ownerID = '" + OwnerID + "' AND nome = '"+NomeProva+"'";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResultgetProva = statement.executeQuery(getProva);
            while (queryResultgetProva.next()) {
                id = queryResultgetProva.getString("id");
                ownerID = queryResultgetProva.getString("ownerID");
                nome = queryResultgetProva.getString("nome");
                dataRealizacao = queryResultgetProva.getString("dataRealizacao");
                distancia = queryResultgetProva.getString("distancia");
                
                Prova p = new Prova(id, ownerID, nome, dataRealizacao, distancia);
                prova.add(p);
            }
            queryResultgetProva.close();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        
        return prova;
    }
    
    public String descobreEscalao(String IDProva, String Idade, String categoria){
        String escalaoNome = "";

        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.getConnection();

        String getescalaoNome = "SELECT escalaoNome FROM Escalao WHERE idProva = '" + IDProva + "' AND idadeMin <= '"+Idade+"' AND idadeMax >= '"+Idade+"' AND categoria = '"+categoria+"'";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResultgetescalaoNome = statement.executeQuery(getescalaoNome);
            while (queryResultgetescalaoNome.next()) {
                escalaoNome = queryResultgetescalaoNome.getString("escalaoNome");
            }
            queryResultgetescalaoNome.close();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return escalaoNome;
    }  
    
    public int calculaIdadeNoDiaDaProva(LocalDate dataNascimento, LocalDate dataProva){  
        if ((dataNascimento != null) && ( dataProva != null)) {
            return Period.between(dataNascimento,  dataProva).getYears();
        } else {
            return 0;
        }
    }
}
