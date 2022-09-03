package cycling.placing.app.classes;

public class Classificacao {
    String NomeCiclista = "";
    String TempoProva = "";
    String Categoria = "";
    String Escalao = "";

    public Classificacao(String NomeCiclista, String TempoProva, String Categoria, String Escalao) {
        this.NomeCiclista = NomeCiclista;
        this.TempoProva = TempoProva;
        this.Categoria = Categoria;
        this.Escalao = Escalao;
    }

    public String getNomeCiclista() {
        return NomeCiclista;
    }

    public String getTempoProva() {
        return TempoProva;
    }

    public String getCategoria() {
        return Categoria;
    }

    public String getEscalao() {
        return Escalao;
    }

    public void setNomeCiclista(String NomeCiclista) {
        this.NomeCiclista = NomeCiclista;
    }

    public void setTempoProva(String TempoProva) {
        this.TempoProva = TempoProva;
    }

    public void setCategoria(String Categoria) {
        this.Categoria = Categoria;
    }

    public void setEscalao(String Escalao) {
        this.Escalao = Escalao;
    }
}
