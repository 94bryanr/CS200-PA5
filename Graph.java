import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Graph{
    private static ArrayList<Vertex> verticies;
    
    public Graph(){
        this.verticies = new ArrayList<Vertex>();
    }

    public static ArrayList<Vertex>
    getVertices() {
        return verticies;
    }

    public static void addVertex(Vertex newVertex){
        verticies.add(newVertex);
    }

    public static Vertex getVertex(String name) {
        for(Vertex vertex: verticies){
            if(vertex.getName().matches(name)){
                return vertex;
            }
        }
        try {
            throw new Exception("Vertex does not exist, referencing unknown vertex.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getInDegree(String vertexName){
        return getVertex(vertexName).getInDegree();
    }

    public static void outputGraphFile(String filename){
        PrintWriter writer;
        try {
            writer = new PrintWriter(filename, "UTF-8");
            //Add first line
            writer.println("digraph program5 {");
            //Iterate through each link in the graph
            for(Vertex vertex: verticies){
                for(Link link: vertex.getLinks()){
                    writer.printf("\"%s\" -> \"%s\";\n", vertex.getName(), link.getToVertex().getName());
                }
            }
            //Add trailing line
            writer.println("}");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}