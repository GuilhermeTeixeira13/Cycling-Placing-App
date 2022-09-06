package cycling.placing.app;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import cycling.placing.app.classes.Prova;
import cycling.placing.app.DataBase.queries;
import cycling.placing.app.classes.Classificacao;
import cycling.placing.app.classes.Escalao;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
    
    PdfPTable tableClassGeral;
    
    
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
        
        float[] columnWidths = {50, 75, 200, 200, 75, 75};
        tableClassGeral = new PdfPTable(columnWidths);
        tableClassGeral.setWidthPercentage(100);

        tableClassGeral.addCell("pos");
        tableClassGeral.addCell("Dorsal");
        tableClassGeral.addCell("Nome");
        tableClassGeral.addCell("Team");
        tableClassGeral.addCell("Tempo");
        tableClassGeral.addCell("Gap");
    }

    @FXML
    public void DownloadButton(ActionEvent event) throws IOException, Exception {
        if(txtFieldPath.getText().equals("")){
            System.out.println("Especifique uma diretoria para guardar os resultados.");
        }
        else{
            String pathCompleto = txtFieldPath.getText() + "\\ClassificaçãoGeral.pdf";
            
            Document documentoPDF = new Document(); 
            
            try{
                PdfWriter.getInstance(documentoPDF, new FileOutputStream(pathCompleto));
                
                documentoPDF.open();
                
                mostraClassificacoes();
                
                documentoPDF.add(tableClassGeral);
            }catch(DocumentException de){
                de.printStackTrace();
            }catch(IOException ioe){
                ioe.printStackTrace();
            }finally{
                documentoPDF.close();
            }       
        }
    }
    
    public void mostraClassificacaoPorEscaloes(Prova Prova, Escalao Escalao) {
        ArrayList<Classificacao> classificacoesDoEscalao = queries.classificaPorEscalao(Prova.getId(), Escalao.getID());
        if (classificacoesDoEscalao.isEmpty() == false) {
            System.out.println("-------------------------------\nEscalão: " + Escalao.getNome() + " // " + Escalao.getCategoria());
            for (int k = 0; k < classificacoesDoEscalao.size(); k++) {
                System.out.println(k + 1 + "ª -> " + classificacoesDoEscalao.get(k).getNomeCiclista() + " // " + classificacoesDoEscalao.get(k).getTempoProva());
            }
            System.out.println("-------------------------------");
        } else {
            System.out.println("Ainda não há classificações no escalão " + Escalao.getNome() + " " + Escalao.getCategoria() + "[ID=" + Escalao.getID() + "] na Prova '" + Prova.getNome() + "' Distância: " + Prova.getDistancia() + "KM [ID=" + Prova.getId() + "]");
        }
    }

    public void mostraClassificacaoGeral(Prova Prova) throws ParseException {
        ArrayList<Classificacao> classificacaoGeralProva = queries.classificaGeralDaProva(Prova.getId());
        String tempoPrimeiroClassificado="", gap="";

        System.out.println("-------------------------------\n" + "Classificação GERAL:");
        for (int k = 0; k < classificacaoGeralProva.size(); k++) {
            System.out.println(k + 1 + "ª -> " + classificacaoGeralProva.get(k).getNomeCiclista() + " // " + classificacaoGeralProva.get(k).getTempoProva());
            
            int lugar = k + 1;
            tableClassGeral.addCell(String.valueOf(lugar)+"º");
            tableClassGeral.addCell(classificacaoGeralProva.get(k).getDorsal());
            tableClassGeral.addCell(classificacaoGeralProva.get(k).getNomeCiclista());
            tableClassGeral.addCell("");
            tableClassGeral.addCell(classificacaoGeralProva.get(k).getTempoProva());     
            if(k==0){
                tempoPrimeiroClassificado = classificacaoGeralProva.get(k).getTempoProva();
                tableClassGeral.addCell("");
            }
            else{
                gap = calculaGap(tempoPrimeiroClassificado, classificacaoGeralProva.get(k).getTempoProva());
                tableClassGeral.addCell(gap);
            }
        }
    }
    
    public String calculaGap(String tempoPrimeiroClassificado, String tempo) throws ParseException{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
  
        Date date1 = simpleDateFormat.parse(tempoPrimeiroClassificado);
        Date date2 = simpleDateFormat.parse(tempo);
  
        long differenceInMilliSeconds = Math.abs(date2.getTime() - date1.getTime());
  
        long differenceInHours = (differenceInMilliSeconds / (60 * 60 * 1000)) % 24;
  
        long differenceInMinutes = (differenceInMilliSeconds / (60 * 1000)) % 60;
  
        long differenceInSeconds = (differenceInMilliSeconds / 1000) % 60;
        
        String Hours = String.valueOf(differenceInHours);
        if(differenceInHours < 10)Hours = "0" + differenceInHours;
        
        String Minutes = String.valueOf(differenceInMinutes);
        if(differenceInMinutes < 10)Minutes = "0" + differenceInMinutes;
        
        String Seconds = String.valueOf(differenceInSeconds);
        if(differenceInSeconds < 10)Seconds = "0" + differenceInSeconds;
        
        return Hours + ":"+ Minutes + ":"+ Seconds;
    } 

    public void mostraClassificacoes() throws ParseException {
        for (int i = 0; i < this.prova.size(); i++) {
            System.out.println("\n******************************************************************************************************\n");

            System.out.println("Prova: '" + this.prova.get(i).getNome() + "' // Distância: " + this.prova.get(i).getDistancia() + "KM");

            mostraClassificacaoGeral(this.prova.get(i));

            ArrayList<Escalao> EscaloesDaProva = queries.getEscaloesDaProva(this.prova.get(i).getId());
            for (int j = 0; j < EscaloesDaProva.size(); j++) {
                mostraClassificacaoPorEscaloes(this.prova.get(i), EscaloesDaProva.get(j));
            }
        }
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
