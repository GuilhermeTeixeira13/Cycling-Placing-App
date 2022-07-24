package cycling.placing.app.DataBase;

import cycling.placing.app.classes.Escalao;
import cycling.placing.app.classes.Prova;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class queries {
    public static void eliminaProvaPorNome(String ownerID, String NomeProva) {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.getConnection();

        String deleteProva = "DELETE FROM Prova WHERE ownerID = '" + ownerID + "' AND nome = '" + NomeProva + "'";
        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(deleteProva);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public static void eliminaEscalao(String ownerID, String NomeProva) {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.getConnection();

        String selectProva = "SELECT id FROM Prova WHERE ownerID = '" + ownerID + "' AND nome = '" + NomeProva + "'";
        String deleteEscalao = "DELETE FROM Escalao WHERE idProva IN (" + selectProva + ")";
        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(deleteEscalao);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
    
    public static void eliminaCiclista(String ownerID, String NomeProva) {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.getConnection();

        String selectCiclistas = "SELECT idCiclista FROM participacao, Prova WHERE Prova.ownerID = " + ownerID + " AND Prova.nome = '" + NomeProva + "' AND Prova.id = participacao.idProva";
        String deleteCiclista = "DELETE FROM Ciclista WHERE id IN (" + selectCiclistas + ")";
        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(deleteCiclista);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
    
    public static void eliminaParticipacao(String ownerID, String NomeProva) {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.getConnection();

        String selectProva = "SELECT id FROM Prova WHERE ownerID = '" + ownerID + "' AND nome = '" + NomeProva + "'";
        String deleteParticipacao = "DELETE FROM Participacao WHERE idProva IN (" + selectProva + ")";
        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(deleteParticipacao);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public static int contaProvasUser(String ownerID) {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.getConnection();
        String verifyProvaRepetida = "SELECT count(*) FROM prova WHERE ownerID = '" + ownerID + "' GROUP BY nome";

        int numProvas = 0;

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyProvaRepetida);

            while (queryResult.next()) {
                numProvas++;
            }
            queryResult.close();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return numProvas;
    }

    public static ArrayList<String> nomeProvasCriadas(String ownerID) {
        ArrayList<String> provasCriadas = new ArrayList<String>();

        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.getConnection();
        String verifyProvaRepetida = "SELECT nome FROM prova WHERE ownerID = '" + ownerID + "' GROUP BY nome";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyProvaRepetida);

            while (queryResult.next()) {
                provasCriadas.add(queryResult.getString("nome"));
            }
            queryResult.close();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return provasCriadas;
    }

    public static String getOwnerID(String username) {
        String ownerID = "";

        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.getConnection();

        String getUserID = "SELECT id FROM utilizadores WHERE username = '" + username + "'";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResultgetUserID = statement.executeQuery(getUserID);
            while (queryResultgetUserID.next()) {
                ownerID = queryResultgetUserID.getString("id");
            }
            queryResultgetUserID.close();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return ownerID;
    }
    
    public static String getProvaID(String ownerID, String NomeProva, String Distancia) {
        String provaID = "";

        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.getConnection();

        String getProvaID = "SELECT id FROM Prova WHERE ownerID = '" + ownerID + "' AND nome = '" + NomeProva + "' AND distancia = "+Distancia;
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResultgetProvaID = statement.executeQuery(getProvaID);
            while (queryResultgetProvaID.next()) {
                provaID = queryResultgetProvaID.getString("id");
            }
            queryResultgetProvaID.close();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return provaID;
    }

    public static void registaProva(String ownerID, String NomeProva, String DataProva, String distanciaProva) {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.getConnection();

        String insertFields = "INSERT INTO prova(ownerID, nome, dataRealizacao, distancia) VALUES ('";
        String insertValues = ownerID + "','" + NomeProva + "', DATE '" + DataProva + "'," + distanciaProva + ")";
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

    public static void registaEscalao(String provaID, String NomeEscalao, String Categoria, int idadeMin, int idadeMax) {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.getConnection();

        String insertFields = "INSERT INTO Escalao(idProva, escalaoNome, categoria, idadeMin, idadeMax) VALUES ('";
        String insertValues = provaID + "','" + NomeEscalao + "','" + Categoria + "'," + String.valueOf(idadeMin) + "," + String.valueOf(idadeMax) + ")";
        String insertToEscalao = insertFields + insertValues;
        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(insertToEscalao);
            System.out.println("Escalão registado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public static boolean existeProvaRepetida(String ownerID, String NomeProva) {
        DBConnection connectNow = new DBConnection();
        Connection connectDB = connectNow.getConnection();
        String verifyProvaRepetida = "SELECT count(1) FROM prova WHERE ownerID = '" + ownerID + "' AND nome = '" + NomeProva + "'";

        boolean provaRepetida = false;

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyProvaRepetida);

            while (queryResult.next()) {
                if (queryResult.getInt(1) > 0) {
                    provaRepetida = true;
                }
            }
            queryResult.close();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return provaRepetida;
    }
    
    public static ArrayList<Prova> getProva(String NomeProva, String OwnerID){
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
    
    public static Escalao descobreEscalaoPelaIdade(String IDProva, String Idade, String categoria){
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
    
    public static String getCiclista(String nome, String dataNascimento, String idade){
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
    
    
    public static String getIDLastCiclista(){
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
    
    public static boolean existeDorsalRepetidoNaProva(String idProva, String dorsal) {
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
