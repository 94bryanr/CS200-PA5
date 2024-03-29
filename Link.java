public class Link {
    private Vertex toVertex;

    /*Constructor, toVertex takes a string name of the vertex to link TO.
    * Method will search through existing vertices in GRAPH object.
    * If no matching vertex is found, a new one will be created and added
    * to the GRAPH object*/
    public Link(String toVertex){
        //Search for existing vertex to link to
        for (Vertex vertexCurrent: Graph.getVertices()){
            if(vertexCurrent.getName().equals(toVertex)) {
                //Vertex exists
                this.toVertex = vertexCurrent;
                this.toVertex.increaseInDegree();
                return;
            }
        }
        //No existing vertex exists, create a new vertex
        linkToNewVertex(toVertex);
    }

    private void linkToNewVertex(String toVertex){
        Vertex newVertex = new Vertex(toVertex);
        this.toVertex = newVertex;
        this.toVertex.increaseInDegree();
        Graph.addVertex(newVertex);
    }

    public Vertex getToVertex() {
        return toVertex;
    }
}
