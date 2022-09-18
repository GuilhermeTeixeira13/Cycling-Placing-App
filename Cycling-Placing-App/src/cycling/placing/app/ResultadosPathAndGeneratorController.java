package cycling.placing.app;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import cycling.placing.app.classes.Prova;
import cycling.placing.app.DataBase.queries;
import cycling.placing.app.classes.Classificacao;
import cycling.placing.app.classes.Escalao;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
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

    Document documentoPDF;

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

    public void inicializaTabela() {
        float[] columnWidths = {50, 75, 200, 200, 75, 75};
        tableClassGeral = new PdfPTable(columnWidths);
        tableClassGeral.setWidthPercentage(100);

        PdfPCell pos = new PdfPCell(new Phrase("Pos"));
        pos.setBorder(Rectangle.BOTTOM);
        pos.setBackgroundColor(Color.decode("#DFDFDF"));
        pos.setBorderWidthBottom(2);
        pos.setHorizontalAlignment(Element.ALIGN_CENTER);
        pos.setPadding(5);
        tableClassGeral.addCell(pos);

        PdfPCell dorsal = new PdfPCell(new Phrase("Dorsal"));
        dorsal.setBorder(Rectangle.BOTTOM);
        dorsal.setBackgroundColor(Color.decode("#DFDFDF"));
        dorsal.setBorderWidthBottom(2);
        dorsal.setHorizontalAlignment(Element.ALIGN_CENTER);
        dorsal.setPadding(5);
        tableClassGeral.addCell(dorsal);

        PdfPCell nome = new PdfPCell(new Phrase("Nome"));
        nome.setBorder(Rectangle.BOTTOM);
        nome.setBackgroundColor(Color.decode("#DFDFDF"));
        nome.setBorderWidthBottom(2);
        nome.setPadding(5);
        tableClassGeral.addCell(nome);

        PdfPCell team = new PdfPCell(new Phrase("Team"));
        team.setBorder(Rectangle.BOTTOM);
        team.setBackgroundColor(Color.decode("#DFDFDF"));
        team.setBorderWidthBottom(2);
        team.setPadding(5);
        tableClassGeral.addCell(team);

        PdfPCell tempo = new PdfPCell(new Phrase("Tempo"));
        tempo.setBorder(Rectangle.BOTTOM);
        tempo.setBackgroundColor(Color.decode("#DFDFDF"));
        tempo.setBorderWidthBottom(2);
        tempo.setHorizontalAlignment(Element.ALIGN_CENTER);
        tempo.setPadding(5);
        tableClassGeral.addCell(tempo);

        PdfPCell gap = new PdfPCell(new Phrase("Gap"));
        gap.setBorder(Rectangle.BOTTOM);
        gap.setBackgroundColor(Color.decode("#DFDFDF"));
        gap.setBorderWidthBottom(2);
        gap.setHorizontalAlignment(Element.ALIGN_CENTER);
        gap.setPadding(5);
        tableClassGeral.addCell(gap);
    }

    @FXML
    public void DownloadButton(ActionEvent event) throws IOException, Exception {
        if (txtFieldPath.getText().equals("")) {
            System.out.println("Especifique uma diretoria para guardar os resultados.");
        } else {
            String pathCompleto = txtFieldPath.getText() + "\\ClassificaçãoGeral.pdf";

            documentoPDF = new Document();

            try {
                PdfWriter.getInstance(documentoPDF, new FileOutputStream(pathCompleto));

                documentoPDF.open();

                mostraClassificacoes();

            } catch (DocumentException de) {
                de.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                documentoPDF.close();
            }
        }
    }

    public String mostraClassificacaoPorEscaloes(Prova Prova, Escalao Escalao) throws ParseException {
        ArrayList<Classificacao> classificacoesDoEscalao = queries.classificaPorEscalao(Prova.getId(), Escalao.getID());
        String tempoPrimeiroClassificado = "", gap = "";
        
        if (classificacoesDoEscalao.isEmpty() == false) {
            System.out.println("-------------------------------\nEscalão: " + Escalao.getNome() + " // " + Escalao.getCategoria());
            for (int k = 0; k < classificacoesDoEscalao.size(); k++) {
                System.out.println(k + 1 + "ª -> " + classificacoesDoEscalao.get(k).getNomeCiclista() + " // " + classificacoesDoEscalao.get(k).getTempoProva());

                int lugar = k + 1;

                PdfPCell pos = new PdfPCell(new Phrase(String.valueOf(lugar) + "º"));
                pos.setHorizontalAlignment(Element.ALIGN_CENTER);
                pos.setBorder(Rectangle.BOTTOM);
                tableClassGeral.addCell(pos);

                PdfPCell dorsal = new PdfPCell(new Phrase(classificacoesDoEscalao.get(k).getDorsal()));
                dorsal.setHorizontalAlignment(Element.ALIGN_CENTER);
                dorsal.setBorder(Rectangle.BOTTOM);
                tableClassGeral.addCell(dorsal);

                PdfPCell nome = new PdfPCell(new Phrase(classificacoesDoEscalao.get(k).getNomeCiclista()));
                nome.setBorder(Rectangle.BOTTOM);
                tableClassGeral.addCell(nome);

                PdfPCell team = new PdfPCell(new Phrase(""));
                team.setBorder(Rectangle.BOTTOM);
                tableClassGeral.addCell(team);

                PdfPCell tempo = new PdfPCell(new Phrase(classificacoesDoEscalao.get(k).getTempoProva()));
                tempo.setBorder(Rectangle.BOTTOM);
                tempo.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableClassGeral.addCell(tempo);

                if (k == 0) {
                    tempoPrimeiroClassificado = classificacoesDoEscalao.get(k).getTempoProva();
                    PdfPCell GapVazio = new PdfPCell(new Phrase(""));
                    GapVazio.setBorder(Rectangle.BOTTOM);
                    tableClassGeral.addCell(GapVazio);
                } else {
                    gap = calculaGap(tempoPrimeiroClassificado, classificacoesDoEscalao.get(k).getTempoProva());
                    PdfPCell Gap = new PdfPCell(new Phrase("+" + gap));
                    Gap.setBorder(Rectangle.BOTTOM);
                    Gap.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tableClassGeral.addCell(Gap);
                }
            }
            System.out.println("-------------------------------");
        } else {
            System.out.println("Ainda não há classificações no escalão " + Escalao.getNome() + " " + Escalao.getCategoria() + "[ID=" + Escalao.getID() + "] na Prova '" + Prova.getNome() + "' Distância: " + Prova.getDistancia() + "KM [ID=" + Prova.getId() + "]");
        }
        
        if(!tempoPrimeiroClassificado.equals("")){
            return tempoPrimeiroClassificado + " - " + calculaVelMediaDoVencedor(tempoPrimeiroClassificado, Prova.getDistancia()) + " km/h";
        }else{
            return "00:00:00 - 0 km/h";
        }
    }

    public String mostraClassificacaoGeral(Prova Prova) throws ParseException, DocumentException {
        ArrayList<Classificacao> classificacaoGeralProva = queries.classificaGeralDaProva(Prova.getId());
        String tempoPrimeiroClassificado = "", gap = "";

        System.out.println("-------------------------------\n" + "Classificação GERAL:");

        for (int k = 0; k < classificacaoGeralProva.size(); k++) {
            System.out.println(k + 1 + "ª -> " + classificacaoGeralProva.get(k).getNomeCiclista() + " // " + classificacaoGeralProva.get(k).getTempoProva());

            int lugar = k + 1;

            PdfPCell pos = new PdfPCell(new Phrase(String.valueOf(lugar) + "º"));
            pos.setHorizontalAlignment(Element.ALIGN_CENTER);
            pos.setBorder(Rectangle.BOTTOM);
            tableClassGeral.addCell(pos);

            PdfPCell dorsal = new PdfPCell(new Phrase(classificacaoGeralProva.get(k).getDorsal()));
            dorsal.setHorizontalAlignment(Element.ALIGN_CENTER);
            dorsal.setBorder(Rectangle.BOTTOM);
            tableClassGeral.addCell(dorsal);

            PdfPCell nome = new PdfPCell(new Phrase(classificacaoGeralProva.get(k).getNomeCiclista()));
            nome.setBorder(Rectangle.BOTTOM);
            tableClassGeral.addCell(nome);

            PdfPCell team = new PdfPCell(new Phrase(""));
            team.setBorder(Rectangle.BOTTOM);
            tableClassGeral.addCell(team);

            PdfPCell tempo = new PdfPCell(new Phrase(classificacaoGeralProva.get(k).getTempoProva()));
            tempo.setBorder(Rectangle.BOTTOM);
            tempo.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableClassGeral.addCell(tempo);

            if (k == 0) {
                tempoPrimeiroClassificado = classificacaoGeralProva.get(k).getTempoProva();
                PdfPCell GapVazio = new PdfPCell(new Phrase(""));
                GapVazio.setBorder(Rectangle.BOTTOM);
                tableClassGeral.addCell(GapVazio);
            } else if (classificacaoGeralProva.size() > 0){
                gap = calculaGap(tempoPrimeiroClassificado, classificacaoGeralProva.get(k).getTempoProva());
                PdfPCell Gap = new PdfPCell(new Phrase("+" + gap));
                Gap.setBorder(Rectangle.BOTTOM);
                Gap.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableClassGeral.addCell(Gap);
            }
        }
        
        if(!tempoPrimeiroClassificado.equals("")){
            return tempoPrimeiroClassificado + " - " + calculaVelMediaDoVencedor(tempoPrimeiroClassificado, Prova.getDistancia()) + " km/h";
        }else{
            return "00:00:00 - 0 km/h";
        }
    }

    public String calculaGap(String tempoPrimeiroClassificado, String tempo) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        Date date1 = simpleDateFormat.parse(tempoPrimeiroClassificado);
        Date date2 = simpleDateFormat.parse(tempo);

        long differenceInMilliSeconds = Math.abs(date2.getTime() - date1.getTime());

        long differenceInHours = (differenceInMilliSeconds / (60 * 60 * 1000)) % 24;

        long differenceInMinutes = (differenceInMilliSeconds / (60 * 1000)) % 60;

        long differenceInSeconds = (differenceInMilliSeconds / 1000) % 60;

        String Hours = String.valueOf(differenceInHours);
        if (differenceInHours < 10) {
            Hours = "0" + differenceInHours;
        }

        String Minutes = String.valueOf(differenceInMinutes);
        if (differenceInMinutes < 10) {
            Minutes = "0" + differenceInMinutes;
        }

        String Seconds = String.valueOf(differenceInSeconds);
        if (differenceInSeconds < 10) {
            Seconds = "0" + differenceInSeconds;
        }

        return Hours + ":" + Minutes + ":" + Seconds;
    }

    public void mostraClassificacoes() throws ParseException, DocumentException {
        for (int i = 0; i < this.prova.size(); i++) {
            System.out.println("\n******************************************************************************************************\n");

            System.out.println("Prova: '" + this.prova.get(i).getNome() + "' // Distância: " + this.prova.get(i).getDistancia() + "KM");
            
            documentoPDF.newPage();         
            inicializaTabela();
            String mediaVencedorGeral = mostraClassificacaoGeral(this.prova.get(i));
            documentoPDF.add(new Paragraph(this.prova.get(i).getNome().toUpperCase() + "\n" + converteFormatoData(this.prova.get(i).getDataRealziacao()) + "\nCLASSIFICAÇÃO GERAL\n" + this.prova.get(i).getDistancia() + "KM, winner: " + mediaVencedorGeral + "\n\n"));
            documentoPDF.add(tableClassGeral);

            ArrayList<Escalao> EscaloesDaProva = queries.getEscaloesDaProva(this.prova.get(i).getId());
            for (int j = 0; j < EscaloesDaProva.size(); j++) {
                documentoPDF.newPage();  
                inicializaTabela();
                String mediaVencedorEscalao = mostraClassificacaoPorEscaloes(this.prova.get(i), EscaloesDaProva.get(j));
                documentoPDF.add(new Paragraph(this.prova.get(i).getNome().toUpperCase() + "\n" + converteFormatoData(this.prova.get(i).getDataRealziacao()) + "\n" + EscaloesDaProva.get(j).getNome() + " " + EscaloesDaProva.get(j).getCategoria() + "\n" + this.prova.get(i).getDistancia() + "KM, winner: " + mediaVencedorEscalao + "\n\n"));
                documentoPDF.add(tableClassGeral);
            }
        }
    }

    @FXML
    public void BrowseButton(ActionEvent event) throws IOException {
        final DirectoryChooser dirchooser = new DirectoryChooser();

        Stage stage = (Stage) sceneBorderPane.getScene().getWindow();

        File file = dirchooser.showDialog(stage);

        if (file != null) {
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
    
    public String converteFormatoData(String yyyymmdd) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = sdf.parse(yyyymmdd);
        sdf.applyPattern("EEE, d MMM yyyy");
        String newDateString = sdf.format(d);
        
        return newDateString;   
    }
    
    public String calculaVelMediaDoVencedor(String tempoVencedor, String distancia) throws ParseException{
        double distKM = Double.parseDouble(distancia);
        DateFormat formatter = new SimpleDateFormat("hh:mm:ss");
        Date tempoVencedorDate = (Date)formatter.parse(tempoVencedor);
        long tempoVencedorSecs = tempoVencedorDate.getTime()/1000;

        double vMediaVencedor = ((distKM*1000)/tempoVencedorSecs)*3.6;
        return String.valueOf(vMediaVencedor);
    }
}
