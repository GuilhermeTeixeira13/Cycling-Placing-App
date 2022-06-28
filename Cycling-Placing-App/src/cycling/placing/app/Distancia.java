package cycling.placing.app;

public class Distancia {
    private int dist;

    public Distancia(Integer dist) {
        this.dist = dist;
    }
    
    public Integer getDist() {
        return dist;
    }

    public void setDist(Integer dist) {
        this.dist = dist;
    }
    
    public String toString(){
        return dist+"";  
    }  
}
