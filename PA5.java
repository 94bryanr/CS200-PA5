import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class PA5 {
	public static void main(String[] args) {
		try {
			File file = new File(args[0]);
			Scanner scan = new Scanner(file);

			//Get output name
			String nextArg = scan.nextLine();
			String output = nextArg;

			//Get hash size
			nextArg = scan.nextLine();
			Integer hashSize = new Integer(nextArg);
			nextArg = scan.nextLine();
			
			WebPages web = new WebPages(hashSize);

			//adding files/creating new webPages
			while(!(nextArg.equals("*EOFs*"))){
				web.addPage(nextArg);
				nextArg = scan.nextLine();
			}

			//skip EOFs
			//nextArg = scan.nextLine();
			while(!(nextArg.equals("*STOPs*"))){
				nextArg = scan.nextLine();
				web.pruneStopWords(nextArg);
			}
			
			nextArg = scan.nextLine();

			System.out.println("WORDS");
			web.printTable();
			System.out.println();
			//web.getHashTable().printDetailedTable();

			//scanning though all whichPages words
			while(!(nextArg == null)){
				String[] pages = web.bestPages(nextArg.toLowerCase());
				String[] query = web.fillQueryArray(nextArg.toLowerCase());
				Arrays.sort(query);
				if(pages == null){
					System.out.println("[" + nextArg + " ]" + " not found");
				}
				else{
					System.out.print("[");
					for(int i = 0; i < query.length; i++){
						System.out.print(query[i] + " ");
					}
					System.out.print("] in ");
					System.out.print(pages[0] + ": ");
					System.out.println(pages[1]);
				}
				if(scan.hasNext()){
					nextArg = scan.nextLine();
				}
				else{
					break;
				}
			}

			//Test Graph
			Graph graph = new Graph();
			graph.addVertex(new Vertex("Vert1"));

			graph.getVertex("Vert1").addLink(new Link("Vert2"));
			graph.getVertex("Vert2").addLink(new Link("Vert3"));
			graph.getVertex("Vert1").addLink(new Link("Vert3"));
			graph.getVertex("Vert2").addLink(new Link("Vert2"));

			//Order vertices alphabetically
			graph.addVertex(new Vertex("Apple"));
			graph.getVertex("Apple").addLink(new Link("Vert3"));
			graph.addVertex(new Vertex("Carrot"));
			graph.getVertex("Carrot").addLink(new Link("Vert3"));
			graph.addVertex(new Vertex("Wobble"));
			graph.getVertex("Wobble").addLink(new Link("Vert3"));
			graph.addVertex(new Vertex("Zapper"));
			graph.getVertex("Zapper").addLink(new Link("Vert3"));
			graph.addVertex(new Vertex("Tibby"));

			System.out.printf("In degree of %s is %d\n", "Vert1", Graph.getInDegree("Vert1"));
			System.out.printf("In degree of %s is %d\n", "Vert2", Graph.getInDegree("Vert2"));
			System.out.printf("In degree of %s is %d\n", "Vert3", Graph.getInDegree("Vert3"));

			Graph.outputGraphFile(output);

			//Keep order of edges the same

			scan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}