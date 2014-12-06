import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;

public class Graph{
    private static ArrayList<Vertex> vertices;
    
    public Graph(){
        this.vertices = new ArrayList<Vertex>();
    }

    public static ArrayList<Vertex>
    getVertices() {
        return vertices;
    }

    public static void addVertex(Vertex newVertex){
        vertices.add(newVertex);
        //Reorder vertices alphabetically
        sortVertices();
    }

    private static void sortVertices(){
        Collections.sort(vertices);
    }

    public static Vertex getVertex(String name) {
        for(Vertex vertex: vertices){
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
            for(Vertex vertex: vertices){
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

    public static String[] getVertexNames(){
        String[] verticesList = new String[vertices.size()];
        sortVertices();
        for(int i = 0; i < vertices.size(); i++){
            verticesList[i] = vertices.get(i).getName();
        }
        return verticesList;
    }
}