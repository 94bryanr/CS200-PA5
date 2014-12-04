import java.util.ArrayList;

public class Vertex{
    private String name;
    private ArrayList<Link> links;

    public Vertex(String name){
        this.name = name;
    }

    public void addLink(Link newLink){
        links.add(newLink);
    }

    public String getName() {
        return name;
    }
}
