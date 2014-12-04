import java.util.ArrayList;

public class Vertex{
    private String name;
    private ArrayList<Link> links;
    private int inDegree;

    public Vertex(String name){
        this.name = name;
        this.inDegree = 0;
    }

    public void addLink(Link newLink){
        links.add(newLink);
    }

    public String getName() {
        return name;
    }

    public int getInDegree() {
        return inDegree;
    }

    public void increaseInDegree(){
        this.inDegree++;
    }
}
