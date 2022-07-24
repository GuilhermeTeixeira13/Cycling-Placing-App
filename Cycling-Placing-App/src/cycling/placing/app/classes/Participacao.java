package cycling.placing.app.classes;

import cycling.placing.app.DataBase.DBConnection;
import java.time.LocalTime;
import java.sql.Connection;
import java.sql.Statement;

public class Participacao {
    private String id;
    private String idCiclista;
    private String idEscalao;
    private String idProva;
    private String dorsal;
    private LocalTime tempo;

    public Participacao(String idEscalao, String idCiclista, String idProva, String dorsal) {
        this.idEscalao = idEscalao;
        this.idCiclista = idCiclista;
        this.idProva = idProva;
        this.dorsal = dorsal;
    }

    public String getId() {
        return id;
    }

    public String getIdCiclista() {
        return idCiclista;
    }

    public String getIdEscalao() {
        return idEscalao;
    }

    public String getIdProva() {
        return idProva;
    }

    public String getDorsal() {
        return dorsal;
    }

    public LocalTime getTempo() {
        return tempo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIdCiclista(String idCiclista) {
        this.idCiclista = idCiclista;
    }

    public void setIdEscalao(String idEscalao) {
        this.idEscalao = idEscalao;
    }

    public void setIdProva(String idProva) {
        this.idProva = idProva;
    }

    public void setDorsal(String dorsal) {
        this.dorsal = dorsal;
    }

    public void setTempo(LocalTime tempo) {
        this.tempo = tempo;
    }
    
    public void registaParticipacao() {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.getConnection();

        String insertFields = "INSERT INTO participacao(idCiclista, idProva, idEscalao, dorsal) VALUES (";
        String insertValues = this.idCiclista + ", " + this.idProva + ", " + this.idEscalao + "," + this.dorsal + ")";
        String insertToParticipacao = insertFields + insertValues;
        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(insertToParticipacao);
            System.out.println("Participação registada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}
