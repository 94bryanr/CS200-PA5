import java.util.ArrayList;

public class Vertex implements Comparable{
    private String name;
    private ArrayList<Link> links;
    private int inDegree;

    public Vertex(String name){
        this.name = name;
        this.links = new ArrayList<Link>();
        this.inDegree = 0;
    }

    public void addLink(Link newLink){
        links.add(newLink);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Link> getLinks() {
        return links;
    }

    public int getInDegree() {
        return inDegree;
    }

    public void increaseInDegree(){
        this.inDegree++;
    }

    @Override
    public int compareTo(Object vertex) {
        return this.getName().compareTo(((Vertex)vertex).getName());
    }
}
