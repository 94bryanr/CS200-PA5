import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebPages{

	private HashTable hashTable;
	private Graph graph;
	public static int totalDoc = 0;
	
	// arrays for Sim calculation
	// holds place holders that represent documents (sorted in alpha order)
	public String[] docs = new String[0];
	// contains numerator of equation
	public double[] common;
	// keeps the first summation of the denominators
	public double[] docSpecific;
	// keeps second summation of denominator
	public double queryWeights;

	//constructor
	public WebPages(int hashSize){
		// creating hash table with temp value
		hashTable = new HashTable(hashSize);
		graph = new Graph();
	}
	
	public HashTable getHashTable(){
		return hashTable;
	}
	
	//add page to fill in ArrayList
	public void addPage(String filename){

		// increase number of total documents
		totalDoc++;
		// copy docs content to temp array
		String[] temp = new String[docs.length];
		for(int j = 0; j < temp.length; j++){
			temp[j] = docs[j];
		}
		// increase docs array size by 1
		docs = new String[totalDoc];
		// copy back to docs array from temp array
		for(int k = 0; k < temp.length; k++){
			docs[k] = temp[k];
		}
		// add read in file to end of docs array
		docs[docs.length-1] = filename;
		// sort docs array alphabetically
		Arrays.sort(docs);

		Graph.getVertexNames();

		try{
			File fileIn = new File(filename);

			String fullFile = buildStringFromFile(filename);
			ArrayList<String> htmlLinks = getHtmlLinks(fullFile);
			//Make a vertex for the file
			Vertex fileVertex = new Vertex(filename);
			//Update its edges
			for(String fileLink: htmlLinks){
				Link newLink = new Link(fileLink);
				fileVertex.addLink(newLink);
			}
			//Add vertex to graph
			Graph.addVertex(fileVertex);

			//Scanning file using delimiter
			Scanner scan = new Scanner(fileIn).useDelimiter("<[^>]+>|[^0-9a-zA-Z']");
			//looping through file and adding words
			while(scan.hasNext()){
				//grabbing next word and removing in-text HTML
				String nextWord = scan.next().toLowerCase();
				//adds the word to the BST
				if(!nextWord.equals(""))
				hashTable.add(filename, nextWord);
			}
			
			scan.close();

		} catch (FileNotFoundException e) {
			System.out.println("Error: File Not Found");
			e.printStackTrace();
		}
	}

	private ArrayList<String> getHtmlLinks(String string){
		//ArrayList to hold links
		ArrayList<String> htmlLinks = new ArrayList<String>();
		Matcher htmlMatches = Pattern.compile("<a *href=\"[^>]+>").matcher(string);
		while(htmlMatches.find()){
			htmlLinks.add(htmlMatches.group().toLowerCase());
		}
		htmlLinks = trimLinks(htmlLinks);
		return htmlLinks;
	}

	private ArrayList<String> trimLinks(ArrayList<String> untrimmed) {
		ArrayList<String> fileLinks = new ArrayList<String>();
		for(String link: untrimmed) {
			String[] linkArray = link.trim().split("<a *href=\"http://");
			if (linkArray.length == 2) {
				link = linkArray[1].substring(0, linkArray[1].length() - 2);
				if (!link.trim().equals("")) {
					fileLinks.add(link.toLowerCase());
				}
			}
		}
		return fileLinks;
	}

	private String buildStringFromFile(String filename){
		String fullFile = "";
		File fileIn = new File(filename);
		try {
			Scanner preScan = new Scanner(fileIn);
			while (preScan.hasNextLine()){
				fullFile += preScan.nextLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fullFile;
	}
	
	
	public void printTable(){
		for(Term term: hashTable)
        {
            System.out.println(term.getWord() + " " + term.totalFrequency);
        }
	}

	 
	 public void pruneStopWords(String word){
		 hashTable.delete(word);
	 }
	 
	 public String[] fillQueryArray(String query){
		 String[] queryArray = query.split(" ");
		 return queryArray;
	 }
	 
	 public void clearArrayData(double[] common, double[] docSpecific){
		 common = this.common;
		 docSpecific = this.docSpecific;
		 for(int i = 0; i < docs.length; i++){
			 common[i] = 0;
			 docSpecific[i] = 0;
		 }
	 }
	 
	 public String[] bestPages(String query){
		 DecimalFormat df = new DecimalFormat("###0.00");
		 // initialize all data fields to 0
		 common = new double[totalDoc];
		 docSpecific = new double[totalDoc];
		 queryWeights = 0.0;
		 clearArrayData(common, docSpecific);
		 
		 // traverse termIndex to fill queryWeights, docSpecific, and common
		 traversal(query);
		 
		 String[] returnArray = new String[2];
		 double max = 0;
		 // traverse all documents
		 for(int i = 0; i < docs.length; i++){
			 double simValue = sim(common[i], docSpecific[i], queryWeights);
			 if(simValue >= max){
				 max = simValue*Graph.inDegree(docs[i]);
				 returnArray[0] = docs[i];
				 returnArray[1] = String.valueOf(df.format(max));
			 }
		 }
		 return returnArray;
	 }

	 public double sim(double commonValue, double docSpecificValue, double queryWeightValue){
		 return commonValue/(Math.sqrt(docSpecificValue)*Math.sqrt(queryWeightValue)); 
	 }


	 // fills queryWeights, docSpecific, and common
	 public void traversal(String query){
		 
		 // fill queryArray
		 String[] queryArray = fillQueryArray(query);

		 //Traverse termIndex and compare each Term to each query word
		 for(int i = 0; i < hashTable.size(); i++){
			 Term currentTerm = hashTable.getHashTable()[i];
			 if(currentTerm != null){
				 boolean inquery = false;
				 // Scanning through all query words to check if Term is in query
				 for(int k = 0; k < queryArray.length; k++ ){
					 // a) if the term is in query, compute wiq, square it, and add to queryWeights
					 if(currentTerm.getWord().equals(queryArray[k])){
						 inquery = true;
						 double wiqTemp = (double) wiq(totalDoc, currentTerm.docFrequency, currentTerm);
						 wiqTemp = wiqTemp*wiqTemp;
						 queryWeights += wiqTemp;
					 }
				 } 
				 
				 // fill array of term documents
				 String[] termDocuments = currentTerm.fillDocArray();
				 double[] tfidfValues = currentTerm.fillTFIDFarray();
				 // for each document that contains term
				 for(int a = 0; a < termDocuments.length; a++){
					 //System.out.println("HERE : " + currentTerm.getWord() + " " + termDocuments[a]);
					 // i. compute wid value, square it, and add to doc position
					 double wid = (double) tfidfValues[a];
					 wid = wid*wid;
					 int docposition = Arrays.asList(docs).indexOf(termDocuments[a]);
					 docSpecific[docposition] += wid;
					 
					 // if term is in doc & query
					 if(inquery == true){
						 double commonValue = (double)tfidfValues[a]*((double)wiq(totalDoc, currentTerm.docFrequency, currentTerm));
						 common[docposition] += commonValue;
					 }
				 }
			 }
		 }
	 }
	 
	 public double wiq(int numTotalDocuments, int numDocumentsPerTerm, Term term){
		 return 0.5*(1+Math.log(((double)((double)numTotalDocuments/(double)numDocumentsPerTerm))));
	 }
}
