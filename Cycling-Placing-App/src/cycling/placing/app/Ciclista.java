package cycling.placing.app;

import cycling.placing.app.DBTables.DBConnection;
import java.time.LocalDate;
import java.time.Period;
import java.sql.Connection;
import java.sql.Statement;

public class Ciclista {
    private String nome;
    private LocalDate dataNascimento;
    private String ownerID;

    public Ciclista(String nome, LocalDate dataNascimento, String ownerID) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.ownerID = ownerID;
    }

    public String getNome() {
        return nome;
    }
    
    public String getOwnerID() {
        return ownerID;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public int getIdade() {
        return calculaIdade();
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
     public void setOwnerID(String OwnerID) {
        this.ownerID = OwnerID;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    
    public int calculaIdade(){
        LocalDate currentDate = LocalDate.now();    
        if ((this.dataNascimento != null) && (currentDate != null)) {
            return Period.between(this.dataNascimento, currentDate).getYears();
        } else {
            return 0;
        }
    }
    
    public void registaCiclista() {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.getConnection();

        String insertFields = "INSERT INTO ciclista(nome, dataNascimento, idade, ownerID) VALUES ('";
        String insertValues = this.nome + "', DATE '" + this.dataNascimento + "', " + calculaIdade() + "," + ownerID + ")";
        String insertToProva = insertFields + insertValues;
        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(insertToProva);
            System.out.println("Ciclista registado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}
