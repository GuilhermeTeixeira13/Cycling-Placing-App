package cycling.placing.app;

public class Prova {
    String id;
    String ownerID;
    String nome;
    String dataRealizacao;
    String distancia;

    public Prova(String id, String ownerID, String nome, String dataRealziacao, String distancia) {
        this.id = id;
        this.ownerID = ownerID;
        this.nome = nome;
        this.dataRealizacao = dataRealziacao;
        this.distancia = distancia;
    }

    public String getId() {
        return id;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public String getNome() {
        return nome;
    }

    public String getDataRealziacao() {
        return dataRealizacao;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDataRealziacao(String dataRealziacao) {
        this.dataRealizacao = dataRealziacao;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }
}
