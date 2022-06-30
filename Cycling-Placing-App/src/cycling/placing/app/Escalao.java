package cycling.placing.app;

public class Escalao {
    private String nome;
    private int idadeMin;
    private int idadeMax;
    private String categoria;
    private Distancia dist;

    public Escalao(String nome, int idadeMin, int idadeMax, String categoria, Distancia dist) {
        this.nome = nome;
        this.idadeMin = idadeMin;
        this.idadeMax = idadeMax;
        this.categoria = categoria;
        this.dist = dist;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setIdadeMin(int idadeMin) {
        this.idadeMin = idadeMin;
    }

    public void setIdadeMax(int idadeMax) {
        this.idadeMax = idadeMax;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setDist(Distancia dist) {
        this.dist = dist;
    }

    public String getNome() {
        return nome;
    }

    public int getIdadeMin() {
        return idadeMin;
    }

    public int getIdadeMax() {
        return idadeMax;
    }

    public String getCategoria() {
        return categoria;
    }

    public Distancia getDist() {
        return dist;
    }

    @Override
    public String toString() {
        return "Escalao{" + "nome=" + nome + ", idadeMin=" + idadeMin + ", idadeMax=" + idadeMax + ", categoria=" + categoria + ", dist=" + dist + '}';
    }  
}
