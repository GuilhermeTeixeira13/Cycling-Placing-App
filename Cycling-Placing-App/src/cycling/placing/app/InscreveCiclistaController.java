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
import javafx.event.ActionEvent;

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
    Label labelAvisos;
    
    @FXML
    ChoiceBox<String> choiceBoxDistancias;
    
    @FXML
    ChoiceBox<String> choiceBoxCategorias;
    
    String nomeProva;
    
    Escalao escalaoInscrito;
    
    String OwnerID;
    
    ArrayList<String> categoriasList = new ArrayList<String>();
    
    ArrayList<Prova> prova = new ArrayList<Prova>();

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
        
        ArrayList<String> distancias = new ArrayList<String>();
        prova = getProva(this.nomeProva, this.OwnerID);
        
        for(int i=0; i<prova.size(); i++){
            distancias.add(prova.get(i).getDistancia());
        }

        choiceBoxDistancias.getItems().setAll(distancias);
        
        LocalDate dataProva = LocalDate.parse(prova.get(0).getDataRealziacao());
        
        datePickerDataNascimento.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> ov, LocalDate t, LocalDate t1) {   
                LocalDate dataNascimento = datePickerDataNascimento.getValue();      
                String categoria = choiceBoxCategorias.getValue();
                String dist = choiceBoxDistancias.getValue();      
                int idade = calculaIdadeNoDiaDaProva(dataNascimento, dataProva);
        
                if(dataNascimento != null && categoria != null && dist != null){
                    escalaoInscrito = descobreEscalaoPelaIdade(prova.get(0).getId(), String.valueOf(idade), categoria);
                    if(escalaoInscrito.getNome().equals("")){
                        labelEscalao.setText("Escalão: Não definido.");
                    }else{
                        labelEscalao.setText("Escalão: "+ escalaoInscrito.getNome());
                    }
                }  
            }
        });
        
        choiceBoxDistancias.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends java.lang.String> ov, java.lang.String t, java.lang.String t1) {
                LocalDate dataNascimento = datePickerDataNascimento.getValue();      
                String categoria = choiceBoxCategorias.getValue();
                String dist = choiceBoxDistancias.getValue();      
                int idade = calculaIdadeNoDiaDaProva(dataNascimento, dataProva);
                
                if(dataNascimento != null && categoria != null && dist != null){
                    escalaoInscrito = descobreEscalaoPelaIdade(prova.get(0).getId(), String.valueOf(idade), categoria);
                    if(escalaoInscrito.getNome().equals("")){
                        labelEscalao.setText("Escalão: Não definido.");
                    }else{
                        labelEscalao.setText("Escalão: "+ escalaoInscrito.getNome());
                    }
                }   
            }
        });
        
        choiceBoxCategorias.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends java.lang.String> ov, java.lang.String t, java.lang.String t1) {
                LocalDate dataNascimento = datePickerDataNascimento.getValue();
                String categoria = choiceBoxCategorias.getValue();
                String dist = choiceBoxDistancias.getValue();      
                int idade = calculaIdadeNoDiaDaProva(dataNascimento, dataProva);
                
                if(dataNascimento != null && categoria != null && dist != null){
                    escalaoInscrito = descobreEscalaoPelaIdade(prova.get(0).getId(), String.valueOf(idade), categoria);
                    if(escalaoInscrito.getNome().equals("")){
                        labelEscalao.setText("Escalão: Não definido.");
                    }else{
                        labelEscalao.setText("Escalão: "+ escalaoInscrito.getNome());
                    }
                }        
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
    
    public Escalao descobreEscalaoPelaIdade(String IDProva, String Idade, String categoria){
        String provaID = "";
        String Nome = "";
        String ID = "";
        String Categoria = "";
        String IdadeMinima = "";
        String IdadeMaxima = "";

        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.getConnection();

        String getescalaoNome = "SELECT * FROM Escalao WHERE idProva = '" + IDProva + "' AND idadeMin <= '"+Idade+"' AND idadeMax >= '"+Idade+"' AND categoria = '"+categoria+"'";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResultgetescalaoNome = statement.executeQuery(getescalaoNome);
            while (queryResultgetescalaoNome.next()) {
                provaID = queryResultgetescalaoNome.getString("idProva");
                Nome = queryResultgetescalaoNome.getString("escalaoNome");
                ID = queryResultgetescalaoNome.getString("id");
                Categoria = queryResultgetescalaoNome.getString("categoria");
                IdadeMinima = queryResultgetescalaoNome.getString("idadeMin");
                IdadeMaxima = queryResultgetescalaoNome.getString("idadeMax");
            }
            queryResultgetescalaoNome.close();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        Escalao e;
        if(!Nome.equals("")){
            e = new Escalao(Nome, Integer.parseInt(IdadeMinima), Integer.parseInt(IdadeMaxima), Categoria);
            e.setID(ID);
            e.setprovaID(provaID);
        }
        else{
            e = new Escalao();
        }
        
        return e;
    } 
    
    public int calculaIdadeNoDiaDaProva(LocalDate dataNascimento, LocalDate dataProva){  
        if ((dataNascimento != null) && ( dataProva != null)) {
            return Period.between(dataNascimento,  dataProva).getYears();
        } else {
            return 0;
        }
    }
    
    @FXML
    public void InscreverCiclista(ActionEvent event) { 
        if(!escalaoInscrito.getNome().equals("")){
            String nome = txtFieldNomeCiclista.getText();
            LocalDate dataNascimento = datePickerDataNascimento.getValue();
            String idProva = escalaoInscrito.getprovaID();
            String idEscalao = escalaoInscrito.getID();
            String dorsal = txtFieldDorsalCiclista.getText();
           
            
            if(!existeDorsalRepetidoNaProva(idProva, dorsal)){
                Ciclista c = new Ciclista(nome, dataNascimento, this.OwnerID);
                c.registaCiclista();
                
                String idUltimoInscrito = getIDLastCiclista();
                if(idUltimoInscrito.equals(""))
                   idUltimoInscrito = "1";
                
                Participacao p = new Participacao(idEscalao, idUltimoInscrito, idProva, dorsal);
                p.registaParticipacao();
                
                labelAvisos.setText("Ciclista inscrito com sucesso.");
            }
            else{
                labelAvisos.setText("Esse dorsal já existe na prova.");
            }
          
        }
        else{
            labelAvisos.setText("Não foi possível registar o ciclista pois o escalão não está definido.");
            labelEscalao.setText("Escalão: Não definido.");
        }
    }
    
    public String getCiclista(String nome, String dataNascimento, String idade){
        String ID = "";

        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.getConnection();

        String getCiclistaID = "SELECT id FROM Ciclista WHERE nome = '" + nome + "' AND dataNascimento = '"+dataNascimento+"' AND idade = '"+idade+"'";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResultgetCiclistaID = statement.executeQuery(getCiclistaID);
            while (queryResultgetCiclistaID.next()) {
                ID = queryResultgetCiclistaID.getString("id");
            }
            queryResultgetCiclistaID.close();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        
        return ID;
    } 
    
    
    public String getIDLastCiclista(){
        String ID = "";

        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.getConnection();

        String getCiclistaID = "SELECT id FROM `ciclista` ORDER BY id DESC LIMIT 1";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResultgetCiclistaID = statement.executeQuery(getCiclistaID);
            while (queryResultgetCiclistaID.next()) {
                ID = queryResultgetCiclistaID.getString("id");
            }
            queryResultgetCiclistaID.close();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        
        return ID;
    }
    
    public boolean existeDorsalRepetidoNaProva(String idProva, String dorsal) {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.getConnection();
        String verifyDorsalRepetido = "SELECT count(1) FROM participacao WHERE idProva = '" + idProva + "' AND dorsal = '" + dorsal + "'";

        boolean DorsalRepetido = false;

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyDorsalRepetido);

            while (queryResult.next()) {
                if (queryResult.getInt(1) > 0) {
                    DorsalRepetido = true;
                }
            }
            queryResult.close();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return DorsalRepetido;
    }
}
