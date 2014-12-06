import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class PA5 {
	public static void main(String[] args) {
		try {
			File file = new File(args[0]);
			Scanner scan = new Scanner(file);

			ArrayList<String> htmlLinks = new ArrayList<String>();
			Pattern hyperLinkWithAngles = Pattern.compile("<[^>]+>");
			while (scan.hasNext(hyperLinkWithAngles)){
				htmlLinks.add(scan.next(hyperLinkWithAngles));
			}

			//Get output name
			String nextArg = scan.nextLine();
			String output = nextArg;
			
			Graph graph = new Graph();

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

			//System.out.println("WORDS");
			//web.printTable();
			//System.out.println();
			//web.getHashTable().printDetailedTable();

			//scanning though all whichPages words
			while(!(nextArg == null)){
				String[] pages = web.bestPages(nextArg.toLowerCase());
				String[] query = web.fillQueryArray(nextArg.toLowerCase());
				Arrays.sort(query);
				if(pages[0] == null){
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

			Graph.writeDotFile(output);
			scan.close();
			//For testing
			//String[] getAllVertices = Graph.getVertexNames();
			//for(String vert: getAllVertices){
			//	System.out.println(vert);
			//}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}