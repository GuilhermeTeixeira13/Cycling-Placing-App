package cycling.placing.app;

import cycling.placing.app.classes.Distancia;
import cycling.placing.app.classes.Escalao;
import cycling.placing.app.DataBase.queries;
import cycling.placing.app.classes.Prova;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;
import java.time.LocalDate;

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
    TextField txtFieldIdadeMin;

    @FXML
    TextField txtFieldIdadeMax;

    @FXML
    CheckComboBox<String> CheckComboBoxCategoria;

    @FXML
    CheckComboBox<String> CheckComboBoxDistancia;

    @FXML
    TableView<Distancia> tableViewDistancias;

    @FXML
    TableColumn<Distancia, Integer> distanciaColumn1;

    ObservableList<Distancia> distancias;

    @FXML
    TableView<Escalao> tableViewEscaloes;

    @FXML
    TableColumn<Escalao, String> nomeEscalaoColumn;

    @FXML
    TableColumn<Escalao, Integer> idadeMinColumn;

    @FXML
    TableColumn<Escalao, Integer> idadeMaxColumn;

    @FXML
    TableColumn<Escalao, String> categoriaColumn;

    @FXML
    TableColumn<Escalao, Integer> distanciaColumn;

    @FXML
    Label labelAvisos;

    ObservableList<Escalao> escaloes;

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
        labelAvisos.setText("");

        categoriasList.add("Masculinos");
        categoriasList.add("Femininos");
        categoriasList.add("Paraciclismo Feminino");
        categoriasList.add("Paraciclismo Masculino");
        categoriasList.add("E-BIKE Feminino");
        categoriasList.add("E-BIKE Masculino");
        CheckComboBoxCategoria.getItems().setAll(categoriasList);

        txtFieldDistancias.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtFieldDistancias.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        txtFieldIdadeMin.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtFieldIdadeMin.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        txtFieldIdadeMax.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtFieldIdadeMax.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        distanciaColumn1.setCellValueFactory(new PropertyValueFactory<Distancia, Integer>("dist"));

        nomeEscalaoColumn.setCellValueFactory(new PropertyValueFactory<Escalao, String>("nome"));
        idadeMinColumn.setCellValueFactory(new PropertyValueFactory<Escalao, Integer>("idadeMin"));
        idadeMaxColumn.setCellValueFactory(new PropertyValueFactory<Escalao, Integer>("idadeMax"));
        categoriaColumn.setCellValueFactory(new PropertyValueFactory<Escalao, String>("categoria"));
        distanciaColumn.setCellValueFactory(new PropertyValueFactory<Escalao, Integer>("dist"));
    }

    @FXML
    public void submitDistancia(ActionEvent event) {
        if (txtFieldDistancias.getText().equals("")) {
            labelAvisos.setText("Preencha todos os campos!!");
        } else {
            Distancia d = new Distancia(Integer.parseInt(txtFieldDistancias.getText()));
            distancias = tableViewDistancias.getItems();
            distancias.add(d);
            tableViewDistancias.setItems(distancias);
            preencherComboBoxDistancias(distancias);
        }
    }

    @FXML
    public void submitEscalao(ActionEvent event) {
        ObservableList categoriasSelecionadas = CheckComboBoxCategoria.getCheckModel().getCheckedItems();
        ObservableList distanciasSelecionadas = CheckComboBoxDistancia.getCheckModel().getCheckedItems();

        if (categoriasSelecionadas.isEmpty() || distanciasSelecionadas.isEmpty()) {
            labelAvisos.setText("Preencha todos os campos!!");
        } else {
            categoriasSelecionadas.forEach((Object categoria) -> {
                distanciasSelecionadas.forEach((Object distancia) -> {
                    if (idadesCertas(Integer.parseInt(txtFieldIdadeMin.getText()), Integer.parseInt(txtFieldIdadeMax.getText()))) {
                        String[] parts = distancia.toString().split(" ");
                        Distancia d = new Distancia(Integer.parseInt(parts[0]));
                        Escalao e;
                        e = new Escalao(txtFieldEscalao.getText(), Integer.parseInt(txtFieldIdadeMin.getText()), Integer.parseInt(txtFieldIdadeMax.getText()), categoria.toString());
                        e.setDist(d);
                        escaloes = tableViewEscaloes.getItems();
                        escaloes.add(e);
                        tableViewEscaloes.setItems(escaloes);
                    } else {
                        labelAvisos.setText("Verifique se a idade mínima é menor que a máxima.");
                    }
                });
            });

            System.out.println("Escalões -> " + escaloes.toString());
        }
    }

    @FXML
    public void removeDistancia(ActionEvent event) {
        int selectedID = tableViewDistancias.getSelectionModel().getSelectedIndex();
        if (selectedID != -1) {
            tableViewDistancias.getItems().remove(selectedID);
            System.out.println("Distâncias -> " + distancias.toString());
            preencherComboBoxDistancias(distancias);
        } else {
            labelAvisos.setText("Selecione um item da tabela para o conseguir remover.");
        }
    }

    @FXML
    public void removeEscalao(ActionEvent event) {
        int selectedID = tableViewEscaloes.getSelectionModel().getSelectedIndex();
        if (selectedID != -1) {
            tableViewEscaloes.getItems().remove(selectedID);
            System.out.println("Escalões -> " + escaloes.toString());
        } else {
            labelAvisos.setText("Selecione um item da tabela para o conseguir remover.");
        }
    }

    public void preencherComboBoxDistancias(ObservableList<Distancia> distancias) {
        ArrayList<Distancia> ALdistancias = new ArrayList<Distancia>(distancias);
        ArrayList<String> ALdistanciasString = new ArrayList<String>();
        for (int i = 0; i < ALdistancias.size(); i++) {
            ALdistanciasString.add(ALdistancias.get(i).getDist());
        }
        CheckComboBoxDistancia.getItems().setAll(ALdistanciasString);
    }

    @FXML
    public void gravarProva(ActionEvent event) throws IOException {
        if (distancias != null && escaloes != null) {
            ArrayList<Escalao> ALescaloes = new ArrayList<Escalao>(escaloes);
            ArrayList<Distancia> ALdistancias = new ArrayList<Distancia>(distancias);
            String ownerID = queries.getOwnerID(this.username);
            String NomeProva = txtFieldNomeProva.getText();

            if (queries.existeProvaRepetida(ownerID, NomeProva)) {
                labelAvisos.setText("Você já criou uma prova com o mesmo nome!!!");
            } else {
                for (int i = 0; i < ALdistancias.size(); i++) {
                    LocalDate dataProva = datePickerdataProva.getValue();
                    String distanciaProva = ALdistancias.get(i).getDist();
                    String[] distanciaPartes = distanciaProva.split(" ");
                    String distanciaNum = distanciaPartes[0];

                    queries.registaProva(ownerID, NomeProva, dataProva.toString(), distanciaNum);
                    String provaID = queries.getProvaID(ownerID, NomeProva, distanciaNum);
                    for (int j = 0; j < ALescaloes.size(); j++) {
                        if(distanciaNum.equals(ALescaloes.get(j).getDist().toString())){
                            queries.registaEscalao(provaID, ALescaloes.get(j).getNome(), ALescaloes.get(j).getCategoria(), ALescaloes.get(j).getIdadeMin(), ALescaloes.get(j).getIdadeMax());
                        }       
                    }

                    System.out.println("Gravou Prova");
                }

                ArrayList<Prova> prova = queries.getProva(NomeProva, queries.getOwnerID(this.username));
                ProvaController provaController = new ProvaController(prova, queries.getOwnerID(this.username));
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
        } else {
            labelAvisos.setText("Preencha todos os campos!!");
        }
    }

    public boolean idadesCertas(int menor, int maior) {
        if (menor > maior) {
            return false;
        } else {
            return true;
        }
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
