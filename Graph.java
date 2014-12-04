import java.util.ArrayList;

public class Graph{
    private static ArrayList<Vertex> verticies;
    
    public Graph(){
        //Constructor placeholder
    }

    public static ArrayList<Vertex> getVertices() {
        return verticies;
    }

    public static void addVertex(Vertex newVertex){
        verticies.add(newVertex);
    }
}