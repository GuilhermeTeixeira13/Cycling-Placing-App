package cycling.placing.app;

import cycling.placing.app.DBTables.DBConnection;
import java.time.LocalDate;
import java.time.Period;
import java.sql.Connection;
import java.sql.Statement;

public class Ciclista {
    private String nome;
    private int dorsal;
    LocalDate dataNascimento;

    public Ciclista(String nome, int dorsal, LocalDate dataNascimento) {
        this.nome = nome;
        this.dorsal = dorsal;
        this.dataNascimento = dataNascimento;
    }

    public String getNome() {
        return nome;
    }

    public int getDorsal() {
        return dorsal;
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

    public void setDorsal(int dorsal) {
        this.dorsal = dorsal;
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

        String insertFields = "INSERT INTO ciclista(nome, dataNascimento, idade, dorsal) VALUES ('";
        String insertValues = this.nome + "', DATE '" + this.dataNascimento + "', " + calculaIdade()+ ", "+ this.dorsal + ")";
        String insertToProva = insertFields + insertValues;
        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(insertToProva);
            System.out.println("Prova registada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}
